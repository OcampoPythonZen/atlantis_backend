package com.atlantis.nutritionist.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security configuration for the application.
 *
 * Since this is a gRPC-only application (no HTTP REST endpoints),
 * we disable most HTTP security features and focus on providing
 * password encoding capabilities for user authentication.
 *
 * Key configurations:
 * - BCrypt password encoder for secure password hashing
 * - Disabled HTTP security (gRPC security is handled by interceptors)
 * - Stateless session management (JWT-based authentication)
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    public SecurityConfig() {
        log.info("Initializing Spring Security configuration");
    }

    /**
     * Configures HTTP security.
     * Since this is a gRPC application, we disable HTTP security features.
     *
     * @param http HttpSecurity to configure
     * @return SecurityFilterChain
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("Configuring HTTP security filter chain");

        http
                // Disable CSRF (not needed for gRPC/stateless JWT)
                .csrf(AbstractHttpConfigurer::disable)

                // Disable form login (using JWT instead)
                .formLogin(AbstractHttpConfigurer::disable)

                // Disable HTTP Basic auth
                .httpBasic(AbstractHttpConfigurer::disable)

                // Session management: stateless (JWT tokens)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Permit all HTTP requests (gRPC security handled separately)
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());

        log.info("HTTP security configured: CSRF disabled, stateless sessions, all requests permitted");

        return http.build();
    }

    /**
     * Password encoder bean using BCrypt hashing algorithm.
     * BCrypt is a strong, adaptive hash function designed for passwords.
     *
     * Features:
     * - Automatic salt generation
     * - Configurable work factor (cost)
     * - Resistant to rainbow table attacks
     *
     * Usage:
     * <pre>
     * @Autowired
     * private PasswordEncoder passwordEncoder;
     *
     * String hashedPassword = passwordEncoder.encode(rawPassword);
     * boolean matches = passwordEncoder.matches(rawPassword, hashedPassword);
     * </pre>
     *
     * @return BCryptPasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        log.info("Creating BCryptPasswordEncoder bean");
        // Using default strength (10). Can be increased for higher security at cost of performance.
        return new BCryptPasswordEncoder();
    }
}
