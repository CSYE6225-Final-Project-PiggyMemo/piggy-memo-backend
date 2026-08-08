package com.csye6225.piggymemo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.csye6225.piggymemo.entity.FamilyJoinRequest;

public interface FamilyJoinRequestRepository extends JpaRepository<FamilyJoinRequest, Long> {
    List<FamilyJoinRequest> findByFamilyIdOrderByCreatedAtAsc(Long familyId);
    Optional<FamilyJoinRequest> findByIdAndFamilyId(Long id, Long familyId);
    Optional<FamilyJoinRequest> findByUserId(Long userId);
    void deleteByUserId(Long userId);
}
