package com.atlantis.nutritionist.repository;

import com.atlantis.nutritionist.entity.MedicalHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for MedicalHistory entity.
 * Provides database operations for medical history records.
 */
@Repository
public interface MedicalHistoryRepository extends JpaRepository<MedicalHistory, UUID> {

    /**
     * Find medical history by client ID.
     * @param clientId The client's UUID
     * @return Optional containing the medical history if found
     */
    Optional<MedicalHistory> findByClientId(UUID clientId);

    /**
     * Check if medical history exists for a client.
     * @param clientId The client's UUID
     * @return true if medical history exists
     */
    boolean existsByClientId(UUID clientId);
}
