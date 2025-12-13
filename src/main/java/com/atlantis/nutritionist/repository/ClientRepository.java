package com.atlantis.nutritionist.repository;

import com.atlantis.nutritionist.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for Client entity.
 * Provides database operations for client profiles.
 */
@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {

    /**
     * Find client by user ID.
     * @param userId The user's UUID
     * @return Optional containing the client if found
     */
    Optional<Client> findByUserId(UUID userId);

    /**
     * Check if client exists for a user.
     * @param userId The user's UUID
     * @return true if client exists
     */
    boolean existsByUserId(UUID userId);
}
