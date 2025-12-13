package com.atlantis.nutritionist.repository;

import com.atlantis.nutritionist.entity.ClinicalNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Repository interface for ClinicalNote entity.
 * Provides database operations for clinical notes.
 */
@Repository
public interface ClinicalNoteRepository extends JpaRepository<ClinicalNote, UUID> {

    /**
     * Find all clinical notes for a client ordered by consultation date descending.
     * @param clientId The client's UUID
     * @return List of clinical notes
     */
    List<ClinicalNote> findByClientIdOrderByConsultationDateDesc(UUID clientId);

    /**
     * Find clinical notes by nutritionist.
     * @param nutritionistId The nutritionist's UUID
     * @return List of clinical notes
     */
    List<ClinicalNote> findByNutritionistId(UUID nutritionistId);

    /**
     * Find clinical notes for a client within a date range.
     * @param clientId The client's UUID
     * @param startDate Start date
     * @param endDate End date
     * @return List of clinical notes
     */
    List<ClinicalNote> findByClientIdAndConsultationDateBetween(UUID clientId, Instant startDate, Instant endDate);

    /**
     * Find clinical notes by consultation type for a client.
     * @param clientId The client's UUID
     * @param consultationType The consultation type
     * @return List of clinical notes
     */
    List<ClinicalNote> findByClientIdAndConsultationType(UUID clientId, String consultationType);
}
