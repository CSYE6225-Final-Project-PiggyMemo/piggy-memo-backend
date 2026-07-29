package com.csye6225.piggymemo.repository;

import java.time.OffsetDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

import com.csye6225.piggymemo.entity.TokenBlacklist;

public interface TokenBlacklistRepository extends JpaRepository<TokenBlacklist, Long> {
    boolean existsByJti(String jti);
    void deleteAllByExpiresAtBefore(OffsetDateTime before);
}
