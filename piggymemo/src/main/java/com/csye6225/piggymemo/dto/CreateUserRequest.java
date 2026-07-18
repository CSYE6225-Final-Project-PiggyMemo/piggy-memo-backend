package com.csye6225.piggymemo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CreateUserRequest {
    @NotBlank(message = "Username cannot be empty.")
    @Size(min = 3, max = 20, message = "Username must be within 3-20 characters.")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username can only includes letters, numbers and underline.")
    private String username;

    @NotBlank(message = "Password cannot be empty.")
    @Size(min = 8, max = 30, message = "Password must be within 8-30 characters.")
    @Pattern(
            regexp = "^(?:(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)|(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+=-])|(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+=-])|(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+=-]))[A-Za-z0-9!@#$%^&*()_+=-]+$",
            message = "Password must be the combination of 3 or more of the following characters: numbers, upper cases, lower cases and special characters."
    )
    private String password;

    public CreateUserRequest() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}
