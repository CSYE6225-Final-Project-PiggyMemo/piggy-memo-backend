package com.csye6225.piggymemo.entity;

import java.math.BigDecimal;
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
@Table(name = "spendings")
public class Spendings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nullable: survives the owning user's account deletion (ON DELETE SET NULL),
    // rather than the row being cascade-deleted, so family/history data isn't lost.
    @Column(name = "user_id")
    private Long userId;

    @NotNull
    @Column(nullable = false, precision = 11, scale = 2)
    private BigDecimal amount;

    @Column(name = "budget_left_now", precision = 11, scale = 2)
    private BigDecimal budgetLeftNow;

    @Column
    private String category;

    @Column
    private String notes;

    @Column(name = "family_id")
    private Long familyId;

    @Generated(event = EventType.INSERT)
    @Column(name = "created_at", insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    public Spendings() {

    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getBudgetLeftNow() {
        return budgetLeftNow;
    }

    public void setBudgetLeftNow(BigDecimal budgetLeftNow) {
        this.budgetLeftNow = budgetLeftNow;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Long getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Long familyId) {
        this.familyId = familyId;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}
