package com.csye6225.piggymemo.service;

import org.springframework.stereotype.Service;

import com.csye6225.piggymemo.dto.SetSpendingRequest;
import com.csye6225.piggymemo.repository.SpendingsRepository;

@Service
public class SpendingService {
    private final SpendingsRepository spendingRepository;

    public SpendingService(
        SpendingsRepository spendingRepository
    ){
        this.spendingRepository = spendingRepository;
    }

    public void newSpending(Long user, SetSpendingRequest req) {

    }
}
