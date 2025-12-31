-- V4__token_blacklist.sql
-- Create token_blacklist table for JWT logout functionality

CREATE TABLE token_blacklist (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    token_hash VARCHAR(64) NOT NULL UNIQUE,
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
    user_id UUID,
    reason VARCHAR(50),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_token_blacklist_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);

-- Index for fast token lookup during authentication
CREATE INDEX idx_token_blacklist_token_hash ON token_blacklist(token_hash);

-- Index for cleanup of expired tokens
CREATE INDEX idx_token_blacklist_expires_at ON token_blacklist(expires_at);

-- Comment on table
COMMENT ON TABLE token_blacklist IS 'Stores invalidated JWT tokens for logout functionality';
COMMENT ON COLUMN token_blacklist.token_hash IS 'SHA-256 hash of the invalidated token';
COMMENT ON COLUMN token_blacklist.expires_at IS 'When the token expires - used for cleanup';
COMMENT ON COLUMN token_blacklist.reason IS 'Reason for blacklisting: logout, password_change, security_revoke';
