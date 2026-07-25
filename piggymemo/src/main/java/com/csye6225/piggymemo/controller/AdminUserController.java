package com.csye6225.piggymemo.controller;

import com.csye6225.piggymemo.dto.UserResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {
    @GetMapping
    public List<UserResponse> listUsers() {
        return Collections.emptyList();
    }
}
