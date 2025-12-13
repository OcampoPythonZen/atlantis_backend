package com.atlantis.nutritionist.validation;

import com.atlantis.nutritionist.exception.ValidationException;
import com.atlantis.nutritionist.grpc.common.UUID;

import java.util.Map;

/**
 * Utility class for validating gRPC request fields with error accumulation.
 * Collects all validation errors before throwing a single ValidationException.
 *
 * Usage pattern:
 * <pre>
 * Map<String, String> errors = new HashMap<>();
 * RequestValidator.validateRequired("email", request.getEmail(), errors);
 * RequestValidator.validateEmail("email", request.getEmail(), errors);
 * RequestValidator.validateRequired("password", request.getPassword(), errors);
 * RequestValidator.throwIfErrors(errors);
 * </pre>
 */
public final class RequestValidator {

    // Private constructor to prevent instantiation
    private RequestValidator() {
        throw new AssertionError("Utility class should not be instantiated");
    }

    /**
     * Validates that a string field is not null or blank.
     *
     * @param fieldName The field name for error reporting
     * @param value     The value to validate
     * @param errors    Map to accumulate errors
     */
    public static void validateRequired(String fieldName, String value, Map<String, String> errors) {
        if (ValidationUtils.isNullOrBlank(value)) {
            errors.put(fieldName, fieldName + " is required");
        }
    }

    /**
     * Validates that an object field is not null.
     *
     * @param fieldName The field name for error reporting
     * @param value     The value to validate
     * @param errors    Map to accumulate errors
     */
    public static void validateRequired(String fieldName, Object value, Map<String, String> errors) {
        if (value == null) {
            errors.put(fieldName, fieldName + " is required");
        }
    }

    /**
     * Validates that a proto UUID field is present and has a value.
     *
     * @param fieldName The field name for error reporting
     * @param uuid      The proto UUID to validate
     * @param errors    Map to accumulate errors
     */
    public static void validateRequired(String fieldName, UUID uuid, Map<String, String> errors) {
        if (uuid == null || ValidationUtils.isNullOrBlank(uuid.getValue())) {
            errors.put(fieldName, fieldName + " is required");
        }
    }

    /**
     * Validates email format if the value is provided.
     * Skips validation if value is null or blank.
     *
     * @param fieldName The field name for error reporting
     * @param email     The email to validate
     * @param errors    Map to accumulate errors
     */
    public static void validateEmail(String fieldName, String email, Map<String, String> errors) {
        if (!ValidationUtils.isNullOrBlank(email) && !ValidationUtils.isValidEmail(email)) {
            errors.put(fieldName, "Invalid email format");
        }
    }

    /**
     * Validates UUID format for a proto UUID field.
     *
     * @param fieldName The field name for error reporting
     * @param uuid      The proto UUID to validate
     * @param errors    Map to accumulate errors
     */
    public static void validateUUID(String fieldName, UUID uuid, Map<String, String> errors) {
        if (uuid != null && !ValidationUtils.isNullOrBlank(uuid.getValue()) &&
            !ValidationUtils.isValidUUID(uuid.getValue())) {
            errors.put(fieldName, "Invalid UUID format");
        }
    }

    /**
     * Validates UUID format for a string UUID.
     *
     * @param fieldName The field name for error reporting
     * @param uuid      The UUID string to validate
     * @param errors    Map to accumulate errors
     */
    public static void validateUUID(String fieldName, String uuid, Map<String, String> errors) {
        if (!ValidationUtils.isNullOrBlank(uuid) && !ValidationUtils.isValidUUID(uuid)) {
            errors.put(fieldName, "Invalid UUID format");
        }
    }

    /**
     * Validates that a number is positive (> 0).
     *
     * @param fieldName The field name for error reporting
     * @param value     The number to validate
     * @param errors    Map to accumulate errors
     */
    public static void validatePositive(String fieldName, Number value, Map<String, String> errors) {
        if (value != null && !ValidationUtils.isPositive(value)) {
            errors.put(fieldName, fieldName + " must be positive");
        }
    }

    /**
     * Validates that a number is non-negative (>= 0).
     *
     * @param fieldName The field name for error reporting
     * @param value     The number to validate
     * @param errors    Map to accumulate errors
     */
    public static void validateNonNegative(String fieldName, Number value, Map<String, String> errors) {
        if (value != null && !ValidationUtils.isNonNegative(value)) {
            errors.put(fieldName, fieldName + " must be non-negative");
        }
    }

