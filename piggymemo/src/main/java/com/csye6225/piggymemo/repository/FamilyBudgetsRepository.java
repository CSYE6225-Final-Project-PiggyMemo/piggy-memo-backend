package com.csye6225.piggymemo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.csye6225.piggymemo.entity.FamilyBudgets;

public interface FamilyBudgetsRepository extends JpaRepository<FamilyBudgets, Long> {
    Optional<FamilyBudgets> findByFamily(Long family);
    void deleteByFamily(Long family);
}
