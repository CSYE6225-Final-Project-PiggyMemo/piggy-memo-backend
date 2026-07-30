package com.csye6225.piggymemo.service;

import org.springframework.stereotype.Service;

import com.csye6225.piggymemo.dto.SetBudgetRequest;
import com.csye6225.piggymemo.dto.SetBudgetResponse;
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
        Long id, SetBudgetRequest req
    ){
        if(profileService.getProfileFamily(id) == null) {
            
        }
        else {

        }
    }
        

    private SetBudgetResponse createDefaultBudget() {

    }

    //TODO: Overload createDefaultBudget() for family budget
    //private FamilyBudgets createDefaultBudget() {}
}
