package com.atlantis.nutritionist.security;

import com.atlantis.nutritionist.exception.AuthorizationException;
import io.grpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * gRPC server interceptor for role-based authorization.
 *
 * This interceptor runs AFTER GrpcAuthenticationInterceptor and assumes
 * authentication context is already populated (if required).
 *
 * Current implementation:
 * - Provides a foundation for role-based access control
 * - Actual role checking is performed within service methods using SecurityContextHolder
 * - This design is more explicit and easier to maintain
 *
 * Service methods should check roles like this:
 * <pre>
 * AuthenticationContext auth = SecurityContextHolder.requireAuthenticationContext();
 * if (!auth.isNutriologist() && !auth.isAdmin()) {
 *     throw new AuthorizationException("Access denied. Required role: ROLE_NUTRIOLOGIST");
 * }
 * </pre>
 *
 * Future enhancement:
 * - Can be extended to use @RequireRole annotation via reflection
 * - Can implement method-level authorization policies
 * - Can integrate with Spring Security's authorization framework
 */
@Component
public class GrpcAuthorizationInterceptor implements ServerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(GrpcAuthorizationInterceptor.class);

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        String methodName = call.getMethodDescriptor().getFullMethodName();
        log.debug("Authorization check for method: {}", methodName);

        // Authentication context should already be set by GrpcAuthenticationInterceptor
        // If authentication is required but context is missing, it would have been caught earlier

        // For now, we delegate authorization checks to service methods
        // This allows for more fine-grained control and explicit authorization logic

        return next.startCall(call, headers);
    }

    /**
     * Utility method to check if user has required role.
     * Can be called from service methods for role-based access control.
     *
     * @param requiredRoles Roles required for access (user needs at least one)
     * @throws AuthorizationException if user doesn't have required role
     */
    public static void requireRole(String... requiredRoles) {
        AuthenticationContext auth = SecurityContextHolder.requireAuthenticationContext();

        if (!auth.hasAnyRole(requiredRoles)) {
            String rolesList = String.join(", ", requiredRoles);
            throw new AuthorizationException(
                    "Access denied. Required roles: " + rolesList,
                    auth.userId(),
                    rolesList
            );
        }
    }

    /**
     * Utility method to check if user is the owner of a resource.
     * Useful for ensuring users can only access their own data.
     *
     * @param resourceUserId The user ID that owns the resource
     * @throws AuthorizationException if user is not the owner and not an admin
     */
    public static void requireOwnerOrAdmin(String resourceUserId) {
        AuthenticationContext auth = SecurityContextHolder.requireAuthenticationContext();

        if (!auth.userId().equals(resourceUserId) && !auth.isAdmin()) {
            throw new AuthorizationException(
                    "Access denied. You can only access your own resources.",
                    auth.userId(),
                    "OWNER or ROLE_ADMIN"
            );
        }
    }

    /**
     * Utility method to check if user is a nutriologist or admin.
     * Common authorization check for nutriologist-specific operations.
     *
     * @throws AuthorizationException if user is neither nutriologist nor admin
     */
    public static void requireNutriologistOrAdmin() {
        requireRole("ROLE_NUTRIOLOGIST", "ROLE_ADMIN");
    }

    /**
     * Utility method to check if user is a patient, nutriologist, or admin.
     * Common authorization check for operations accessible to patients and nutritionists.
     *
     * @throws AuthorizationException if user doesn't have required role
     */
    public static void requirePatientNutriologistOrAdmin() {
        requireRole("ROLE_PATIENT", "ROLE_NUTRIOLOGIST", "ROLE_ADMIN");
    }
}
