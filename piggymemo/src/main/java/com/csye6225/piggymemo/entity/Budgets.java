package com.csye6225.piggymemo.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

public interface Budgets {
    public Long getId();
    public void setId(Long id);

    public OffsetDateTime getCreatedAt();
    public void setCreatedAt(OffsetDateTime createdAt);

    public Long getUser();
    public void setUser(Long user);

    public BigDecimal getMonthlyBudget();
    public void setMonthlyBudget(BigDecimal monthlyBudget);

    public BigDecimal getDailyLimit();
    public void setDailyLimit(BigDecimal dailyLimit);
    
    public LocalDate getPeriodFirstDay();
    public void setPeriodFirstDay(LocalDate periodFirstDay);
}
