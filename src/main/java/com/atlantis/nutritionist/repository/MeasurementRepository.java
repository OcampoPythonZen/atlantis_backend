package com.atlantis.nutritionist.repository;

import com.atlantis.nutritionist.entity.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Repository interface for Measurement entity.
 * Provides database operations for biometric measurements.
 */
@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, UUID> {

    /**
     * Find all measurements for a client ordered by measurement date descending.
     * @param clientId The client's UUID
     * @return List of measurements
     */
    List<Measurement> findByClientIdOrderByMeasurementDateDesc(UUID clientId);

    /**
     * Find all measurements for a client by nutritionist.
     * @param clientId The client's UUID
     * @param nutritionistId The nutritionist's UUID
     * @return List of measurements
     */
    List<Measurement> findByClientIdAndNutritionistId(UUID clientId, UUID nutritionistId);

    /**
     * Find measurements for a client within a date range.
     * @param clientId The client's UUID
     * @param startDate Start date
     * @param endDate End date
     * @return List of measurements
     */
    @Query("SELECT m FROM Measurement m WHERE m.client.id = :clientId AND m.measurementDate BETWEEN :startDate AND :endDate ORDER BY m.measurementDate DESC")
    List<Measurement> findByClientIdAndDateRange(
        @Param("clientId") UUID clientId,
        @Param("startDate") Instant startDate,
        @Param("endDate") Instant endDate
    );

    /**
     * Find the latest measurement for a client.
     * @param clientId The client's UUID
     * @return The most recent measurement
     */
    @Query("SELECT m FROM Measurement m WHERE m.client.id = :clientId ORDER BY m.measurementDate DESC LIMIT 1")
    Measurement findLatestByClientId(@Param("clientId") UUID clientId);
}
