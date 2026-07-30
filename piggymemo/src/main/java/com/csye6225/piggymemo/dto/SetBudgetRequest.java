package com.csye6225.piggymemo.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.PositiveOrZero;

public class SetBudgetRequest {
    @Digits(integer = 9, fraction = 2, message = "Invalid amount for monthly budget")
    @PositiveOrZero(message = "Budget cannot be negative")
    private BigDecimal newMonthlyBudget;

    @Digits(integer = 9, fraction = 2, message = "Invalid amount for daily limit")
    private BigDecimal newDailyLimit;

    @FutureOrPresent(message = "First day of period cannot be earlier than today")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate newPeriodFirstDay;

    public SetBudgetRequest() {}

    public BigDecimal getNewMonthlyBudget() {
        return newMonthlyBudget;
    }

    public void setNewMonthlyBudget(BigDecimal newMonthlyBudget) {
        this.newMonthlyBudget = newMonthlyBudget;
    }

    public BigDecimal getNewDailyLimit() {
        return newDailyLimit;
    }

    public void setNewDailyLimit(BigDecimal newDailyLimit) {
        this.newDailyLimit = newDailyLimit;
    }

    public LocalDate getNewPeriodFirstDay() {
        return newPeriodFirstDay;
    }

    public void setNewPeriodFirstDay(LocalDate newPeriodFirstDay) {
        this.newPeriodFirstDay = newPeriodFirstDay;
    }
}
