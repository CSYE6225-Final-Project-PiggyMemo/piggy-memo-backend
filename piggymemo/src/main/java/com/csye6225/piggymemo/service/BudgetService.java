package com.csye6225.piggymemo.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.csye6225.piggymemo.dto.SetBudgetRequest;
import com.csye6225.piggymemo.dto.SetBudgetResponse;
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

    public SetBudgetResponse setBudget(
        Long user, SetBudgetRequest req
    ){
        Budgets budget = findBudgetOrCreate(user);
        BigDecimal
            currMonthBudget = budget.getMonthlyBudget(),
            newMonthBudget = req.getNewMonthlyBudget(),
            currDailyLimit = budget.getDailyLimit(),
            newDailyLimit = req.getNewDailyLimit();
        if()
        
        if(newMonthBudget != null) {
            if(req.getNewDailyLimit().compareTo(newMonthBudget) > 0)
                throw new InvalidDailyLimitException("Daily limit can't be more than monthly budget!");

        }
        else if(currMonthBudget != null) {
            if()
        }


    }
        

    private Budgets findBudgetOrCreate(Long user) {
        Long family = profileService.getProfileFamily(user);

        if(family == null) {
            return personalBudgetsRepository.findByUser(user).orElseGet(() -> new PersonalBudgets());
        }
        else {
            //TODO: Family budget implementation and family-owner-only write privilege
            //if(!FamilyService.isFamilyOwner()) 
            // throw new FamilyBudgetAccessDeniedException("Only owner can operate family budget");
            //familyBudgetRepository.findByFamily(family).orElseGet(() -> new FamilyBudgets());
        }
    }
}
