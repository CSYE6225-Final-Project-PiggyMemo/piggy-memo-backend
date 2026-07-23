package com.csye6225.piggymemo.controller;

import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/me")
public class TestController {
    @GetMapping()
    public Map<String, String> getMethodName(Authentication auth) {
        String name = auth.getName();
        return Map.of("username", name);
    }
}
