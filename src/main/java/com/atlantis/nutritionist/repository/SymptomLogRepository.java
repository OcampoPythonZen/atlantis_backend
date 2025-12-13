package com.atlantis.nutritionist.repository;

import com.atlantis.nutritionist.entity.SymptomLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Repository interface for SymptomLog entity.
 * Provides database operations for symptom logs.
 */
@Repository
public interface SymptomLogRepository extends JpaRepository<SymptomLog, UUID> {

    /**
     * Find all symptom logs for a client ordered by log date descending.
     * @param clientId The client's UUID
     * @return List of symptom logs
     */
    List<SymptomLog> findByClientIdOrderByLogDateDesc(UUID clientId);

    /**
     * Find symptom logs for a client within a date range.
     * @param clientId The client's UUID
     * @param startDate Start date
     * @param endDate End date
     * @return List of symptom logs
     */
    List<SymptomLog> findByClientIdAndLogDateBetween(UUID clientId, Instant startDate, Instant endDate);

    /**
     * Find symptom logs by symptom name for a client.
     * @param clientId The client's UUID
     * @param symptomName The symptom name
     * @return List of symptom logs
     */
    List<SymptomLog> findByClientIdAndSymptomNameContainingIgnoreCase(UUID clientId, String symptomName);
}
