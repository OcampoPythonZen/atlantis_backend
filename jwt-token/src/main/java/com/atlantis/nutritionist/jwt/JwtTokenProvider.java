package com.atlantis.nutritionist.jwt;

import com.atlantis.nutritionist.jwt.model.JwtToken;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class JwtTokenProvider {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);
    private static final String TOKEN_TYPE = "Bearer";

    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;

    public JwtTokenProvider(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
        this.secretKey = Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    public JwtToken generateToken(String userId, String email, List<String> roles) {
        return generateToken(userId, email, roles, Map.of());
    }

    public JwtToken generateToken(String userId, String email, List<String> roles, Map<String, Object> additionalClaims) {
        Instant now = Instant.now();
        Instant accessTokenExpiry = now.plusMillis(jwtConfig.getAccessTokenExpiration());
        Instant refreshTokenExpiry = now.plusMillis(jwtConfig.getRefreshTokenExpiration());

        String accessToken = createToken(userId, email, roles, additionalClaims, accessTokenExpiry);
        String refreshToken = createRefreshToken(userId, refreshTokenExpiry);

        log.debug("Generated JWT token for user: {}", userId);

        return new JwtToken(accessToken, refreshToken, accessTokenExpiry, TOKEN_TYPE);
    }

    private String createToken(String userId, String email, List<String> roles,
                               Map<String, Object> additionalClaims, Instant expiry) {
        return Jwts.builder()
                .setSubject(userId)
                .claim("email", email)
                .claim("roles", roles)
                .addClaims(additionalClaims)
                .setIssuer(jwtConfig.getIssuer())
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(expiry))
                .setId(UUID.randomUUID().toString())
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    private String createRefreshToken(String userId, Instant expiry) {
        return Jwts.builder()
                .setSubject(userId)
                .claim("type", "refresh")
                .setIssuer(jwtConfig.getIssuer())
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(expiry))
                .setId(UUID.randomUUID().toString())
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public JwtToken refreshToken(String refreshToken, String email, List<String> roles) {
        Instant now = Instant.now();
        Instant accessTokenExpiry = now.plusMillis(jwtConfig.getAccessTokenExpiration());
        Instant refreshTokenExpiry = now.plusMillis(jwtConfig.getRefreshTokenExpiration());

        String userId = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(refreshToken)
                .getPayload()
                .getSubject();

        String newAccessToken = createToken(userId, email, roles, Map.of(), accessTokenExpiry);
        String newRefreshToken = createRefreshToken(userId, refreshTokenExpiry);

        log.debug("Refreshed JWT token for user: {}", userId);

        return new JwtToken(newAccessToken, newRefreshToken, accessTokenExpiry, TOKEN_TYPE);
    }
}
