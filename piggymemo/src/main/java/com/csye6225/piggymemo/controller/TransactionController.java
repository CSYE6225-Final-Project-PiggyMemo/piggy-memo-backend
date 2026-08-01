package com.csye6225.piggymemo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.csye6225.piggymemo.dto.PagedTransactionResponse;
import com.csye6225.piggymemo.dto.SetTransactionRequest;
import com.csye6225.piggymemo.dto.TransactionResponse;
import com.csye6225.piggymemo.security.CurrentUser;
import com.csye6225.piggymemo.service.SpendingService;

import jakarta.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final SpendingService spendingService;

    public TransactionController(
        SpendingService spendingService
    ){
        this.spendingService = spendingService;
    }

    @PostMapping("/new")
    public TransactionResponse newTransaction(
        @AuthenticationPrincipal CurrentUser user,
        @Valid @RequestBody SetTransactionRequest req
    ) {
        return spendingService.newSpending(user.id(), req);
    }
    
    @GetMapping("/get")
    public PagedTransactionResponse getTransRecord(
        @AuthenticationPrincipal CurrentUser user,
        @RequestParam Integer size,
        @RequestParam Integer pageNumber
    ) {
        return spendingService.getTransactionRecord(user.id(), size, pageNumber);
    }
}
