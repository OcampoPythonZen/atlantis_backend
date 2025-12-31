package com.atlantis.nutritionist.grpc;

import com.atlantis.nutritionist.entity.User;
import com.atlantis.nutritionist.exception.AuthenticationException;
import com.atlantis.nutritionist.exception.EntityAlreadyExistsException;
import com.atlantis.nutritionist.exception.ValidationException;
import com.atlantis.nutritionist.grpc.auth.*;
import com.atlantis.nutritionist.grpc.common.Empty;
import com.atlantis.nutritionist.jwt.JwtTokenProvider;
import com.atlantis.nutritionist.jwt.JwtTokenValidator;
import com.atlantis.nutritionist.jwt.model.JwtToken;
import com.atlantis.nutritionist.jwt.model.TokenClaims;
import com.atlantis.nutritionist.security.PublicEndpoint;
import com.atlantis.nutritionist.service.TokenBlacklistService;
import com.atlantis.nutritionist.service.UserService;
import com.atlantis.nutritionist.validation.RequestValidator;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * gRPC service implementation for authentication operations.
 * Handles user registration, login, token refresh, logout, and password management.
 *
 * Integrated with database through UserService for:
 * - User creation and retrieval
 * - Role assignment
 * - Password verification and updates
 *
 * Token blacklist implemented for logout and session invalidation.
 */
