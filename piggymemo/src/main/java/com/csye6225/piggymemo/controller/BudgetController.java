package com.csye6225.piggymemo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csye6225.piggymemo.dto.SetBudgetRequest;
import com.csye6225.piggymemo.dto.BudgetResponse;
import com.csye6225.piggymemo.security.CurrentUser;
import com.csye6225.piggymemo.service.BudgetService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api/budget")
public class BudgetController {
    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }
    
    @PostMapping("/set")
    public BudgetResponse setBudget(@AuthenticationPrincipal CurrentUser user, @RequestBody SetBudgetRequest req) {
        return budgetService.setBudget(user.id(), req);
    }

    @GetMapping("/fetch")
    public BudgetResponse fetchBudget(@RequestParam String param) {
        return new BudgetResponse();
    }
    
    
    @DeleteMapping("/remove")
    public ResponseEntity<Void> deleteBudget(@AuthenticationPrincipal CurrentUser user, @RequestBody String req) {
        budgetService.deleteBudget(user.id());
        return ResponseEntity.noContent().build();
    }
}
