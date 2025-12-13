package com.atlantis.nutritionist.exception;

import com.atlantis.nutritionist.jwt.exception.ExpiredTokenException;
import com.atlantis.nutritionist.jwt.exception.InvalidTokenException;
import io.grpc.*;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

/**
 * Centralized exception handler for gRPC services.
 * Maps Java exceptions to appropriate gRPC Status codes with sanitized error messages.
 *
 * Exception Mapping:
 * - ExpiredTokenException → UNAUTHENTICATED ("Token has expired")
 * - InvalidTokenException → UNAUTHENTICATED ("Invalid token")
 * - AuthenticationException → UNAUTHENTICATED
 * - AuthorizationException → PERMISSION_DENIED
 * - ValidationException → INVALID_ARGUMENT (with field details)
 * - EntityNotFoundException → NOT_FOUND
 * - EntityAlreadyExistsException → ALREADY_EXISTS
 * - IllegalArgumentException → INVALID_ARGUMENT
 * - IllegalStateException → FAILED_PRECONDITION
 * - Exception → INTERNAL (sanitized in production)
 */
@GrpcAdvice
public class GrpcExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GrpcExceptionHandler.class);

    @Value("${spring.profiles.active:dev}")
    private String activeProfile;

    /**
     * Handles expired JWT tokens.
     */
    @net.devh.boot.grpc.server.advice.GrpcExceptionHandler(ExpiredTokenException.class)
    public StatusException handleExpiredToken(ExpiredTokenException ex) {
        log.warn("Expired token: {}", ex.getMessage());
        return Status.UNAUTHENTICATED
                .withDescription("Token has expired. Please refresh your token or login again.")
                .asException();
    }

    /**
     * Handles invalid JWT tokens (signature mismatch, malformed, etc.).
     */
    @net.devh.boot.grpc.server.advice.GrpcExceptionHandler(InvalidTokenException.class)
    public StatusException handleInvalidToken(InvalidTokenException ex) {
        log.warn("Invalid token: {}", ex.getMessage());
        return Status.UNAUTHENTICATED
                .withDescription("Invalid authentication token. Please login again.")
                .asException();
    }

    /**
     * Handles authentication failures (missing token, invalid credentials, etc.).
     */
    @net.devh.boot.grpc.server.advice.GrpcExceptionHandler(AuthenticationException.class)
    public StatusException handleAuthenticationException(AuthenticationException ex) {
        log.warn("Authentication failed: {}", ex.getMessage());
        return Status.UNAUTHENTICATED
                .withDescription(ex.getMessage())
                .asException();
    }

    /**
     * Handles authorization failures (insufficient permissions, wrong role, etc.).
     */
    @net.devh.boot.grpc.server.advice.GrpcExceptionHandler(AuthorizationException.class)
    public StatusException handleAuthorizationException(AuthorizationException ex) {
        log.warn("Authorization failed: {}", ex.getMessage());

        String description = ex.getMessage();
        if (ex.getRequiredRole() != null) {
            description = String.format("Access denied. Required role: %s", ex.getRequiredRole());
        }

        return Status.PERMISSION_DENIED
                .withDescription(description)
                .asException();
    }

    /**
     * Handles validation errors with field-level details.
     */
    @net.devh.boot.grpc.server.advice.GrpcExceptionHandler(ValidationException.class)
    public StatusException handleValidationException(ValidationException ex) {
        log.warn("Validation failed: {}", ex.getMessage());

        StringBuilder description = new StringBuilder(ex.getMessage());

        if (ex.hasFieldErrors()) {
            description.append(". Errors: ");
            ex.getFieldErrors().forEach((field, error) ->
                description.append(String.format("%s: %s; ", field, error))
            );
        }

        return Status.INVALID_ARGUMENT
                .withDescription(description.toString())
                .asException();
    }

    /**
     * Handles entity not found errors.
     */
    @net.devh.boot.grpc.server.advice.GrpcExceptionHandler(EntityNotFoundException.class)
    public StatusException handleEntityNotFoundException(EntityNotFoundException ex) {
        log.warn("Entity not found: {}", ex.getMessage());
        return Status.NOT_FOUND
                .withDescription(ex.getMessage())
                .asException();
    }

    /**
     * Handles duplicate entity creation attempts.
     */
    @net.devh.boot.grpc.server.advice.GrpcExceptionHandler(EntityAlreadyExistsException.class)
    public StatusException handleEntityAlreadyExistsException(EntityAlreadyExistsException ex) {
        log.warn("Entity already exists: {}", ex.getMessage());
        return Status.ALREADY_EXISTS
                .withDescription(ex.getMessage())
                .asException();
    }

    /**
     * Handles invalid arguments (bad input parameters).
     */
    @net.devh.boot.grpc.server.advice.GrpcExceptionHandler(IllegalArgumentException.class)
    public StatusException handleIllegalArgumentException(IllegalArgumentException ex) {
        log.warn("Invalid argument: {}", ex.getMessage());
        return Status.INVALID_ARGUMENT
                .withDescription(ex.getMessage())
                .asException();
    }

    /**
     * Handles illegal state errors (operation not allowed in current state).
     */
    @net.devh.boot.grpc.server.advice.GrpcExceptionHandler(IllegalStateException.class)
    public StatusException handleIllegalStateException(IllegalStateException ex) {
        log.warn("Illegal state: {}", ex.getMessage());
        return Status.FAILED_PRECONDITION
                .withDescription(ex.getMessage())
                .asException();
    }

    /**
     * Handles all other uncaught exceptions.
     * In production, sanitizes error messages to avoid leaking sensitive information.
     */
    @net.devh.boot.grpc.server.advice.GrpcExceptionHandler(Exception.class)
    public StatusException handleGenericException(Exception ex) {
        log.error("Unexpected error occurred", ex);

        String description;
        if (isProduction()) {
            // In production, don't leak internal error details
            description = "An internal error occurred. Please contact support if the problem persists.";
        } else {
            // In development, show full error for debugging
            description = String.format("Internal error: %s: %s",
                    ex.getClass().getSimpleName(),
                    ex.getMessage());
        }

        return Status.INTERNAL
                .withDescription(description)
                .asException();
    }

    /**
     * Checks if the application is running in production mode.
     */
    private boolean isProduction() {
        return "prod".equalsIgnoreCase(activeProfile) ||
               "production".equalsIgnoreCase(activeProfile);
    }
}
