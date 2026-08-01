package com.csye6225.piggymemo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csye6225.piggymemo.dto.LoginRequest;
import com.csye6225.piggymemo.security.CurrentUser;
import com.csye6225.piggymemo.service.AuthService;
import com.csye6225.piggymemo.service.TokenBlacklistService;

import java.time.Duration;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final TokenBlacklistService blacklistService;

    public AuthController(AuthService authService, TokenBlacklistService blacklistService) {
        this.authService = authService;
        this.blacklistService = blacklistService;
    }
    
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest req) {
        String token = authService.loginAndGetToken(req);

        //Generate cookie and encapsulate token
        ResponseCookie cookie = ResponseCookie.from("token", token)
            .httpOnly(true)
            .path("/")
            .sameSite("None")
            .secure(true)
            .maxAge(Duration.ofHours(1))
            .build();

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, cookie.toString())
            .body(Map.of("message", "Login success"));
    }
    
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(@AuthenticationPrincipal CurrentUser currentUser) {
        if(currentUser != null) {
            blacklistService.revoke(currentUser.jti(), currentUser.expiresAt());
        }

        ResponseCookie cookie = ResponseCookie.from("token", "")
            .httpOnly(true)
            .path("/")
            .sameSite("None")
            .secure(true)
            .maxAge(Duration.ofHours(0))
            .build();
        
        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, cookie.toString())
            .body(Map.of("message", "Successfully logged out"));
    }
    
}
