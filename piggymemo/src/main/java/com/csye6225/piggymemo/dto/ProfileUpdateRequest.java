package com.csye6225.piggymemo.dto;

public record ProfileUpdateRequest(
    String avatarUrl,
    String nickname,
    String bio,
    Boolean isProfilePublic,
    Long family
) {
}
