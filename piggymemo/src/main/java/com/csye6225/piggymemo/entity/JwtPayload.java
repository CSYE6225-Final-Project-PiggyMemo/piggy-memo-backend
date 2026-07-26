package com.csye6225.piggymemo.entity;

import java.util.List;

public record JwtPayload(String username, List<String> authorities) {
}