@GrpcService
public class AuthServiceImpl extends AuthServiceGrpc.AuthServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenValidator jwtTokenValidator;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final TokenBlacklistService tokenBlacklistService;

    public AuthServiceImpl(JwtTokenProvider jwtTokenProvider,
                          JwtTokenValidator jwtTokenValidator,
                          PasswordEncoder passwordEncoder,
                          UserService userService,
                          TokenBlacklistService tokenBlacklistService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtTokenValidator = jwtTokenValidator;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    /**
     * Register a new user account.
     * Validates input, creates user (TODO), assigns role, and generates JWT tokens.
     */
    @PublicEndpoint("User registration is public")
    @Override
    public void register(RegisterRequest request, StreamObserver<AuthResponse> responseObserver) {
        log.info("Registration request for email: {}", request.getEmail());

        try {
            // Validate input
            Map<String, String> errors = new HashMap<>();
            RequestValidator.validateRequired("email", request.getEmail(), errors);
            RequestValidator.validateEmail("email", request.getEmail(), errors);
            RequestValidator.validateRequired("password", request.getPassword(), errors);
            RequestValidator.validatePasswordStrength("password", request.getPassword(), errors);
            RequestValidator.validateRequired("first_name", request.getFirstName(), errors);
            RequestValidator.validateRequired("last_name", request.getLastName(), errors);
            RequestValidator.validateRequired("role", request.getRole(), errors);

            if (request.hasPhone()) {
                RequestValidator.validatePhoneNumber("phone", request.getPhone(), errors);
            }

            RequestValidator.throwIfErrors(errors);

            // Map role enum to role ID
            Short roleId = mapRoleToId(request.getRole());

            // Create user with role
            User savedUser = userService.createUser(
                request.getEmail(),
                request.getPassword(),
                request.getFirstName(),
                request.getLastName(),
                request.hasPhone() ? request.getPhone() : null,
                Set.of(roleId)
            );

            String userId = savedUser.getId().toString();
            String email = savedUser.getEmail();
            Set<String> userRoles = userService.getUserRoleNames(savedUser.getId());
            List<String> roles = userRoles.stream()
                .map(role -> "ROLE_" + role)
                .collect(Collectors.toList());

            log.info("User registered successfully with ID: {}", userId);

            // Generate JWT tokens
            JwtToken jwtToken = jwtTokenProvider.generateToken(userId, email, roles);

            // Build response
            AuthResponse response = AuthResponse.newBuilder()
                    .setAccessToken(jwtToken.accessToken())
                    .setRefreshToken(jwtToken.refreshToken())
                    .setExpiresIn(jwtToken.getExpiresIn())
                    .setUserId(com.atlantis.nutritionist.grpc.common.UUID.newBuilder()
                            .setValue(userId)
                            .build())
                    .setEmail(email)
                    .addRoles(request.getRole())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (ValidationException | EntityAlreadyExistsException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error during registration", e);
            throw new AuthenticationException("Registration failed: " + e.getMessage(), e);
        }
    }

    /**
     * Authenticate a user and generate JWT tokens.
     * Validates credentials against database (TODO) and returns tokens.
     */
    @PublicEndpoint("Login is public")
    @Override
    public void login(LoginRequest request, StreamObserver<AuthResponse> responseObserver) {
        log.info("Login request for email: {}", request.getEmail());

        try {
            // Validate input
            Map<String, String> errors = new HashMap<>();
            RequestValidator.validateRequired("email", request.getEmail(), errors);
            RequestValidator.validateEmail("email", request.getEmail(), errors);
            RequestValidator.validateRequired("password", request.getPassword(), errors);
            RequestValidator.throwIfErrors(errors);

            // Retrieve user from database
            User user = userService.findByEmail(request.getEmail());

            // Verify password
            if (!userService.verifyPassword(request.getEmail(), request.getPassword())) {
                throw new AuthenticationException("Invalid email or password");
            }

            // Check if user is active
            if (!user.getIsActive()) {
                throw new AuthenticationException("Account is disabled");
            }

            // Get user roles from database
            Set<String> userRoles = userService.getUserRoleNames(user.getId());
            List<String> roles = userRoles.stream()
                .map(role -> "ROLE_" + role)
                .collect(Collectors.toList());

            String userId = user.getId().toString();
            String email = user.getEmail();

            log.info("User authenticated successfully: {}", email);

            // Generate JWT tokens
            JwtToken jwtToken = jwtTokenProvider.generateToken(userId, email, roles);

            // Build response
            AuthResponse response = AuthResponse.newBuilder()
                    .setAccessToken(jwtToken.accessToken())
                    .setRefreshToken(jwtToken.refreshToken())
                    .setExpiresIn(jwtToken.getExpiresIn())
                    .setUserId(com.atlantis.nutritionist.grpc.common.UUID.newBuilder()
                            .setValue(userId)
                            .build())
                    .setEmail(email)
                    .addAllRoles(convertRolesToEnum(roles))
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (AuthenticationException | ValidationException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error during login", e);
            throw new AuthenticationException("Login failed: " + e.getMessage(), e);
        }
    }

    /**
     * Refresh access token using a valid refresh token.
     * Validates refresh token and generates new token pair.
     */
    @PublicEndpoint("Token refresh is public")
    @Override
    public void refreshToken(RefreshTokenRequest request, StreamObserver<AuthResponse> responseObserver) {
        log.info("Token refresh request");

        try {
            // Validate input
            Map<String, String> errors = new HashMap<>();
            RequestValidator.validateRequired("refresh_token", request.getRefreshToken(), errors);
            RequestValidator.throwIfErrors(errors);

            // Validate refresh token
            TokenClaims tokenClaims = jwtTokenValidator.validateAndParse(request.getRefreshToken());

            // Check if token is blacklisted
            if (tokenBlacklistService.isBlacklisted(request.getRefreshToken())) {
                throw new AuthenticationException("Token has been revoked");
            }

            // Verify user still exists and is active
            User user = userService.findById(UUID.fromString(tokenClaims.subject()));
            if (!user.getIsActive()) {
                throw new AuthenticationException("Account is disabled");
            }

            log.info("Refresh token validated successfully for user: {}", tokenClaims.subject());

            // Generate new JWT tokens
            JwtToken newJwtToken = jwtTokenProvider.generateToken(
                    tokenClaims.subject(),
                    tokenClaims.email(),
                    tokenClaims.roles()
            );

            // Build response
            AuthResponse response = AuthResponse.newBuilder()
                    .setAccessToken(newJwtToken.accessToken())
                    .setRefreshToken(newJwtToken.refreshToken())
                    .setExpiresIn(newJwtToken.getExpiresIn())
                    .setUserId(com.atlantis.nutritionist.grpc.common.UUID.newBuilder()
                            .setValue(tokenClaims.subject())
                            .build())
                    .setEmail(tokenClaims.email())
                    .addAllRoles(convertRolesToEnum(tokenClaims.roles()))
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (ValidationException | AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error during token refresh", e);
            throw new AuthenticationException("Token refresh failed: " + e.getMessage(), e);
        }
    }

    /**
     * Logout user by invalidating their token.
     * Adds the token to the blacklist so it can no longer be used.
     */
    @Override
    public void logout(LogoutRequest request, StreamObserver<Empty> responseObserver) {
        log.info("Logout request");

        try {
            // Validate input
            Map<String, String> errors = new HashMap<>();
            RequestValidator.validateRequired("token", request.getToken(), errors);
            RequestValidator.throwIfErrors(errors);

            // Validate token (to ensure it's a valid format)
            TokenClaims tokenClaims = jwtTokenValidator.validateAndParse(request.getToken());

            // Add token to blacklist
            tokenBlacklistService.blacklistToken(
                    request.getToken(),
                    tokenClaims.expiresAt(),
                    UUID.fromString(tokenClaims.subject()),
                    "logout"
            );

            log.info("User logged out successfully: {}", tokenClaims.subject());

            // Return empty response
            Empty response = Empty.newBuilder().build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (ValidationException | AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error during logout", e);
            throw new AuthenticationException("Logout failed: " + e.getMessage(), e);
        }
    }

    /**
     * Change user password.
     * Updates the password in the database after validating the old password.
     */
    @Override
    public void changePassword(ChangePasswordRequest request, StreamObserver<Empty> responseObserver) {
        log.info("Change password request for user: {}", request.getUserId().getValue());

        try {
            // Validate input
            Map<String, String> errors = new HashMap<>();
            RequestValidator.validateRequired("user_id", request.getUserId(), errors);
            RequestValidator.validateRequired("old_password", request.getOldPassword(), errors);
            RequestValidator.validateRequired("new_password", request.getNewPassword(), errors);
            RequestValidator.validatePasswordStrength("new_password", request.getNewPassword(), errors);
            RequestValidator.throwIfErrors(errors);

            UUID userId = UUID.fromString(request.getUserId().getValue());

            // Retrieve user from database
            User user = userService.findById(userId);

            // Verify old password
            if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
                throw new AuthenticationException("Current password is incorrect");
            }

            // Check new password is different from old
            if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
                throw new ValidationException("New password must be different from current password");
            }

            // Update password in database
            userService.changePassword(userId, request.getNewPassword());

            log.info("Password changed successfully for user: {}", userId);

            // Return empty response
            Empty response = Empty.newBuilder().build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (ValidationException | AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error during password change", e);
            throw new AuthenticationException("Password change failed: " + e.getMessage(), e);
        }
    }

    /**
     * Validate a JWT token and return its claims.
     * Public endpoint for token validation.
     */
    @PublicEndpoint("Token validation is public")
    @Override
    public void validateToken(ValidateTokenRequest request, StreamObserver<ValidateTokenResponse> responseObserver) {
        log.info("Token validation request");

        try {
            // Validate input
            Map<String, String> errors = new HashMap<>();
            RequestValidator.validateRequired("token", request.getToken(), errors);
            RequestValidator.throwIfErrors(errors);

            // Validate token and extract claims
            TokenClaims tokenClaims = jwtTokenValidator.validateAndParse(request.getToken());

            log.info("Token validated successfully for user: {}", tokenClaims.subject());

            // Build response
            // Convert string roles to UserRole enum
            var userRolesBuilder = ValidateTokenResponse.newBuilder()
                    .setValid(true)
                    .setUserId(com.atlantis.nutritionist.grpc.common.UUID.newBuilder()
                            .setValue(tokenClaims.subject())
                            .build())
                    .setEmail(tokenClaims.email());

            // Add roles (for now, just add a mock role - TODO: convert string roles to enums)
            for (String role : tokenClaims.roles()) {
                if (role.equals("ROLE_NUTRIOLOGIST")) {
                    userRolesBuilder.addRoles(com.atlantis.nutritionist.grpc.common.UserRole.ROLE_NUTRIOLOGIST);
                } else if (role.equals("ROLE_PATIENT")) {
                    userRolesBuilder.addRoles(com.atlantis.nutritionist.grpc.common.UserRole.ROLE_PATIENT);
                } else if (role.equals("ROLE_ADMIN")) {
                    userRolesBuilder.addRoles(com.atlantis.nutritionist.grpc.common.UserRole.ROLE_ADMIN);
                }
            }

            ValidateTokenResponse response = userRolesBuilder.build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            // For validation failures, return valid=false instead of throwing
            log.warn("Token validation failed: {}", e.getMessage());

            ValidateTokenResponse response = ValidateTokenResponse.newBuilder()
                    .setValid(false)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }

    /**
     * Helper method to map gRPC UserRole enum to database role ID.
     */
    private Short mapRoleToId(com.atlantis.nutritionist.grpc.common.UserRole role) {
        return switch (role) {
            case ROLE_NUTRIOLOGIST -> (short) 1;
            case ROLE_PATIENT -> (short) 2;
            default -> throw new ValidationException("Invalid role: " + role);
        };
    }

    /**
     * Helper method to convert string roles to gRPC UserRole enum list.
     */
    private List<com.atlantis.nutritionist.grpc.common.UserRole> convertRolesToEnum(List<String> roles) {
        return roles.stream()
            .map(role -> {
                if (role.equals("ROLE_NUTRIOLOGIST") || role.equals("NUTRIOLOGIST")) {
                    return com.atlantis.nutritionist.grpc.common.UserRole.ROLE_NUTRIOLOGIST;
                } else if (role.equals("ROLE_PATIENT") || role.equals("PATIENT")) {
                    return com.atlantis.nutritionist.grpc.common.UserRole.ROLE_PATIENT;
                } else if (role.equals("ROLE_ADMIN") || role.equals("ADMIN")) {
                    return com.atlantis.nutritionist.grpc.common.UserRole.ROLE_ADMIN;
                }
                return com.atlantis.nutritionist.grpc.common.UserRole.UNRECOGNIZED;
            })
            .filter(role -> role != com.atlantis.nutritionist.grpc.common.UserRole.UNRECOGNIZED)
            .collect(Collectors.toList());
    }
}
