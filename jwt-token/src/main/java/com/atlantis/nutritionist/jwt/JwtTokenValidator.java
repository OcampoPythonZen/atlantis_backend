package com.atlantis.nutritionist.jwt;

import com.atlantis.nutritionist.jwt.exception.ExpiredTokenException;
import com.atlantis.nutritionist.jwt.exception.InvalidTokenException;
import com.atlantis.nutritionist.jwt.model.TokenClaims;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JwtTokenValidator {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenValidator.class);

    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;

    public JwtTokenValidator(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
        this.secretKey = Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    public TokenClaims validateAndParse(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return extractClaims(claims);
        } catch (ExpiredJwtException e) {
            log.warn("Token has expired: {}", e.getMessage());
            throw new ExpiredTokenException("Token has expired", e);
        } catch (JwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            throw new InvalidTokenException("Invalid token", e);
        } catch (Exception e) {
            log.error("Error parsing JWT token: {}", e.getMessage());
            throw new InvalidTokenException("Error parsing token", e);
        }
    }

    public boolean isValid(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            log.debug("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    public String extractUserId(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return claims.getSubject();
        } catch (Exception e) {
            log.error("Error extracting user ID from token: {}", e.getMessage());
            throw new InvalidTokenException("Could not extract user ID", e);
        }
    }

    public String extractEmail(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return claims.get("email", String.class);
        } catch (Exception e) {
            log.error("Error extracting email from token: {}", e.getMessage());
            throw new InvalidTokenException("Could not extract email", e);
        }
    }

    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return (List<String>) claims.get("roles");
        } catch (Exception e) {
            log.error("Error extracting roles from token: {}", e.getMessage());
            throw new InvalidTokenException("Could not extract roles", e);
        }
    }

    private TokenClaims extractClaims(Claims claims) {
        String subject = claims.getSubject();
        String email = claims.get("email", String.class);

        @SuppressWarnings("unchecked")
        List<String> roles = (List<String>) claims.get("roles");

        Instant issuedAt = claims.getIssuedAt().toInstant();
        Instant expiresAt = claims.getExpiration().toInstant();
        String issuer = claims.getIssuer();

        Map<String, Object> additionalClaims = new HashMap<>(claims);
        additionalClaims.remove("sub");
        additionalClaims.remove("email");
        additionalClaims.remove("roles");
        additionalClaims.remove("iat");
        additionalClaims.remove("exp");
        additionalClaims.remove("iss");
        additionalClaims.remove("jti");

        return new TokenClaims(subject, email, roles, issuedAt, expiresAt, issuer, additionalClaims);
    }
}
