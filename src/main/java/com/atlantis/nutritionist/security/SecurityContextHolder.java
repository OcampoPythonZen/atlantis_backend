package com.atlantis.nutritionist.security;

import com.atlantis.nutritionist.exception.AuthenticationException;
import io.grpc.Context;

import java.util.Optional;

/**
 * Utility class for accessing the authenticated user's context from gRPC Context.
 * This class provides thread-safe access to authentication information for the current request.
 *
 * Usage in gRPC service methods:
 * <pre>
 * // Optional access (returns empty if not authenticated)
 * Optional<AuthenticationContext> auth = SecurityContextHolder.getAuthenticationContext();
 * if (auth.isPresent()) {
 *     String userId = auth.get().userId();
 * }
 *
 * // Required access (throws if not authenticated)
 * AuthenticationContext auth = SecurityContextHolder.requireAuthenticationContext();
 * String userId = auth.userId();
 * </pre>
 */
public final class SecurityContextHolder {

    /**
     * gRPC Context key for storing/retrieving authentication context.
     * This key is used by GrpcAuthenticationInterceptor to store the context.
     */
    public static final Context.Key<AuthenticationContext> CONTEXT_KEY =
            Context.key("authentication-context");

    // Private constructor to prevent instantiation
    private SecurityContextHolder() {
        throw new AssertionError("Utility class should not be instantiated");
    }

    /**
     * Retrieves the authentication context for the current request.
     * Returns empty Optional if the request is not authenticated or if
     * called outside of a gRPC request context.
     *
     * @return Optional containing AuthenticationContext if present
     */
    public static Optional<AuthenticationContext> getAuthenticationContext() {
        AuthenticationContext context = CONTEXT_KEY.get();
        return Optional.ofNullable(context);
    }

    /**
     * Retrieves the authentication context for the current request.
     * Throws AuthenticationException if no authentication context is present.
     *
     * Use this method when authentication is required for the operation.
     *
     * @return AuthenticationContext for the current request
     * @throws AuthenticationException if no authentication context is present
     */
    public static AuthenticationContext requireAuthenticationContext() {
        return getAuthenticationContext()
                .orElseThrow(() -> new AuthenticationException(
                        "Authentication required. No authentication context found for this request."));
    }

    /**
     * Checks if the current request has an authenticated user.
     *
     * @return true if authentication context is present
     */
    public static boolean isAuthenticated() {
        return CONTEXT_KEY.get() != null;
    }

    /**
     * Retrieves the authenticated user's ID.
     *
     * @return Optional containing user ID if authenticated
     */
    public static Optional<String> getCurrentUserId() {
        return getAuthenticationContext().map(AuthenticationContext::userId);
    }

    /**
     * Retrieves the authenticated user's email.
     *
     * @return Optional containing email if authenticated
     */
    public static Optional<String> getCurrentUserEmail() {
        return getAuthenticationContext().map(AuthenticationContext::email);
    }

    /**
     * Checks if the authenticated user has a specific role.
     *
     * @param role The role to check
     * @return true if user is authenticated and has the role
     */
    public static boolean hasRole(String role) {
        return getAuthenticationContext()
                .map(context -> context.hasRole(role))
                .orElse(false);
    }

    /**
     * Checks if the authenticated user has any of the specified roles.
     *
     * @param roles Roles to check
     * @return true if user is authenticated and has at least one role
     */
    public static boolean hasAnyRole(String... roles) {
        return getAuthenticationContext()
                .map(context -> context.hasAnyRole(roles))
                .orElse(false);
    }
}
