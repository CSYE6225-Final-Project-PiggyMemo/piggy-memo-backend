package com.csye6225.piggymemo.service;

import com.csye6225.piggymemo.entity.User;
import com.csye6225.piggymemo.security.JwtPayload;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class JwtService {
    public static final String AUTHORITIES_CLAIM = "authorities";
    public static final String UID_CLAIM = "uid";

    private final SecretKey key;
    private final long expirationMs;

    public JwtService(
        @Value("${jwt.secret}") String secret,
        @Value("${jwt.expiration}") long expiration
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expiration;
    }

    public String generateToken(User user, UserDetails userDetails) {
        Instant now = Instant.now();
        List<String> authorities = userDetails.getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .toList();

        return Jwts
            .builder()
            .subject(userDetails.getUsername())
            .id(UUID.randomUUID().toString())
            .claim(AUTHORITIES_CLAIM, authorities)
            .claim(UID_CLAIM, user.getId())
            .issuedAt(Date.from(now))
            .expiration(Date.from(now.plusMillis(expirationMs)))
            .signWith(key)
            .compact();
    }

    public JwtPayload validateAndGetPayload(String token) {
        Claims claims = Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload();

        Long userId = claims.get(UID_CLAIM, Long.class);

        if (userId == null) {
            throw new JwtException("Missing uid claim");
        }

        String username = claims.getSubject();

        @SuppressWarnings("unchecked")
        List<String> authorities = claims.get(AUTHORITIES_CLAIM, List.class);
        if (authorities == null) {
            authorities = Collections.emptyList();
        }

        String jti = claims.getId();

        Date expiration = claims.getExpiration();
        OffsetDateTime expiresAt = expiration.toInstant().atOffset(ZoneOffset.UTC);

        return new JwtPayload(userId, username, authorities, jti, expiresAt);
    }
}
