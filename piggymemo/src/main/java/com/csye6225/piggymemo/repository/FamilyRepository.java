package com.csye6225.piggymemo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.csye6225.piggymemo.entity.Family;

public interface FamilyRepository extends JpaRepository<Family, Long> {
    Optional<Family> findByFamilyCode(String familyCode);
    boolean existsByFamilyCode(String familyCode);
}
