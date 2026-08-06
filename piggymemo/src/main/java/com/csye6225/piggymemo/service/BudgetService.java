package com.csye6225.piggymemo.service;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.csye6225.piggymemo.dto.SetBudgetRequest;
import com.csye6225.piggymemo.dto.BudgetResponse;
import com.csye6225.piggymemo.entity.Budgets;
import com.csye6225.piggymemo.entity.FamilyBudgets;
import com.csye6225.piggymemo.entity.PersonalBudgets;
import com.csye6225.piggymemo.exception.BudgetNotExistException;
import com.csye6225.piggymemo.exception.FamilyBudgetAccessDeniedException;
import com.csye6225.piggymemo.exception.InvalidDailyLimitException;
import com.csye6225.piggymemo.repository.FamilyBudgetsRepository;
import com.csye6225.piggymemo.repository.PersonalBudgetsRepository;

@Service
public class BudgetService {
    private final PersonalBudgetsRepository personalBudgetsRepository;
    private final FamilyBudgetsRepository familyBudgetsRepository;
    private final ProfileService profileService;
    private final FamilyService familyService;

    public BudgetService(
        PersonalBudgetsRepository personalBudgetsRepository,
        FamilyBudgetsRepository familyBudgetsRepository,
        ProfileService profileService,
        FamilyService familyService
    ){
        this.personalBudgetsRepository = personalBudgetsRepository;
        this.familyBudgetsRepository = familyBudgetsRepository;
        this.profileService = profileService;
        this.familyService = familyService;
    }

    @Transactional
    public BudgetResponse setBudget(
        Long user, SetBudgetRequest req
    ){
        Budgets budget = findBudgetOrCreate(user);

        BigDecimal newMonthBudget = req.getNewMonthlyBudget(),
            newDailyLimit = req.getNewDailyLimit();
        LocalDate newPeriodFirstDay = req.getNewPeriodFirstDay();
        BigDecimal budgetDiff = null;

        if(newMonthBudget != null) {
            budgetDiff = newMonthBudget.subtract(budget.getMonthlyBudget());
            budget.setMonthlyBudget(newMonthBudget);
        }
        if(newDailyLimit != null)
            budget.setDailyLimit(newDailyLimit);
        if(newPeriodFirstDay != null)
            budget.setPeriodFirstDay(newPeriodFirstDay);

        if(
            budget.getDailyLimit() != null &&
            budget.getMonthlyBudget() != null &&
            budget.getDailyLimit().compareTo(budget.getMonthlyBudget()) > 0
        ) 
        throw new InvalidDailyLimitException("Daily limit cannot be more than monthly budget");

        //If budget changed, budget left will be changed as well
        if(budgetDiff != null) {
            budget.setBudgetLeft(budget.getBudgetLeft().add(budgetDiff));
        }

        Budgets save = saveBudget(budget);

        return new BudgetResponse(save.getMonthlyBudget(), save.getDailyLimit(), save.getPeriodFirstDay(), save.getBudgetLeft());
    }

    @Transactional
    public BudgetResponse getBudget(Long user) {
        Long family = profileService.getProfileFamily(user);

        if(family == null) {
            PersonalBudgets budget = personalBudgetsRepository.findByUser(user).orElseGet(() -> {
                PersonalBudgets emptyBudget = new PersonalBudgets();
                return emptyBudget;
            });

            return new BudgetResponse(budget.getMonthlyBudget(), budget.getDailyLimit(), budget.getPeriodFirstDay(), budget.getBudgetLeft());
        }
        else {
            //Any family member has read access to the family budget.
            FamilyBudgets budget = familyBudgetsRepository.findByFamily(family).orElseGet(FamilyBudgets::new);

            return new BudgetResponse(budget.getMonthlyBudget(), budget.getDailyLimit(), budget.getPeriodFirstDay(), budget.getBudgetLeft());
        }
    }

    @Transactional
    public void deleteBudget(Long user) {
        Long family = profileService.getProfileFamily(user);

        if(family == null) {
            personalBudgetsRepository.deleteByUser(user);
        }
        else {
            if(!familyService.isFamilyOwner(user, family))
                throw new FamilyBudgetAccessDeniedException("Only the family owner can delete the family budget");
            familyBudgetsRepository.deleteByFamily(family);
        }
    }

    //Change budget_left column. Positive for spending, negative for saving. Must be called when logging spending/saving.
    @Transactional(noRollbackFor = BudgetNotExistException.class)
    protected BudgetResponse subtractBudgetLeft(Long user, BigDecimal subtraction) {
        Long family = profileService.getProfileFamily(user);
        if(family == null) {
            PersonalBudgets budget = personalBudgetsRepository.findByUser(user)
                .orElseThrow(() -> new BudgetNotExistException("Personal budget doesn't exist!"));
            budget.setBudgetLeft(budget.getBudgetLeft().subtract(subtraction));
            PersonalBudgets save = personalBudgetsRepository.save(budget);

            return new BudgetResponse(
                    save.getMonthlyBudget(), save.getDailyLimit(), save.getPeriodFirstDay(), save.getBudgetLeft()
            );
        }
        else {
            //Any family member may log spending, so any member may reach this branch.
            FamilyBudgets budget = familyBudgetsRepository.findByFamily(family)
                    .orElseThrow(() -> new BudgetNotExistException("Family budget doesn't exist!"));
            budget.setBudgetLeft(budget.getBudgetLeft().subtract(subtraction));
            FamilyBudgets save = familyBudgetsRepository.save(budget);

            return new BudgetResponse(
                save.getMonthlyBudget(), save.getDailyLimit(), save.getPeriodFirstDay(), save.getBudgetLeft()
            );
        }
    }
        
    private Budgets findBudgetOrCreate(Long user) {
        Long family = profileService.getProfileFamily(user);

        if(family == null) {
            return personalBudgetsRepository.findByUser(user)
                .orElseGet(() -> {
                    PersonalBudgets newBudget = new PersonalBudgets();
                    newBudget = (PersonalBudgets)setDefaultBudget(user, newBudget);
                    return newBudget;
                });
        }
        else {
            if(!familyService.isFamilyOwner(user, family))
                throw new FamilyBudgetAccessDeniedException("Only the family owner can set the family budget");
            return familyBudgetsRepository.findByFamily(family).orElseGet(() -> {
                FamilyBudgets newBudget = new FamilyBudgets();
                newBudget = (FamilyBudgets) setDefaultBudget(family, newBudget);
                return newBudget;
            });
        }
    }

    private Budgets setDefaultBudget(Long owner, Budgets newBudget) {
        newBudget.setOwner(owner);
        newBudget.setMonthlyBudget(new BigDecimal("100.00"));
        newBudget.setDailyLimit(new BigDecimal("100.00"));
        newBudget.setPeriodFirstDay(LocalDate.now());
        newBudget.setBudgetLeft(new BigDecimal("100.00"));
        return newBudget;
    }

    private Budgets saveBudget(Budgets budget) {
        if(budget instanceof PersonalBudgets personalBudgets)
            return personalBudgetsRepository.save(personalBudgets);
        else
            return familyBudgetsRepository.save((FamilyBudgets) budget);
    }
}
