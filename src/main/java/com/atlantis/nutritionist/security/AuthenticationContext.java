package com.atlantis.nutritionist.security;

import com.atlantis.nutritionist.jwt.model.TokenClaims;

import java.util.List;

/**
 * Immutable record representing the authenticated user's security context.
 * This record is stored in gRPC Context for each request and provides
 * convenient methods to check roles and permissions.
 *
 * Usage example:
 * <pre>
 * AuthenticationContext auth = SecurityContextHolder.requireAuthenticationContext();
 * if (auth.isNutriologist()) {
 *     // Process nutritionist-specific logic
 * }
 * </pre>
 */
public record AuthenticationContext(
        String userId,
        String email,
        List<String> roles,
        TokenClaims tokenClaims
) {

    /**
     * Checks if the user has a specific role.
     *
     * @param role The role to check (e.g., "ROLE_NUTRIOLOGIST")
     * @return true if user has the role
     */
    public boolean hasRole(String role) {
        return roles != null && roles.contains(role);
    }

    /**
     * Checks if the user has any of the specified roles.
     *
     * @param rolesToCheck Roles to check
     * @return true if user has at least one of the roles
     */
    public boolean hasAnyRole(String... rolesToCheck) {
        if (roles == null || rolesToCheck == null) {
            return false;
        }
        for (String role : rolesToCheck) {
            if (roles.contains(role)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the user has all of the specified roles.
     *
     * @param rolesToCheck Roles to check
     * @return true if user has all of the roles
     */
    public boolean hasAllRoles(String... rolesToCheck) {
        if (roles == null || rolesToCheck == null) {
            return false;
        }
        for (String role : rolesToCheck) {
            if (!roles.contains(role)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the authenticated user is a nutritionist.
     *
     * @return true if user has ROLE_NUTRIOLOGIST
     */
    public boolean isNutriologist() {
        return hasRole("ROLE_NUTRIOLOGIST");
    }

    /**
     * Checks if the authenticated user is a patient.
     *
     * @return true if user has ROLE_PATIENT
     */
    public boolean isPatient() {
        return hasRole("ROLE_PATIENT");
    }

    /**
     * Checks if the authenticated user is an admin.
     *
     * @return true if user has ROLE_ADMIN
     */
    public boolean isAdmin() {
        return hasRole("ROLE_ADMIN");
    }

    /**
     * Returns a human-readable representation of the authentication context.
     *
     * @return String representation
     */
    @Override
    public String toString() {
        return String.format("AuthenticationContext[userId=%s, email=%s, roles=%s]",
                userId, email, roles);
    }
}
