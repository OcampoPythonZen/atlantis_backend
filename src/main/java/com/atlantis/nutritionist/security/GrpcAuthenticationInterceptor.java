package com.atlantis.nutritionist.security;

import com.atlantis.nutritionist.exception.AuthenticationException;
import com.atlantis.nutritionist.jwt.JwtTokenValidator;
import com.atlantis.nutritionist.jwt.model.TokenClaims;
import com.atlantis.nutritionist.util.MetadataUtils;
import io.grpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Optional;

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
public class GrpcAuthenticationInterceptor implements ServerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(GrpcAuthenticationInterceptor.class);

    private final JwtTokenValidator jwtTokenValidator;

    public GrpcAuthenticationInterceptor(JwtTokenValidator jwtTokenValidator) {
        this.jwtTokenValidator = jwtTokenValidator;
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
     * Checks if the method being called is annotated with @PublicEndpoint.
     * Uses reflection to find the annotation on the service implementation method.
     *
     * @param call The server call
     * @return true if method is public (has @PublicEndpoint annotation)
     */
    private <ReqT, RespT> boolean isPublicEndpoint(ServerCall<ReqT, RespT> call) {
        try {
            String fullMethodName = call.getMethodDescriptor().getFullMethodName();
            // Format: package.ServiceName/MethodName
            String[] parts = fullMethodName.split("/");
            if (parts.length != 2) {
                return false;
            }

            String methodName = parts[1];

            // Get the service descriptor to find the service class
            ServerMethodDefinition<ReqT, RespT> methodDef = call.getMethodDescriptor().getServiceName() != null ?
                    findMethodDefinition(call) : null;

            if (methodDef == null) {
                // If we can't find the method, default to requiring authentication
                return false;
            }

            // Try to find @PublicEndpoint annotation
            // Note: This is a simplified approach. In production, you might want to cache these lookups.
            return hasPublicEndpointAnnotation(methodName, methodDef);

        } catch (Exception e) {
            log.warn("Error checking for @PublicEndpoint annotation, defaulting to authenticated", e);
            return false; // Default to requiring authentication on error
        }
    }

    /**
     * Attempts to find the ServerMethodDefinition for the current call.
     */
    private <ReqT, RespT> ServerMethodDefinition<ReqT, RespT> findMethodDefinition(ServerCall<ReqT, RespT> call) {
        // This is a placeholder - in actual implementation, you'd look up the method definition
        // from the server's registered services
        return null;
    }

    /**
     * Checks if a method has the @PublicEndpoint annotation.
     * This implementation looks for the annotation on the service implementation class.
     */
    private <ReqT, RespT> boolean hasPublicEndpointAnnotation(
            String methodName,
            ServerMethodDefinition<ReqT, RespT> methodDef) {

        // In a real implementation, you would:
        // 1. Get the service implementation class from methodDef
        // 2. Use reflection to find the method
        // 3. Check for @PublicEndpoint annotation
        //
        // For now, we'll use a simpler approach with a registry pattern
        // (to be implemented in service classes)

        return false; // Default implementation
    }
}
