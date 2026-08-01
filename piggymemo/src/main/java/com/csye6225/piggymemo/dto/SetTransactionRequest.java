package com.csye6225.piggymemo.dto;

import java.math.BigDecimal;

import com.csye6225.piggymemo.constant.SpendingCategory;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class SetTransactionRequest {
    //Positive means spending, negative means saving.
    //Such design is because spending is more common in our application usage
    @NotNull
    @Digits(integer = 9, fraction = 2, message = "Invalid amount for spending/saving")
    private BigDecimal transactionAmount;

    @NotNull
    private SpendingCategory category;

    @Size(max = 200, message = "Note must be within 200 characters")
    private String notes;

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public SpendingCategory getCategory() {
        return category;
    }

    public void setCategory(SpendingCategory category) {
        this.category = category;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
    
}
