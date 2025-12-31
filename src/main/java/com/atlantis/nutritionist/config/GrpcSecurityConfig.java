package com.atlantis.nutritionist.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for gRPC security.
 *
 * Note: Interceptors are now registered directly on their classes using @GrpcGlobalServerInterceptor
 * annotation instead of being registered here to avoid circular dependency issues.
 *
 * Interceptor order:
 * 1. GrpcExceptionHandler (via @GrpcAdvice) - Catches all exceptions
 * 2. GrpcAuthenticationInterceptor - Validates JWT and stores auth context
 * 3. GrpcAuthorizationInterceptor - Checks role-based permissions
 * 4. Service Method - Executes business logic
 *
 * The @GrpcGlobalServerInterceptor annotation on interceptor classes registers them globally
 * for all gRPC services in the application.
 */
@Configuration
public class GrpcSecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(GrpcSecurityConfig.class);

    public GrpcSecurityConfig() {
        log.info("Initializing gRPC security configuration");
        log.info("Interceptors are registered via @GrpcGlobalServerInterceptor on their classes");
    }
}
