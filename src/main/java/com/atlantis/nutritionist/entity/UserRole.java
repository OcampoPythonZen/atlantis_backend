package com.atlantis.nutritionist.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * UserRole entity representing user-role associations.
 * Maps to the 'user_roles' table in the database.
 *
 * Uses a composite primary key (userId, roleId) implemented via @EmbeddedId.
 *
 * @see User
 * @see Role
 */
@Entity
@Table(name = "user_roles")
public class UserRole {

    @EmbeddedId
    private UserRoleId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("roleId")
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(name = "assigned_at", nullable = false, updatable = false)
    private Instant assignedAt;

    // Lifecycle callback
    @PrePersist
    protected void onCreate() {
        if (assignedAt == null) {
            assignedAt = Instant.now();
        }
    }

    // Constructors
    public UserRole() {
    }

    public UserRole(User user, Role role) {
        this.user = user;
        this.role = role;
        this.id = new UserRoleId(user.getId(), role.getId());
    }

    // Getters and Setters
    public UserRoleId getId() {
        return id;
    }

    public void setId(UserRoleId id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        if (this.id == null) {
            this.id = new UserRoleId();
        }
        if (user != null) {
            this.id.setUserId(user.getId());
        }
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
        if (this.id == null) {
            this.id = new UserRoleId();
        }
        if (role != null) {
            this.id.setRoleId(role.getId());
        }
    }

    public Instant getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(Instant assignedAt) {
        this.assignedAt = assignedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserRole)) return false;
        UserRole userRole = (UserRole) o;
        return id != null && id.equals(userRole.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "UserRole{" +
                "userId=" + (id != null ? id.getUserId() : null) +
                ", roleId=" + (id != null ? id.getRoleId() : null) +
                ", assignedAt=" + assignedAt +
                '}';
    }

    /**
     * Composite primary key for UserRole entity.
     * Consists of userId and roleId.
     */
    @Embeddable
    public static class UserRoleId implements Serializable {

        @Column(name = "user_id")
        private UUID userId;

        @Column(name = "role_id")
        private Short roleId;

        // Constructors
        public UserRoleId() {
        }

        public UserRoleId(UUID userId, Short roleId) {
            this.userId = userId;
            this.roleId = roleId;
        }

        // Getters and Setters
        public UUID getUserId() {
            return userId;
        }

        public void setUserId(UUID userId) {
            this.userId = userId;
        }

        public Short getRoleId() {
            return roleId;
        }

        public void setRoleId(Short roleId) {
            this.roleId = roleId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof UserRoleId)) return false;
            UserRoleId that = (UserRoleId) o;
            return Objects.equals(userId, that.userId) &&
                    Objects.equals(roleId, that.roleId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(userId, roleId);
        }

        @Override
        public String toString() {
            return "UserRoleId{" +
                    "userId=" + userId +
                    ", roleId=" + roleId +
                    '}';
        }
    }
}
