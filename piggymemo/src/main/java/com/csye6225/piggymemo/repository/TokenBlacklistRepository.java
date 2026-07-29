package com.csye6225.piggymemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.csye6225.piggymemo.entity.TokenBlacklist;

public interface TokenBlacklistRepository extends JpaRepository<TokenBlacklist, Long> {
}
