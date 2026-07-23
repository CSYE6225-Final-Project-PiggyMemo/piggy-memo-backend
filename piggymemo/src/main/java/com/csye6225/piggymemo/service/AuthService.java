package com.csye6225.piggymemo.service;

import java.time.Duration;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.csye6225.piggymemo.dto.LoginRequest;
import com.csye6225.piggymemo.entity.User;
import com.csye6225.piggymemo.repository.UserRepository;

public class AuthService {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(JwtService jwtService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<Map<String, String>> login(LoginRequest req) {
        String username = req.getUsername();
        String password = req.getPassword();
        
        String token = validateAndGetToken(username, password);
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

    private String validateAndGetToken(String username, String password) {
        User user = userRepository.findByUsername(username).orElseThrow(() ->
            new BadCredentialsException("Wrong username or password, please check your information."));
        
        if(!(passwordEncoder.matches(password, user.getPassword()))) {
            throw new BadCredentialsException("Wrong username or password, please check your information.");
        }

        return jwtService.generateToken(username);
    }
}
