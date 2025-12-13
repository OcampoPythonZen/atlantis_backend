package com.atlantis.nutritionist.exception;

/**
 * Exception thrown when attempting to create an entity that already exists.
 * Typically used for duplicate key violations or unique constraint failures.
 *
 * This exception is mapped to gRPC Status.ALREADY_EXISTS by the GrpcExceptionHandler.
 *
 * Example usage:
 * <pre>
 * throw new EntityAlreadyExistsException("User with email " + email + " already exists");
 * throw new EntityAlreadyExistsException("Client", "user_id", userId);
 * </pre>
 */
public class EntityAlreadyExistsException extends RuntimeException {

    private final String entityType;
    private final String fieldName;
    private final String fieldValue;

    public EntityAlreadyExistsException(String message) {
        super(message);
        this.entityType = null;
        this.fieldName = null;
        this.fieldValue = null;
    }

    public EntityAlreadyExistsException(String entityType, String fieldName, String fieldValue) {
        super(String.format("%s already exists with %s: %s", entityType, fieldName, fieldValue));
        this.entityType = entityType;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public EntityAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
        this.entityType = null;
        this.fieldName = null;
        this.fieldValue = null;
    }

    public String getEntityType() {
        return entityType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFieldValue() {
        return fieldValue;
    }
}
