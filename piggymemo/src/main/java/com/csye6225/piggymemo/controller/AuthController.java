package com.csye6225.piggymemo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csye6225.piggymemo.dto.LoginRequest;
import com.csye6225.piggymemo.service.AuthService;

import java.time.Duration;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
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
    
}
