package com.csye6225.piggymemo.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.csye6225.piggymemo.dto.PagedTransactionResponse;
import com.csye6225.piggymemo.dto.SetTransactionRequest;
import com.csye6225.piggymemo.dto.TransactionResponse;
import com.csye6225.piggymemo.entity.Spendings;
import com.csye6225.piggymemo.exception.BudgetNotExistException;
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

        try {
            BigDecimal budgetLeft = budgetService.subtractBudgetLeft(user, req.getTransactionAmount()).budgetLeft();
            spending.setBudgetLeftNow(budgetLeft);
        }
        catch(BudgetNotExistException e) {
            spending.setBudgetLeftNow(null);
        }
        Spendings save = spendingRepository.save(spending);

        return new TransactionResponse(save.getAmount(), save.getBudgetLeftNow(), save.getCategory(), save.getNotes(), save.getCreatedAt());
    }

    public PagedTransactionResponse getTransactionRecord(Long user, Integer size, Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, size);

        Page<Spendings> page = spendingRepository.findByUserIdOrderByCreatedAtDesc(user, pageable);
        List<TransactionResponse> records = page.stream().map(
            sp -> new TransactionResponse(
                sp.getAmount(), sp.getBudgetLeftNow(), sp.getCategory(), sp.getNotes(), sp.getCreatedAt()
            )
        ).toList();

        return new PagedTransactionResponse(records, page.getNumber(), page.getTotalPages(), page.getTotalElements());
    }
}
