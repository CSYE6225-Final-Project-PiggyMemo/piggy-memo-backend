package com.csye6225.piggymemo.dto;

import java.math.BigDecimal;

/**
 * Data for budget execution progress bar
 */
public record BudgetExecution(BigDecimal monthlyBudget, BigDecimal budgetLeft) {
}
