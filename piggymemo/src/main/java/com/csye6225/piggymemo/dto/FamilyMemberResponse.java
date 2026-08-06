package com.csye6225.piggymemo.dto;

public record FamilyMemberResponse(Long userId, String nickname, String avatarUrl, boolean isOwner) {
}
