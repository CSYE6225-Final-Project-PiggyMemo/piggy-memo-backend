package com.csye6225.piggymemo.service;

import org.springframework.security.crypto.password.PasswordEncoder;

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
}
