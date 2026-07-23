package com.csye6225.piggymemo.service;

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

    public String loginAndGetToken(LoginRequest req) {
        String username = req.getUsername();
        String password = req.getPassword();
        
        String token = validateAndGetToken(username, password);

        return token;
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
