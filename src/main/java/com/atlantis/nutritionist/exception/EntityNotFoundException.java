package com.atlantis.nutritionist.exception;

/**
 * Exception thrown when a requested entity is not found in the system.
 *
 * This exception is mapped to gRPC Status.NOT_FOUND by the GrpcExceptionHandler.
 *
 * Example usage:
 * <pre>
 * throw new EntityNotFoundException("User not found with ID: " + userId);
 * throw new EntityNotFoundException("Client", clientId);
 * </pre>
 */
public class EntityNotFoundException extends RuntimeException {

    private final String entityType;
    private final String entityId;

    public EntityNotFoundException(String message) {
        super(message);
        this.entityType = null;
        this.entityId = null;
    }

    public EntityNotFoundException(String entityType, String entityId) {
        super(String.format("%s not found with ID: %s", entityType, entityId));
        this.entityType = entityType;
        this.entityId = entityId;
    }

    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
        this.entityType = null;
        this.entityId = null;
    }

    public String getEntityType() {
        return entityType;
    }

    public String getEntityId() {
        return entityId;
    }
}
