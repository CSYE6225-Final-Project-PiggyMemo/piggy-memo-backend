package com.csye6225.piggymemo.dto;

import java.time.OffsetDateTime;

public record FamilyResponse(Long id, String familyCode, String familyName, Long ownerUserId, OffsetDateTime createdAt) {
}
