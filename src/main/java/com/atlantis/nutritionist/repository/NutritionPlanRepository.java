package com.atlantis.nutritionist.repository;

import com.atlantis.nutritionist.entity.NutritionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for NutritionPlan entity.
 * Provides database operations for nutrition plans.
 */
@Repository
public interface NutritionPlanRepository extends JpaRepository<NutritionPlan, UUID> {

    /**
     * Find all nutrition plans for a client ordered by creation date descending.
     * @param clientId The client's UUID
     * @return List of nutrition plans
     */
    List<NutritionPlan> findByClientIdOrderByCreatedAtDesc(UUID clientId);

    /**
     * Find active nutrition plans for a client.
     * @param clientId The client's UUID
     * @param isActive Active status
     * @return List of active nutrition plans
     */
    List<NutritionPlan> findByClientIdAndIsActive(UUID clientId, Boolean isActive);

    /**
     * Find the currently active nutrition plan for a client.
     * @param clientId The client's UUID
     * @return Optional containing the active nutrition plan
     */
    @Query("SELECT np FROM NutritionPlan np WHERE np.client.id = :clientId AND np.isActive = true ORDER BY np.startDate DESC LIMIT 1")
    Optional<NutritionPlan> findActiveByClientId(@Param("clientId") UUID clientId);

    /**
     * Find nutrition plans by nutritionist.
     * @param nutritionistId The nutritionist's UUID
     * @return List of nutrition plans
     */
    List<NutritionPlan> findByNutritionistId(UUID nutritionistId);

    /**
     * Find nutrition plans by plan type.
     * @param clientId The client's UUID
     * @param planType The plan type
     * @return List of nutrition plans
     */
    List<NutritionPlan> findByClientIdAndPlanType(UUID clientId, String planType);
}
