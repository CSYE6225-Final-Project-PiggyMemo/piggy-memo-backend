package com.csye6225.piggymemo.service;

import com.csye6225.piggymemo.dto.CreateUserRequest;
import com.csye6225.piggymemo.dto.UserResponse;
import com.csye6225.piggymemo.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private static final String DEFAULT_AUTHORITY = "USER";

    private final PasswordEncoder passwordEncoder;
    private final PiggymemoUserDetailsManager userDetailsManager;

    public UserService(
        PasswordEncoder passwordEncoder, PiggymemoUserDetailsManager userDetailsManager
    ) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailsManager = userDetailsManager;
    }

    public UserResponse createUser(CreateUserRequest req) {
        UserDetails userDetails = org.springframework.security.core.userdetails.User
            .withUsername(req.getUsername())
            .password(passwordEncoder.encode(req.getPassword()))
            .authorities(DEFAULT_AUTHORITY)
            .build();
        return toResponse(userDetailsManager.createUserAndReturn(userDetails));
    }

    public boolean existsByUsername(String username) {
        return userDetailsManager.userExists(username);
    }

    private UserResponse toResponse(User user) {
        UserResponse response = new UserResponse();

        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setCreatedAt(user.getCreatedAt());

        return response;
    }
}