    /**
     * Validates that a number is within a specified range (inclusive).
     *
     * @param fieldName The field name for error reporting
     * @param value     The number to validate
     * @param min       Minimum value (inclusive)
     * @param max       Maximum value (inclusive)
     * @param errors    Map to accumulate errors
     */
    public static void validateRange(String fieldName, Number value, Number min, Number max, Map<String, String> errors) {
        if (value != null && !ValidationUtils.isInRange(value, min, max)) {
            errors.put(fieldName, String.format("%s must be between %s and %s", fieldName, min, max));
        }
    }

    /**
     * Validates ISO 8601 date format (YYYY-MM-DD).
     *
     * @param fieldName The field name for error reporting
     * @param date      The date string to validate
     * @param errors    Map to accumulate errors
     */
    public static void validateDateFormat(String fieldName, String date, Map<String, String> errors) {
        if (!ValidationUtils.isNullOrBlank(date) && !ValidationUtils.isValidDateISO8601(date)) {
            errors.put(fieldName, "Invalid date format. Expected ISO 8601 format (YYYY-MM-DD)");
        }
    }

    /**
     * Validates password strength.
     * Requirements: min 8 chars, uppercase, lowercase, digit.
     *
     * @param fieldName The field name for error reporting
     * @param password  The password to validate
     * @param errors    Map to accumulate errors
     */
    public static void validatePasswordStrength(String fieldName, String password, Map<String, String> errors) {
        if (!ValidationUtils.isNullOrBlank(password) && !ValidationUtils.isStrongPassword(password)) {
            errors.put(fieldName,
                    "Password must be at least 8 characters and contain uppercase, lowercase, and digit");
        }
    }

    /**
     * Validates phone number format.
     *
     * @param fieldName The field name for error reporting
     * @param phone     The phone number to validate
     * @param errors    Map to accumulate errors
     */
    public static void validatePhoneNumber(String fieldName, String phone, Map<String, String> errors) {
        if (!ValidationUtils.isNullOrBlank(phone) && !ValidationUtils.isValidPhoneNumber(phone)) {
            errors.put(fieldName, "Invalid phone number format");
        }
    }

    /**
     * Validates string length is within bounds.
     *
     * @param fieldName The field name for error reporting
     * @param value     The string to validate
     * @param minLength Minimum length (inclusive)
     * @param maxLength Maximum length (inclusive)
     * @param errors    Map to accumulate errors
     */
    public static void validateLength(String fieldName, String value, int minLength, int maxLength, Map<String, String> errors) {
        if (!ValidationUtils.isNullOrBlank(value) &&
            !ValidationUtils.isLengthInRange(value, minLength, maxLength)) {
            errors.put(fieldName,
                    String.format("%s length must be between %d and %d characters", fieldName, minLength, maxLength));
        }
    }

    /**
     * Validates that a required string is present AND meets length requirements.
     *
     * @param fieldName The field name for error reporting
     * @param value     The string to validate
     * @param minLength Minimum length (inclusive)
     * @param maxLength Maximum length (inclusive)
     * @param errors    Map to accumulate errors
     */
    public static void validateRequiredLength(String fieldName, String value, int minLength, int maxLength, Map<String, String> errors) {
        validateRequired(fieldName, value, errors);
        if (!ValidationUtils.isNullOrBlank(value)) {
            validateLength(fieldName, value, minLength, maxLength, errors);
        }
    }

    /**
     * Throws ValidationException if the errors map is not empty.
     * This should be called after accumulating all validation errors.
     *
     * @param errors Map of field errors
     * @throws ValidationException if errors map contains entries
     */
    public static void throwIfErrors(Map<String, String> errors) {
        if (!errors.isEmpty()) {
            throw new ValidationException("Validation failed", errors);
        }
    }

    /**
     * Convenience method to validate and throw in one call.
     * Throws ValidationException with a custom message if errors exist.
     *
     * @param message Custom error message
     * @param errors  Map of field errors
     * @throws ValidationException if errors map contains entries
     */
    public static void throwIfErrors(String message, Map<String, String> errors) {
        if (!errors.isEmpty()) {
            throw new ValidationException(message, errors);
        }
    }
}
