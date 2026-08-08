package com.csye6225.piggymemo.dto;

import java.time.OffsetDateTime;

public record FamilyJoinRequestResponse(Long requestId, Long userId, String nickname, OffsetDateTime requestedAt) {
}
