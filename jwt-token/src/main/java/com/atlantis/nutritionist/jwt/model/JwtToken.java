package com.atlantis.nutritionist.jwt.model;

import java.time.Instant;

public record JwtToken(
        String accessToken,
        String refreshToken,
        Instant expiresAt,
        String tokenType
) {
    public long getExpiresIn() {
        return expiresAt.getEpochSecond() - Instant.now().getEpochSecond();
    }
}
