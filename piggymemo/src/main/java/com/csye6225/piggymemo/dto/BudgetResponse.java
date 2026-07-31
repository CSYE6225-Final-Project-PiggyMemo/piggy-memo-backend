package com.csye6225.piggymemo.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BudgetResponse(
    BigDecimal currentBudget,
    BigDecimal currentDailyLimit,
    LocalDate nextPeriodFirstDay,
    BigDecimal budgetLeft
) {} 
