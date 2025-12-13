package com.atlantis.nutritionist.exception;

/**
 * Exception thrown when a user lacks permission to perform an action.
 * This includes scenarios such as:
 * - User doesn't have required role
 * - Attempting to access another user's resources
 * - Insufficient privileges for operation
 *
 * This exception is mapped to gRPC Status.PERMISSION_DENIED by the GrpcExceptionHandler.
 */
public class AuthorizationException extends RuntimeException {

    private final String requiredRole;
    private final String userId;

    public AuthorizationException(String message) {
        super(message);
        this.requiredRole = null;
        this.userId = null;
    }

    public AuthorizationException(String message, String userId, String requiredRole) {
        super(message);
        this.userId = userId;
        this.requiredRole = requiredRole;
    }

    public AuthorizationException(String message, Throwable cause) {
        super(message, cause);
        this.requiredRole = null;
        this.userId = null;
    }

    public String getRequiredRole() {
        return requiredRole;
    }

    public String getUserId() {
        return userId;
    }
}
