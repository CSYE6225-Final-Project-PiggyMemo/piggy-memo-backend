package com.csye6225.piggymemo.dto;

import jakarta.validation.constraints.Size;

public class CreateFamilyRequest {
    @Size(max = 100)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
