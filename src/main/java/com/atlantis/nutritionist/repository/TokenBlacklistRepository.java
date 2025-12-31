package com.atlantis.nutritionist.repository;

import com.atlantis.nutritionist.entity.TokenBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.UUID;

/**
 * Repository interface for TokenBlacklist entity.
 * Provides database operations for token blacklist management.
 */
@Repository
public interface TokenBlacklistRepository extends JpaRepository<TokenBlacklist, UUID> {

    /**
     * Check if a token hash exists in the blacklist.
     * @param tokenHash The SHA-256 hash of the token
     * @return true if the token is blacklisted
     */
    boolean existsByTokenHash(String tokenHash);

    /**
     * Delete all expired tokens from the blacklist.
     * Used for periodic cleanup.
     * @param now The current timestamp
     * @return Number of deleted records
     */
    @Modifying
    @Query("DELETE FROM TokenBlacklist t WHERE t.expiresAt < :now")
    int deleteExpiredTokens(@Param("now") Instant now);

    /**
     * Count expired tokens (for monitoring/logging before cleanup).
     * @param now The current timestamp
     * @return Number of expired tokens
     */
    @Query("SELECT COUNT(t) FROM TokenBlacklist t WHERE t.expiresAt < :now")
    long countExpiredTokens(@Param("now") Instant now);

    /**
     * Delete all tokens for a specific user.
     * Used when revoking all sessions for a user.
     * @param userId The user's UUID
     * @return Number of deleted records
     */
    @Modifying
    @Query("DELETE FROM TokenBlacklist t WHERE t.userId = :userId")
    int deleteByUserId(@Param("userId") UUID userId);
}
