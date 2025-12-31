package com.atlantis.nutritionist.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

/**
 * TokenBlacklist entity for storing invalidated JWT tokens.
 * Maps to the 'token_blacklist' table in the database.
 *
 * Used to implement logout functionality by storing tokens that
 * should no longer be accepted for authentication.
 */
@Entity
@Table(name = "token_blacklist", indexes = {
    @Index(name = "idx_token_blacklist_token_hash", columnList = "token_hash"),
    @Index(name = "idx_token_blacklist_expires_at", columnList = "expires_at")
})
public class TokenBlacklist {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * SHA-256 hash of the token (not the actual token for security).
     * Using hash instead of raw token to avoid storing sensitive data.
     */
    @Column(name = "token_hash", nullable = false, unique = true, length = 64)
    private String tokenHash;

    /**
     * When the token expires. Used for cleanup of old entries.
     */
    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    /**
     * User ID associated with the token (optional, for audit purposes).
     */
    @Column(name = "user_id")
    private UUID userId;

    /**
     * Reason for blacklisting (e.g., "logout", "password_change", "security_revoke").
     */
    @Column(name = "reason", length = 50)
    private String reason;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
    }

    // Constructors
    public TokenBlacklist() {
    }

    public TokenBlacklist(String tokenHash, Instant expiresAt, UUID userId, String reason) {
        this.tokenHash = tokenHash;
        this.expiresAt = expiresAt;
        this.userId = userId;
        this.reason = reason;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTokenHash() {
        return tokenHash;
    }

    public void setTokenHash(String tokenHash) {
        this.tokenHash = tokenHash;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
