package com.csye6225.piggymemo.entity;

import java.time.OffsetDateTime;

public record CurrentUser(Long id, String username, String jti, OffsetDateTime expiresAt) {
}
