package com.atlantis.nutritionist.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for role-based access control on gRPC service methods.
 * Methods annotated with @RequireRole will only be accessible to users with one of the specified roles.
 *
 * Usage:
 * <pre>
 * @RequireRole({"ROLE_NUTRIOLOGIST", "ROLE_ADMIN"})
 * @Override
 * public void createNutriologist(CreateNutriologistRequest request,
 *                                StreamObserver<NutriologistResponse> responseObserver) {
 *     // Only NUTRIOLOGIST or ADMIN can create nutriologists
 * }
 * </pre>
 *
 * Role naming convention:
 * - Use "ROLE_" prefix for all roles
 * - Examples: ROLE_NUTRIOLOGIST, ROLE_PATIENT, ROLE_ADMIN
 *
 * Authorization behavior:
 * - If user has ANY of the specified roles, access is granted
 * - If user has NONE of the specified roles, AuthorizationException is thrown
 * - Empty array means any authenticated user can access (authentication required, no role check)
 *
 * Notes:
 * - This annotation requires @PublicEndpoint to NOT be present (authentication required)
 * - The GrpcAuthorizationInterceptor enforces these role requirements
 * - Users must be authenticated before role checks occur
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireRole {
    /**
     * Array of roles that are allowed to access this method.
     * User must have AT LEAST ONE of these roles.
     *
     * @return Array of role names (e.g., {"ROLE_NUTRIOLOGIST", "ROLE_ADMIN"})
     */
    String[] value();

    /**
     * Optional description of the authorization requirement.
     * Useful for documentation.
     */
    String description() default "";
}
