package com.csye6225.piggymemo.service;

import com.csye6225.piggymemo.dto.LoginRequest;
import com.csye6225.piggymemo.entity.User;
import com.csye6225.piggymemo.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    public static final String WRONG_MESSAGE = "Wrong username or password, please check your information.";
    private final JwtService jwtService;
    private final PiggymemoUserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public AuthService(JwtService jwtService, PiggymemoUserDetailsManager userDetailsManager, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userDetailsManager = userDetailsManager;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public String loginAndGetToken(LoginRequest req) {
        String username = req.getUsername();
        String password = req.getPassword();
        return validateAndGetToken(username, password);
    }

    private String validateAndGetToken(String username, String password) {
        UserDetails userDetails = getUserDetails(username);
        if (!(passwordEncoder.matches(password, userDetails.getPassword()))) {
            throw new BadCredentialsException(WRONG_MESSAGE);
        }
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new BadCredentialsException(WRONG_MESSAGE));
        return jwtService.generateToken(user, userDetails);
    }

    private UserDetails getUserDetails(String username) {
        try {
            return userDetailsManager.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            throw new BadCredentialsException(WRONG_MESSAGE);
        }
    }
}
