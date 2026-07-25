package com.csye6225.piggymemo.service;

import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.csye6225.piggymemo.dto.LoginRequest;
import com.csye6225.piggymemo.entity.User;
import com.csye6225.piggymemo.repository.UserRepository;

@Service
public class AuthService {
    private final JwtService jwtService;
    private final PiggymemoUserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;

    public AuthService(JwtService jwtService, PiggymemoUserDetailsManager userDetailsManager, PasswordEncoder passwordEncoder) {
        this.jwtService = jwtService;
        this.userDetailsManager = userDetailsManager;
        this.passwordEncoder = passwordEncoder;
    }

    public String loginAndGetToken(LoginRequest req) {
        String username = req.getUsername();
        String password = req.getPassword();
        return validateAndGetToken(username, password);
    }

    private String validateAndGetToken(String username, String password) {
        UserDetails userDetails = getUserDetails(username);
        if (!(passwordEncoder.matches(password, userDetails.getPassword()))) {
            throw new BadCredentialsException("Wrong username or password, please check your information.");
        }
        return jwtService.generateToken(userDetails);
    }

    private UserDetails getUserDetails(String username) {
        try {
            return userDetailsManager.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            throw new BadCredentialsException("Wrong username or password, please check your information.");
        }
    }
}
