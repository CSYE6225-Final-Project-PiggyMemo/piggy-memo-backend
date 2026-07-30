package com.csye6225.piggymemo.service;

import org.springframework.stereotype.Service;

import com.csye6225.piggymemo.dto.SetBudgetRequest;
import com.csye6225.piggymemo.dto.SetBudgetResponse;
import com.csye6225.piggymemo.entity.Budgets;
import com.csye6225.piggymemo.entity.PersonalBudgets;
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
