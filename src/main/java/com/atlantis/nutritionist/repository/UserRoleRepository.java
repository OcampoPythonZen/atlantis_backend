package com.atlantis.nutritionist.repository;

import com.atlantis.nutritionist.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for UserRole entity.
 * Provides data access methods for user-role associations.
 *
 * @see UserRole
 */
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRole.UserRoleId> {

    /**
     * Find all UserRole associations for a given user.
     *
     * @param userId the user's ID
     * @return List of UserRole associations
     */
    @Query("SELECT ur FROM UserRole ur JOIN FETCH ur.role WHERE ur.user.id = :userId")
    List<UserRole> findByUserId(@Param("userId") UUID userId);

    /**
     * Find all roles for a given user.
     * Returns role names with ROLE_ prefix for Spring Security.
     *
     * @param userId the user's ID
     * @return List of role names (e.g., ["ROLE_NUTRIOLOGIST"])
     */
    @Query("SELECT CONCAT('ROLE_', r.roleName) FROM UserRole ur JOIN ur.role r WHERE ur.user.id = :userId")
    List<String> findRoleNamesByUserId(@Param("userId") UUID userId);

    /**
     * Delete a specific user-role association.
     *
     * @param userId the user's ID
     * @param roleId the role's ID
     */
    @Modifying
    @Query("DELETE FROM UserRole ur WHERE ur.user.id = :userId AND ur.role.id = :roleId")
    void deleteByUserIdAndRoleId(@Param("userId") UUID userId, @Param("roleId") Short roleId);

    /**
     * Check if a user has a specific role.
     *
     * @param userId the user's ID
     * @param roleId the role's ID
     * @return true if association exists, false otherwise
     */
    boolean existsByUserIdAndRoleId(UUID userId, Short roleId);
}
