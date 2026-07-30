package com.csye6225.piggymemo.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csye6225.piggymemo.security.CurrentUser;

import java.util.Map;

@RestController
@RequestMapping("/api/me")
public class TestController {
    @GetMapping()
    public Map<String, String> getMethodName(@AuthenticationPrincipal CurrentUser currentUser) {
        String name = currentUser.username();
        return Map.of("username", name);
    }
}
