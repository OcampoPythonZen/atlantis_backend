package com.atlantis.nutritionist.repository;

import com.atlantis.nutritionist.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for Role entity.
 * Provides data access methods for role management.
 *
 * Predefined roles in the system:
 * - NUTRIOLOGIST (id=1)
 * - PATIENT (id=2)
 *
 * @see Role
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Short> {

    /**
     * Find role by role name.
     *
     * @param roleName the name of the role (e.g., "NUTRIOLOGIST", "PATIENT")
     * @return Optional containing the role if found
     */
    Optional<Role> findByRoleName(String roleName);
}
