package com.csye6225.piggymemo.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "personal_budgets")
public class PersonalBudgets implements Budgets {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Generated(event = EventType.INSERT)
    @Column(name = "created_at", insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long user;

    @Column(name = "monthly_budget", precision = 11, scale = 2)
    private BigDecimal monthlyBudget;

    @Column(name = "daily_limit", precision = 11, scale = 2)
    private BigDecimal dailyLimit;

    @NotNull
    @Column(name = "period_first_day", nullable = false)
    private LocalDate periodFirstDay;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    @Override
    public BigDecimal getMonthlyBudget() {
        return monthlyBudget;
    }

    public void setMonthlyBudget(BigDecimal monthlyBudget) {
        this.monthlyBudget = monthlyBudget;
    }

    @Override
    public BigDecimal getDailyLimit() {
        return dailyLimit;
    }

    public void setDailyLimit(BigDecimal dailyLimit) {
        this.dailyLimit = dailyLimit;
    }

    @Override
    public LocalDate getPeriodFirstDay() {
        return periodFirstDay;
    }

    public void setPeriodFirstDay(LocalDate periodFirstDay) {
        this.periodFirstDay = periodFirstDay;
    }
    
}