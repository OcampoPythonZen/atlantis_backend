package com.atlantis.nutritionist.util;

import io.grpc.Metadata;

import java.util.Optional;

/**
 * Utility class for extracting data from gRPC metadata (headers).
 * Provides convenient methods for common metadata operations.
 *
 * Usage:
 * <pre>
 * Metadata metadata = ... // from interceptor
 * Optional<String> token = MetadataUtils.extractBearerToken(metadata);
 * </pre>
 */
public final class MetadataUtils {

    // Metadata keys
    private static final Metadata.Key<String> AUTHORIZATION_KEY =
            Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER);

    private static final String BEARER_PREFIX = "Bearer ";

    // Private constructor to prevent instantiation
    private MetadataUtils() {
        throw new AssertionError("Utility class should not be instantiated");
    }

    /**
     * Extracts the JWT token from the "Authorization: Bearer <token>" header.
     *
     * @param metadata The gRPC metadata
     * @return Optional containing the token if present and properly formatted
     */
    public static Optional<String> extractBearerToken(Metadata metadata) {
        if (metadata == null) {
            return Optional.empty();
        }

        String authHeader = metadata.get(AUTHORIZATION_KEY);
        if (authHeader == null || authHeader.isBlank()) {
            return Optional.empty();
        }

        // Check if it starts with "Bearer "
        if (authHeader.startsWith(BEARER_PREFIX)) {
            String token = authHeader.substring(BEARER_PREFIX.length()).trim();
            return token.isEmpty() ? Optional.empty() : Optional.of(token);
        }

        return Optional.empty();
    }

    /**
     * Gets the value of a metadata header by key.
     *
     * @param metadata The gRPC metadata
     * @param key      The metadata key
     * @return Optional containing the value if present
     */
    public static Optional<String> getMetadataValue(Metadata metadata, String key) {
        if (metadata == null || key == null) {
            return Optional.empty();
        }

        Metadata.Key<String> metadataKey = Metadata.Key.of(key, Metadata.ASCII_STRING_MARSHALLER);
        String value = metadata.get(metadataKey);
        return Optional.ofNullable(value);
    }

    /**
     * Checks if the authorization header is present.
     *
     * @param metadata The gRPC metadata
     * @return true if authorization header exists
     */
    public static boolean hasAuthorizationHeader(Metadata metadata) {
        if (metadata == null) {
            return false;
        }
        String authHeader = metadata.get(AUTHORIZATION_KEY);
        return authHeader != null && !authHeader.isBlank();
    }
}
