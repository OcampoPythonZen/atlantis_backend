package com.atlantis.nutritionist.service;

import com.atlantis.nutritionist.entity.TokenBlacklist;
import com.atlantis.nutritionist.repository.TokenBlacklistRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.HexFormat;
import java.util.UUID;

/**
 * Service for managing JWT token blacklist.
 * Handles token invalidation for logout and session revocation.
 */
@Service
@Transactional
public class TokenBlacklistService {

    private static final Logger log = LoggerFactory.getLogger(TokenBlacklistService.class);

    private final TokenBlacklistRepository tokenBlacklistRepository;

    public TokenBlacklistService(TokenBlacklistRepository tokenBlacklistRepository) {
        this.tokenBlacklistRepository = tokenBlacklistRepository;
    }

    /**
     * Add a token to the blacklist (invalidate it).
     *
     * @param token The JWT token to blacklist
     * @param expiresAt When the token expires (for cleanup)
     * @param userId The user ID associated with the token (optional)
     * @param reason The reason for blacklisting
     */
    public void blacklistToken(String token, Instant expiresAt, UUID userId, String reason) {
        String tokenHash = hashToken(token);

        // Check if already blacklisted
        if (tokenBlacklistRepository.existsByTokenHash(tokenHash)) {
            log.debug("Token already blacklisted");
            return;
        }

        TokenBlacklist blacklistEntry = new TokenBlacklist(tokenHash, expiresAt, userId, reason);
        tokenBlacklistRepository.save(blacklistEntry);

        log.info("Token blacklisted for user: {} reason: {}", userId, reason);
    }

    /**
     * Check if a token is blacklisted.
     *
     * @param token The JWT token to check
     * @return true if the token is blacklisted
     */
    @Transactional(readOnly = true)
    public boolean isBlacklisted(String token) {
        String tokenHash = hashToken(token);
        return tokenBlacklistRepository.existsByTokenHash(tokenHash);
    }

    /**
     * Revoke all tokens for a user.
     * Note: This only removes existing blacklist entries.
     * For full revocation, you'd need to track all issued tokens.
     *
     * @param userId The user ID
     * @return Number of removed entries
     */
    public int revokeAllUserTokens(UUID userId) {
        int deleted = tokenBlacklistRepository.deleteByUserId(userId);
        log.info("Removed {} blacklist entries for user: {}", deleted, userId);
        return deleted;
    }

    /**
     * Clean up expired tokens from the blacklist.
     * Tokens that have expired don't need to be tracked anymore.
     *
     * @return Number of removed entries
     */
    public int cleanupExpiredTokens() {
        Instant now = Instant.now();
        long expiredCount = tokenBlacklistRepository.countExpiredTokens(now);

        if (expiredCount > 0) {
            int deleted = tokenBlacklistRepository.deleteExpiredTokens(now);
            log.info("Cleaned up {} expired tokens from blacklist", deleted);
            return deleted;
        }

        return 0;
    }

    /**
     * Scheduled task to clean up expired tokens.
     * Runs every hour.
     */
    @Scheduled(fixedRate = 3600000) // Every hour
    public void scheduledCleanup() {
        try {
            cleanupExpiredTokens();
        } catch (Exception e) {
            log.error("Error during scheduled token blacklist cleanup", e);
        }
    }

    /**
     * Hash a token using SHA-256.
     * We store hashes instead of raw tokens for security.
     *
     * @param token The token to hash
     * @return The SHA-256 hash as a hex string
     */
    private String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            // SHA-256 is always available in Java
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }
}
