package com.csye6225.piggymemo.service;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.csye6225.piggymemo.dto.SetBudgetRequest;
import com.csye6225.piggymemo.dto.BudgetResponse;
import com.csye6225.piggymemo.entity.Budgets;
import com.csye6225.piggymemo.entity.PersonalBudgets;
import com.csye6225.piggymemo.exception.InvalidDailyLimitException;
import com.csye6225.piggymemo.repository.PersonalBudgetsRepository;

@Service
public class BudgetService {
    private final PersonalBudgetsRepository personalBudgetsRepository;
    private final ProfileService profileService;

    public BudgetService(
        PersonalBudgetsRepository personalBudgetsRepository,
        ProfileService profileService
    ){
        this.personalBudgetsRepository = personalBudgetsRepository;
        this.profileService = profileService;
    }

    @Transactional
    public BudgetResponse setBudget(
        Long user, SetBudgetRequest req
    ){
        Budgets budget = findBudgetOrCreate(user);

        BigDecimal newMonthBudget = req.getNewMonthlyBudget(),
            newDailyLimit = req.getNewDailyLimit();
        LocalDate newPeriodFirstDay = req.getNewPeriodFirstDay();

        if(newMonthBudget != null)
            budget.setMonthlyBudget(newMonthBudget);
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

        Budgets save = saveBudget(budget);

        return new BudgetResponse(save.getMonthlyBudget(), save.getDailyLimit(), save.getPeriodFirstDay());
    }

    @Transactional
    public void deleteBudget(Long user) {
        Long family = profileService.getProfileFamily(user);

        if(family == null) {
            personalBudgetsRepository.deleteByUser(user);
        }
        else {
            //TODO: Family owner-only deletion
            // if(!FamilyService.isFamilyOwner())
            // throw new FamilyBudgetAccessDeniedException("Only owner can operate family budget");
            //familyBudgetRepository.deleteByFamily(family);
            personalBudgetsRepository.deleteByUser(user);
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
            //TODO: Family budget implementation and family-owner-only write privilege
            //if(!FamilyService.isFamilyOwner()) 
            // throw new FamilyBudgetAccessDeniedException("Only owner can operate family budget");
            //familyBudgetsRepository.findByFamily(family)
            // .orElseGet(() -> {
            //  FamilyBudgets newBudget = new FamilyBudgets();
            //  newBudget = (FamilyBudgets)setDefaultBudget(family, newBudget);
            //  return newBudget;
            //  });
            return personalBudgetsRepository.findByUser(user).orElseGet(() -> {
                PersonalBudgets newBudget = new PersonalBudgets();
                newBudget = (PersonalBudgets) setDefaultBudget(user, newBudget);
                return newBudget;
            });
        }
    }

    private Budgets setDefaultBudget(Long owner, Budgets newBudget) {
        newBudget.setOwner(owner);
        newBudget.setMonthlyBudget(new BigDecimal("100.00"));
        newBudget.setDailyLimit(new BigDecimal("100.00"));
        newBudget.setPeriodFirstDay(LocalDate.now());
        return newBudget;
    }

    private Budgets saveBudget(Budgets budget) {
        if(budget instanceof PersonalBudgets)
            return personalBudgetsRepository.save((PersonalBudgets)budget);
        else
            //TODO: return familyBudgetsRepository.save((FamilyBudgets) budget);
            return personalBudgetsRepository.save((PersonalBudgets) budget);
    }
}
