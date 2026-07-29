package com.csye6225.piggymemo.service;

import java.time.OffsetDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.csye6225.piggymemo.entity.TokenBlacklist;
import com.csye6225.piggymemo.repository.TokenBlacklistRepository;

@Service
public class TokenBlacklistService {
    private final TokenBlacklistRepository tokenBlacklistRepository;

    public TokenBlacklistService(TokenBlacklistRepository tokenBlacklistRepository) {
        this.tokenBlacklistRepository = tokenBlacklistRepository;
    }

    public void revoke(String jti, OffsetDateTime expiresAt) {
        TokenBlacklist blacklist = new TokenBlacklist();
        blacklist.setJti(jti);
        blacklist.setExpiresAt(expiresAt);
        tokenBlacklistRepository.save(blacklist);
    }

    public boolean isRevoked(String jti) {
        return tokenBlacklistRepository.existsByJti(jti);
    }

    @Scheduled(cron = "0 0 * * * *")
    public void cleanExpired() {
        tokenBlacklistRepository.deleteAllByExpiresAtBefore(OffsetDateTime.now());
    }
}
