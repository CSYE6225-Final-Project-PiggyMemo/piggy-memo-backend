package com.csye6225.piggymemo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class JoinFamilyRequest {
    @NotBlank(message = "Family code cannot be empty.")
    @Size(min = 6, max = 16, message = "Family code must be within 6-16 characters.")
    private String familyCode;

    public JoinFamilyRequest() {}

    public String getFamilyCode() {
        return familyCode;
    }

    public void setFamilyCode(String familyCode) {
        this.familyCode = familyCode;
    }
}
