package com.csye6225.piggymemo.service;

import org.springframework.stereotype.Service;

import com.csye6225.piggymemo.repository.UserRepository;

@Service
public class UserService {
    public final UserRepository userRepository;
    
    public UserService(
        UserRepository userRepository){
            this.userRepository = userRepository;
    }
}
