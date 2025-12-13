package com.atlantis.nutritionist.config;

import com.atlantis.nutritionist.security.GrpcAuthenticationInterceptor;
import com.atlantis.nutritionist.security.GrpcAuthorizationInterceptor;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for gRPC security interceptors.
 * Registers global server interceptors in the correct order.
 *
 * Interceptor order:
 * 1. GrpcExceptionHandler (via @GrpcAdvice) - Catches all exceptions
 * 2. GrpcAuthenticationInterceptor - Validates JWT and stores auth context
 * 3. GrpcAuthorizationInterceptor - Checks role-based permissions
 * 4. Service Method - Executes business logic
 *
 * The @GrpcGlobalServerInterceptor annotation registers interceptors globally
 * for all gRPC services in the application.
 */
@Configuration
public class GrpcSecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(GrpcSecurityConfig.class);

    public GrpcSecurityConfig() {
        log.info("Initializing gRPC security configuration");
    }

    /**
     * Registers the authentication interceptor globally.
     * This interceptor validates JWT tokens and populates authentication context.
     *
     * @param authenticationInterceptor The authentication interceptor bean
     * @return The same interceptor (for Spring to register)
     */
    @GrpcGlobalServerInterceptor
    public GrpcAuthenticationInterceptor grpcAuthenticationInterceptor(
            GrpcAuthenticationInterceptor authenticationInterceptor) {
        log.info("Registering global gRPC authentication interceptor");
        return authenticationInterceptor;
    }

    /**
     * Registers the authorization interceptor globally.
     * This interceptor provides role-based access control utilities.
     *
     * @param authorizationInterceptor The authorization interceptor bean
     * @return The same interceptor (for Spring to register)
     */
    @GrpcGlobalServerInterceptor
    public GrpcAuthorizationInterceptor grpcAuthorizationInterceptor(
            GrpcAuthorizationInterceptor authorizationInterceptor) {
        log.info("Registering global gRPC authorization interceptor");
        return authorizationInterceptor;
    }
}
