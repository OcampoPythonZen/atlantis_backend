package com.atlantis.nutritionist.validation;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

/**
 * Utility class providing static validation methods for common data types.
 * This class contains pure validation logic without side effects.
 *
 * Usage example:
 * <pre>
 * if (!ValidationUtils.isValidEmail(email)) {
 *     errors.put("email", "Invalid email format");
 * }
 * </pre>
 */
public final class ValidationUtils {

    // Email regex pattern - RFC 5322 simplified
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    // UUID regex pattern
    private static final Pattern UUID_PATTERN = Pattern.compile(
            "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$"
    );

    // Phone number pattern - supports various formats
    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^[+]?[(]?[0-9]{1,4}[)]?[-\\s.]?[(]?[0-9]{1,4}[)]?[-\\s.]?[0-9]{1,9}$"
    );

    // ISO 8601 date formatter
    private static final DateTimeFormatter ISO_DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    // Private constructor to prevent instantiation
    private ValidationUtils() {
        throw new AssertionError("Utility class should not be instantiated");
    }

    /**
     * Validates email format using RFC 5322 simplified pattern.
     *
     * @param email The email to validate
     * @return true if valid email format
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /**
     * Validates UUID format (8-4-4-4-12 hexadecimal pattern).
     *
     * @param uuid The UUID string to validate
     * @return true if valid UUID format
     */
    public static boolean isValidUUID(String uuid) {
        if (uuid == null || uuid.isBlank()) {
            return false;
        }
        return UUID_PATTERN.matcher(uuid.trim()).matches();
    }

    /**
     * Validates ISO 8601 date format (YYYY-MM-DD).
     *
     * @param date The date string to validate
     * @return true if valid ISO 8601 date
     */
    public static boolean isValidDateISO8601(String date) {
        if (date == null || date.isBlank()) {
            return false;
        }
        try {
            ISO_DATE_FORMATTER.parse(date.trim());
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Validates password strength.
     * Requirements:
     * - Minimum 8 characters
     * - At least one uppercase letter
     * - At least one lowercase letter
     * - At least one digit
     *
     * @param password The password to validate
     * @return true if password meets strength requirements
     */
    public static boolean isStrongPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasUpperCase = false;
        boolean hasLowerCase = false;
        boolean hasDigit = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUpperCase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowerCase = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            }

            // Early exit if all requirements met
            if (hasUpperCase && hasLowerCase && hasDigit) {
                return true;
            }
        }

        return hasUpperCase && hasLowerCase && hasDigit;
    }

    /**
     * Validates phone number format.
     * Accepts various international formats with optional country codes.
     *
     * @param phone The phone number to validate
     * @return true if valid phone format
     */
    public static boolean isValidPhoneNumber(String phone) {
        if (phone == null || phone.isBlank()) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone.trim()).matches();
    }

    /**
     * Checks if a string is null or blank (empty or whitespace only).
     *
     * @param value The string to check
     * @return true if string is null or blank
     */
    public static boolean isNullOrBlank(String value) {
        return value == null || value.isBlank();
    }

    /**
     * Validates that a number is positive (> 0).
     *
     * @param value The number to validate
     * @return true if positive
     */
    public static boolean isPositive(Number value) {
        if (value == null) {
            return false;
        }
        return value.doubleValue() > 0;
    }

    /**
     * Validates that a number is non-negative (>= 0).
     *
     * @param value The number to validate
     * @return true if non-negative
     */
    public static boolean isNonNegative(Number value) {
        if (value == null) {
            return false;
        }
        return value.doubleValue() >= 0;
    }

    /**
     * Validates that a number is within a specified range (inclusive).
     *
     * @param value The number to validate
     * @param min   Minimum value (inclusive)
     * @param max   Maximum value (inclusive)
     * @return true if within range
     */
    public static boolean isInRange(Number value, Number min, Number max) {
        if (value == null || min == null || max == null) {
            return false;
        }
        double val = value.doubleValue();
        double minVal = min.doubleValue();
        double maxVal = max.doubleValue();
        return val >= minVal && val <= maxVal;
    }

    /**
     * Validates that a string's length is within specified bounds.
     *
     * @param value     The string to validate
     * @param minLength Minimum length (inclusive)
     * @param maxLength Maximum length (inclusive)
     * @return true if length is within bounds
     */
    public static boolean isLengthInRange(String value, int minLength, int maxLength) {
        if (value == null) {
            return false;
        }
        int length = value.length();
        return length >= minLength && length <= maxLength;
    }
}
