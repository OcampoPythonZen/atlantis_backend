package com.atlantis.nutritionist.repository;

import com.atlantis.nutritionist.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for User entity.
 * Provides data access methods for user management.
 *
 * Custom queries:
 * - findByEmail: Find user by email address
 * - existsByEmail: Check if user exists by email
 * - findByEmailWithRoles: Find user with eagerly loaded roles (JOIN FETCH)
 * - findByIdWithRoles: Find user by ID with eagerly loaded roles
 *
 * @see User
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Find user by email address.
     *
     * @param email the user's email
     * @return Optional containing the user if found
     */
    Optional<User> findByEmail(String email);

    /**
     * Check if a user exists with the given email address.
     *
     * @param email the email to check
     * @return true if user exists, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Find user by email with roles eagerly loaded using JOIN FETCH.
     * This query avoids N+1 query problem by fetching user, userRoles, and roles in a single query.
     *
     * @param email the user's email
     * @return Optional containing the user with roles if found
     */
    @Query("SELECT DISTINCT u FROM User u " +
           "LEFT JOIN FETCH u.userRoles ur " +
           "LEFT JOIN FETCH ur.role " +
           "WHERE u.email = :email")
    Optional<User> findByEmailWithRoles(@Param("email") String email);

    /**
     * Find user by ID with roles eagerly loaded using JOIN FETCH.
     * Useful for authentication context after token validation.
     *
     * @param id the user's ID
     * @return Optional containing the user with roles if found
     */
    @Query("SELECT DISTINCT u FROM User u " +
           "LEFT JOIN FETCH u.userRoles ur " +
           "LEFT JOIN FETCH ur.role " +
           "WHERE u.id = :id")
    Optional<User> findByIdWithRoles(@Param("id") UUID id);

    /**
     * Find active users only.
     *
     * @param email the user's email
     * @return Optional containing the active user if found
     */
    Optional<User> findByEmailAndIsActiveTrue(String email);
}
