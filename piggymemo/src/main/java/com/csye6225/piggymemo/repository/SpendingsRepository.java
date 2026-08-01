package com.csye6225.piggymemo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.csye6225.piggymemo.entity.Spendings;

public interface SpendingsRepository extends JpaRepository<Spendings, Long> {
    Page<Spendings> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
}
