package com.atlantis.nutritionist.repository;

import com.atlantis.nutritionist.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Repository interface for AuditLog entity.
 * Provides database operations for audit logs.
 */
@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    /**
     * Find audit logs by user ordered by timestamp descending.
     * @param userId The user's UUID
     * @return List of audit logs
     */
    List<AuditLog> findByUserIdOrderByTimestampDesc(UUID userId);

    /**
     * Find audit logs by entity type and entity ID.
     * @param entityType The entity type
     * @param entityId The entity's UUID
     * @return List of audit logs
     */
    List<AuditLog> findByEntityTypeAndEntityIdOrderByTimestampDesc(String entityType, UUID entityId);

    /**
     * Find audit logs within a date range.
     * @param startDate Start date
     * @param endDate End date
     * @return List of audit logs
     */
    List<AuditLog> findByTimestampBetween(Instant startDate, Instant endDate);

    /**
     * Find audit logs by action.
     * @param action The action performed
     * @return List of audit logs
     */
    List<AuditLog> findByAction(String action);
}
