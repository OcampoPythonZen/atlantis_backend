package com.atlantis.nutritionist.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark gRPC service methods as public (no authentication required).
 * Methods annotated with @PublicEndpoint will bypass authentication checks.
 *
 * Usage:
 * <pre>
 * @PublicEndpoint
 * @Override
 * public void login(LoginRequest request, StreamObserver<AuthResponse> responseObserver) {
 *     // Login logic - no authentication needed
 * }
 * </pre>
 *
 * Common use cases:
 * - Authentication endpoints (login, register)
 * - Health checks
 * - Public information endpoints
 * - Token validation/refresh
 *
 * WARNING: Use sparingly and only for endpoints that truly need to be public.
 * By default, all gRPC methods require authentication unless marked with this annotation.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PublicEndpoint {
    /**
     * Optional description of why this endpoint is public.
     * Useful for documentation and security audits.
     */
    String value() default "";
}
