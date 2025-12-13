package com.atlantis.nutritionist.repository;

import com.atlantis.nutritionist.entity.SupplementTracking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Repository interface for SupplementTracking entity.
 * Provides database operations for supplement tracking.
 */
@Repository
public interface SupplementTrackingRepository extends JpaRepository<SupplementTracking, UUID> {

    /**
     * Find all supplements for a client ordered by creation date descending.
     * @param clientId The client's UUID
     * @return List of supplement tracking records
     */
    List<SupplementTracking> findByClientIdOrderByCreatedAtDesc(UUID clientId);

    /**
     * Find supplements by nutritionist.
     * @param nutritionistId The nutritionist's UUID
     * @return List of supplement tracking records
     */
    List<SupplementTracking> findByNutritionistId(UUID nutritionistId);

    /**
     * Find active supplements for a client (no end date or end date in the future).
     * @param clientId The client's UUID
     * @param currentDate Current date
     * @return List of active supplement tracking records
     */
    @Query("SELECT s FROM SupplementTracking s WHERE s.client.id = :clientId AND (s.endDate IS NULL OR s.endDate >= :currentDate)")
    List<SupplementTracking> findActiveByClientId(@Param("clientId") UUID clientId, @Param("currentDate") LocalDate currentDate);

    /**
     * Find supplements by type for a client.
     * @param clientId The client's UUID
     * @param supplementType The supplement type
     * @return List of supplement tracking records
     */
    List<SupplementTracking> findByClientIdAndSupplementType(UUID clientId, String supplementType);
}
