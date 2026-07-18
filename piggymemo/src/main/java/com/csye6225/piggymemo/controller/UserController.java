package com.csye6225.piggymemo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csye6225.piggymemo.dto.CreateUserRequest;
import com.csye6225.piggymemo.dto.UserResponse;
import com.csye6225.piggymemo.service.UserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserResponse create(@Valid @RequestBody CreateUserRequest req) {
        return userService.createUser(req);
    }
}
