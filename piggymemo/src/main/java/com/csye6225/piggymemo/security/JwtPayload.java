package com.csye6225.piggymemo.security;

import java.time.OffsetDateTime;
import java.util.List;

public record JwtPayload(Long userId, String username, List<String> authorities, String jti, OffsetDateTime expiresAt) {
}
