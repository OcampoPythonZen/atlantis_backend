package com.atlantis.nutritionist.exception;

/**
 * Exception thrown when authentication fails.
 * This includes scenarios such as:
 * - Invalid credentials
 * - Missing authentication token
 * - Malformed authentication data
 *
 * This exception is mapped to gRPC Status.UNAUTHENTICATED by the GrpcExceptionHandler.
 */
public class AuthenticationException extends RuntimeException {

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
