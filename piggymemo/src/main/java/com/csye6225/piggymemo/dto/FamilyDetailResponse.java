package com.csye6225.piggymemo.dto;

import java.time.OffsetDateTime;
import java.util.List;

public record FamilyDetailResponse(
    Long id,
    String familyCode,
    String familyName,
    Long ownerUserId,
    OffsetDateTime createdAt,
    List<FamilyMemberResponse> members
) {
}
