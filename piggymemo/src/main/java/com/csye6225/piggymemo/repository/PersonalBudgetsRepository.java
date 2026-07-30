package com.csye6225.piggymemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.csye6225.piggymemo.entity.PersonalBudgets;

public interface PersonalBudgetsRepository extends JpaRepository<PersonalBudgets, Long> {
    
}
