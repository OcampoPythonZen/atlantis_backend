package com.atlantis.nutritionist.repository;

import com.atlantis.nutritionist.entity.ClientGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for ClientGoal entity.
 * Provides database operations for client goals.
 */
@Repository
public interface ClientGoalRepository extends JpaRepository<ClientGoal, UUID> {

    /**
     * Find all goals for a client ordered by creation date descending.
     * @param clientId The client's UUID
     * @return List of client goals
     */
    List<ClientGoal> findByClientIdOrderByCreatedAtDesc(UUID clientId);

    /**
     * Find active goals for a client.
     * @param clientId The client's UUID
     * @param status The goal status
     * @return List of active client goals
     */
    List<ClientGoal> findByClientIdAndStatus(UUID clientId, String status);

    /**
     * Find goals by nutritionist.
     * @param nutritionistId The nutritionist's UUID
     * @return List of client goals
     */
    List<ClientGoal> findByNutritionistId(UUID nutritionistId);

    /**
     * Find goals by client and goal type.
     * @param clientId The client's UUID
     * @param goalType The goal type
     * @return List of client goals
     */
    List<ClientGoal> findByClientIdAndGoalType(UUID clientId, String goalType);
}
