package com.atlantis.nutritionist.exception;

import java.util.Collections;
import java.util.Map;

/**
 * Exception thrown when request validation fails.
 * Contains field-level error details to provide specific feedback about validation failures.
 *
 * This exception is mapped to gRPC Status.INVALID_ARGUMENT by the GrpcExceptionHandler.
 *
 * Example usage:
 * <pre>
 * Map<String, String> errors = new HashMap<>();
 * errors.put("email", "Invalid email format");
 * errors.put("password", "Password must be at least 8 characters");
 * throw new ValidationException("Validation failed", errors);
 * </pre>
 */
public class ValidationException extends RuntimeException {

    private final Map<String, String> fieldErrors;

    public ValidationException(String message) {
        super(message);
        this.fieldErrors = Collections.emptyMap();
    }

    public ValidationException(String message, Map<String, String> fieldErrors) {
        super(message);
        this.fieldErrors = fieldErrors != null ? Map.copyOf(fieldErrors) : Collections.emptyMap();
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
        this.fieldErrors = Collections.emptyMap();
    }

    /**
     * Returns an immutable map of field names to error messages.
     * @return Field errors map
     */
    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }

    /**
     * Checks if there are any field-level errors.
     * @return true if there are field errors
     */
    public boolean hasFieldErrors() {
        return !fieldErrors.isEmpty();
    }

    /**
     * Gets the error message for a specific field.
     * @param fieldName The field name
     * @return The error message or null if no error for that field
     */
    public String getFieldError(String fieldName) {
        return fieldErrors.get(fieldName);
    }
}
