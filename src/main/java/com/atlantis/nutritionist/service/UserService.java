package com.atlantis.nutritionist.service;

import com.atlantis.nutritionist.entity.Role;
import com.atlantis.nutritionist.entity.User;
import com.atlantis.nutritionist.entity.UserRole;
import com.atlantis.nutritionist.exception.EntityAlreadyExistsException;
import com.atlantis.nutritionist.exception.EntityNotFoundException;
import com.atlantis.nutritionist.repository.RoleRepository;
import com.atlantis.nutritionist.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service class for User entity operations.
 * Handles user creation, updates, authentication, and role management.
 */
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                      RoleRepository roleRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Create a new user with hashed password.
     * @param email User email
     * @param password Plain text password
     * @param firstName First name
     * @param lastName Last name
     * @param phone Phone number
     * @param roleIds Role IDs to assign
     * @return Created user
     * @throws EntityAlreadyExistsException if email already exists
     */
    public User createUser(String email, String password, String firstName,
                          String lastName, String phone, Set<Short> roleIds) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new EntityAlreadyExistsException("User with email " + email + " already exists");
        }

        User user = new User(email, passwordEncoder.encode(password), firstName, lastName);
        user.setPhone(phone);

        // Assign roles
        for (Short roleId : roleIds) {
            Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new EntityNotFoundException("Role not found: " + roleId));
            UserRole userRole = new UserRole(user, role);
            user.addRole(userRole);
        }

        return userRepository.save(user);
    }

    /**
     * Find user by ID.
     * @param userId User UUID
     * @return User entity
     * @throws EntityNotFoundException if user not found
     */
    @Transactional(readOnly = true)
    public User findById(UUID userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));
    }

    /**
     * Find user by email.
     * @param email User email
     * @return User entity
     * @throws EntityNotFoundException if user not found
     */
    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));
    }

    /**
     * Find user by email (returns Optional).
     * @param email User email
     * @return Optional containing user if found
     */
    @Transactional(readOnly = true)
    public Optional<User> findByEmailOptional(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Update user information.
     * @param userId User UUID
     * @param firstName First name
     * @param lastName Last name
     * @param phone Phone number
     * @return Updated user
     */
    public User updateUser(UUID userId, String firstName, String lastName, String phone) {
        User user = findById(userId);

        if (firstName != null) user.setFirstName(firstName);
        if (lastName != null) user.setLastName(lastName);
        if (phone != null) user.setPhone(phone);

        return userRepository.save(user);
    }

    /**
     * Change user password.
     * @param userId User UUID
     * @param newPassword New plain text password
     */
    public void changePassword(UUID userId, String newPassword) {
        User user = findById(userId);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    /**
     * Verify user password.
     * @param email User email
     * @param plainPassword Plain text password
     * @return true if password matches
     */
    @Transactional(readOnly = true)
    public boolean verifyPassword(String email, String plainPassword) {
        User user = findByEmail(email);
        return passwordEncoder.matches(plainPassword, user.getPassword());
    }

    /**
     * Deactivate user account.
     * @param userId User UUID
     */
    public void deactivateUser(UUID userId) {
        User user = findById(userId);
        user.setIsActive(false);
        userRepository.save(user);
    }

    /**
     * Activate user account.
     * @param userId User UUID
     */
    public void activateUser(UUID userId) {
        User user = findById(userId);
        user.setIsActive(true);
        userRepository.save(user);
    }

    /**
     * Get user roles as role names.
     * @param userId User UUID
     * @return Set of role names
     */
    @Transactional(readOnly = true)
    public Set<String> getUserRoleNames(UUID userId) {
        User user = findById(userId);
        return user.getUserRoles().stream()
            .map(userRole -> userRole.getRole().getRoleName())
            .collect(Collectors.toSet());
    }

    /**
     * Check if user has a specific role.
     * @param userId User UUID
     * @param roleName Role name
     * @return true if user has the role
     */
    @Transactional(readOnly = true)
    public boolean hasRole(UUID userId, String roleName) {
        return getUserRoleNames(userId).contains(roleName);
    }

    /**
     * Delete user (hard delete).
     * @param userId User UUID
     */
    public void deleteUser(UUID userId) {
        User user = findById(userId);
        userRepository.delete(user);
    }
}
