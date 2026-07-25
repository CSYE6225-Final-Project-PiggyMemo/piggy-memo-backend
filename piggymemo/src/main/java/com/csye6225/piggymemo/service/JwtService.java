package com.csye6225.piggymemo.service;

import com.csye6225.piggymemo.entity.JwtPayload;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {
    public static final String AUTHORITIES_CLAIM = "authorities";

    private final SecretKey key;
    private final long expirationMs;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expiration
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expiration;
    }

    public String generateToken(UserDetails userDetails) {
        Instant now = Instant.now();
        List<String> authorities = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts
                .builder()
                .subject(userDetails.getUsername())
                .claim(AUTHORITIES_CLAIM, authorities)
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
        if (claims.get(AUTHORITIES_CLAIM) == null) {
            return new JwtPayload(claims.getSubject(), Collections.emptyList());
        } else {
            @SuppressWarnings("unchecked")
            List<String> authorities = (List<String>) claims.get(AUTHORITIES_CLAIM, List.class);
            return new JwtPayload(claims.getSubject(), authorities);
        }
    }
}
