package com.csye6225.piggymemo.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

public interface Budgets {
    public Long getId();
    public OffsetDateTime getCreatedAt();
    public Long getUser();
    public BigDecimal getMonthlyBudget();
    public BigDecimal getDailyLimit();
    public LocalDate getPeriodFirstDay();
}
