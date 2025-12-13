package com.atlantis.nutritionist.jwt.model;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public record TokenClaims(
        String subject,
        String email,
        List<String> roles,
        Instant issuedAt,
        Instant expiresAt,
        String issuer,
        Map<String, Object> additionalClaims
) {
    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }
}
