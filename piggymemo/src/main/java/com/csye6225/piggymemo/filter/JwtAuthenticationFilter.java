package com.csye6225.piggymemo.filter;

import com.csye6225.piggymemo.entity.CurrentUser;
import com.csye6225.piggymemo.entity.JwtPayload;
import com.csye6225.piggymemo.service.JwtService;
import com.csye6225.piggymemo.service.TokenBlacklistService;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final TokenBlacklistService blacklistService;

    public JwtAuthenticationFilter(JwtService jwtService, TokenBlacklistService blacklistService) {
        this.jwtService = jwtService;
        this.blacklistService = blacklistService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        String token = extractToken(request);

        if (token != null) {
            try {
                JwtPayload payload = jwtService.validateAndGetPayload(token);

                //Block revoked token
                if(!blacklistService.isRevoked(payload.jti())) {
                    List<SimpleGrantedAuthority> authorities = payload.authorities()
                            .stream()
                            .map(SimpleGrantedAuthority::new)
                            .toList();
                    CurrentUser principal = new CurrentUser(payload.userId(), payload.username());
                    var auth = new UsernamePasswordAuthenticationToken(principal, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (JwtException e) {
                //Do nothing, let filter chain handle that
            }
        }
        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest req) {
        if (req.getCookies() == null) return null;

        for (Cookie c : req.getCookies()) {
            if ("token".equals(c.getName())) {
                return c.getValue();
            }
        }

        return null;
    }
}
