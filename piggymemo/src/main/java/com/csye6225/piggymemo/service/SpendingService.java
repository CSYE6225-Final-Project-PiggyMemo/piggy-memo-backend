package com.csye6225.piggymemo.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.csye6225.piggymemo.dto.SetTransactionRequest;
import com.csye6225.piggymemo.dto.TransactionResponse;
import com.csye6225.piggymemo.entity.Budgets;
import com.csye6225.piggymemo.entity.Spendings;
import com.csye6225.piggymemo.repository.SpendingsRepository;

//Spending and saving are recorded together. Positive for spending, negative for saving
@Service
public class SpendingService {
    private final SpendingsRepository spendingRepository;
    private final BudgetService budgetService;

    public SpendingService(
        SpendingsRepository spendingRepository,
        BudgetService budgetService
    ){
        this.spendingRepository = spendingRepository;
        this.budgetService = budgetService;
    }

    @Transactional
    public TransactionResponse newSpending(Long user, SetTransactionRequest req) {
        Spendings spending = new Spendings();

        spending.setUserId(user);
        spending.setAmount(req.getTransactionAmount());
        String category = req.getCategory().getRecord();
        spending.setCategory(category);

        if(req.getNotes() != null)
            spending.setNotes(req.getNotes());

        BigDecimal budgetLeft = budgetService.subtractBudgetLeft(user, req.getTransactionAmount()).budgetLeft();
        Spendings save = spendingRepository.save(spending);

        return new TransactionResponse(save.getAmount(), budgetLeft, save.getCategory(), save.getNotes(), save.getCreatedAt());
    }
}
