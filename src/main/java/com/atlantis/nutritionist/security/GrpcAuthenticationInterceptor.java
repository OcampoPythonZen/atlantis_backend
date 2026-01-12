package com.atlantis.nutritionist.security;

import com.atlantis.nutritionist.exception.AuthenticationException;
import com.atlantis.nutritionist.jwt.JwtTokenValidator;
import com.atlantis.nutritionist.jwt.model.TokenClaims;
import com.atlantis.nutritionist.service.TokenBlacklistService;
import com.atlantis.nutritionist.util.MetadataUtils;
import io.grpc.*;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

/**
 * gRPC server interceptor that handles JWT authentication for all service methods.
 *
 * Responsibilities:
 * - Extract JWT token from "Authorization: Bearer <token>" header
 * - Validate token using JwtTokenValidator
 * - Create AuthenticationContext and store in gRPC Context
 * - Skip authentication for methods annotated with @PublicEndpoint
 * - Throw AuthenticationException for missing or invalid tokens (caught by GrpcExceptionHandler)
 *
 * Flow:
 * 1. Extract method being called
 * 2. Check if method has @PublicEndpoint annotation
 * 3. If public, proceed without authentication
 * 4. If not public, extract and validate JWT token
 * 5. Store authentication context in gRPC Context
 * 6. Proceed to next interceptor/service method
 */
@Component
@GrpcGlobalServerInterceptor
public class GrpcAuthenticationInterceptor implements ServerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(GrpcAuthenticationInterceptor.class);

    private final JwtTokenValidator jwtTokenValidator;
    private final TokenBlacklistService tokenBlacklistService;

    /**
     * Set of public endpoints that don't require authentication.
     * Format: "package.ServiceName/MethodName"
     * IMPORTANT: Must match the full method name from proto package definition
     */
    private static final Set<String> PUBLIC_ENDPOINTS = Set.of(
            // Auth service endpoints (using full package from auth_service.proto)
            "com.atlantis.nutritionist.grpc.AuthService/Login",
            "com.atlantis.nutritionist.grpc.AuthService/Register",
            "com.atlantis.nutritionist.grpc.AuthService/RefreshToken",
            "com.atlantis.nutritionist.grpc.AuthService/ValidateToken",
            // Health check endpoints
            "com.atlantis.nutritionist.grpc.HealthCheckService/Check",
            "com.atlantis.nutritionist.grpc.HealthCheckService/GetSystemStatus",
            "grpc.health.v1.Health/Check",
            "grpc.health.v1.Health/Watch"
    );

    public GrpcAuthenticationInterceptor(JwtTokenValidator jwtTokenValidator,
                                          TokenBlacklistService tokenBlacklistService) {
        this.jwtTokenValidator = jwtTokenValidator;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        // Get the method being called
        String methodName = call.getMethodDescriptor().getFullMethodName();
        log.debug("Intercepting call to: {}", methodName);

        try {
            // Check if this is a public endpoint
            if (isPublicEndpoint(call)) {
                log.debug("Public endpoint detected, skipping authentication: {}", methodName);
                return next.startCall(call, headers);
            }

            // Extract JWT token from authorization header
            Optional<String> tokenOptional = MetadataUtils.extractBearerToken(headers);
            if (tokenOptional.isEmpty()) {
                log.warn("No authentication token provided for protected endpoint: {}", methodName);
                throw new AuthenticationException("Authentication required. No token provided.");
            }

            String token = tokenOptional.get();
            log.debug("Token extracted for method: {}", methodName);

            // Check if token is blacklisted (logged out)
            if (tokenBlacklistService.isBlacklisted(token)) {
                log.warn("Blacklisted token used for endpoint: {}", methodName);
                throw new AuthenticationException("Token has been revoked. Please login again.");
            }

            // Validate token and extract claims
            TokenClaims tokenClaims = jwtTokenValidator.validateAndParse(token);
            log.debug("Token validated successfully for user: {}", tokenClaims.subject());

            // Create authentication context
            AuthenticationContext authContext = new AuthenticationContext(
                    tokenClaims.subject(),
                    tokenClaims.email(),
                    tokenClaims.roles(),
                    tokenClaims
            );

            // Store context in gRPC Context
            Context context = Context.current()
                    .withValue(SecurityContextHolder.CONTEXT_KEY, authContext);

            log.debug("Authentication context stored for user: {} with roles: {}",
                    authContext.userId(), authContext.roles());

            // Continue call with authentication context
            return Contexts.interceptCall(context, call, headers, next);

        } catch (AuthenticationException e) {
            // Re-throw to be handled by GrpcExceptionHandler
            throw e;
        } catch (Exception e) {
            // Wrap unexpected exceptions
            log.error("Unexpected error during authentication for method: {}", methodName, e);
            throw new AuthenticationException("Authentication failed: " + e.getMessage(), e);
        }
    }

    /**
     * Checks if the method being called is a public endpoint.
     * Uses a static set of known public endpoints for simplicity.
     *
     * @param call The server call
     * @return true if method is public (no authentication required)
     */
    private <ReqT, RespT> boolean isPublicEndpoint(ServerCall<ReqT, RespT> call) {
        String fullMethodName = call.getMethodDescriptor().getFullMethodName();
        boolean isPublic = PUBLIC_ENDPOINTS.contains(fullMethodName);

        if (isPublic) {
            log.debug("Endpoint {} is marked as public", fullMethodName);
        }

        return isPublic;
    }
}
