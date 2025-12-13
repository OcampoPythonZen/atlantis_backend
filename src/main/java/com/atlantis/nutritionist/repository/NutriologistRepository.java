package com.atlantis.nutritionist.repository;

import com.atlantis.nutritionist.entity.Nutriologist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for Nutriologist entity.
 * Provides database operations for nutritionist profiles.
 */
@Repository
public interface NutriologistRepository extends JpaRepository<Nutriologist, UUID> {

    /**
     * Find nutriologist by user ID.
     * @param userId The user's UUID
     * @return Optional containing the nutriologist if found
     */
    Optional<Nutriologist> findByUserId(UUID userId);

    /**
     * Find nutriologist by professional license.
     * @param professionalLicense The professional license number
     * @return Optional containing the nutriologist if found
     */
    Optional<Nutriologist> findByProfessionalLicense(String professionalLicense);

    /**
     * Find all verified nutriologists.
     * @return List of verified nutriologists
     */
    List<Nutriologist> findByVerifiedTrue();

    /**
     * Find nutriologists by specialization.
     * @param specialization The specialization
     * @return List of nutriologists
     */
    List<Nutriologist> findBySpecializationContainingIgnoreCase(String specialization);

    /**
     * Check if nutriologist exists for a user.
     * @param userId The user's UUID
     * @return true if nutriologist exists
     */
    boolean existsByUserId(UUID userId);
}
