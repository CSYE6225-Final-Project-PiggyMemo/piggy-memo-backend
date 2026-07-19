package com.csye6225.piggymemo.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.csye6225.piggymemo.dto.CreateUserRequest;
import com.csye6225.piggymemo.dto.UserResponse;
import com.csye6225.piggymemo.entity.User;
import com.csye6225.piggymemo.exception.UsernameAlreadyExistsException;
import com.csye6225.piggymemo.repository.UserRepository;

@Service
public class UserService {
    public final UserRepository userRepository;
    public final PasswordEncoder passwordEncoder;
    
    public UserService(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder
    ){
            this.userRepository = userRepository;
            this.passwordEncoder = passwordEncoder;
    }

    public UserResponse createUser(CreateUserRequest req) {
        if(userRepository.existsByUsername(req.getUsername()))
            throw new UsernameAlreadyExistsException("Username " + req.getUsername() + " already exists!");

        User user = new User();
        user.setUsername(req.getUsername());
        user.setPassword(passwordEncoder.encode(req.getPassword()));

        User save = userRepository.save(user);

        return toResponse(save);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public UserResponse toResponse(User user) {
        UserResponse response = new UserResponse();

        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setCreatedAt(user.getCreatedAt());

        return response;
    }
}
