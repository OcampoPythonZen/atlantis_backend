package com.atlantis.nutritionist.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

/**
 * SupplementTracking entity representing supplement prescriptions and tracking.
 * Maps to the 'supplement_tracking' table in the database.
 *
 * @see Client
 * @see Nutriologist
 */
@Entity
@Table(name = "supplement_tracking", indexes = {
    @Index(name = "idx_supplement_client", columnList = "client_id")
})
public class SupplementTracking {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nutritionist_id", nullable = false)
    private Nutriologist nutritionist;

    @Column(name = "supplement_name", length = 255)
    private String supplementName;

    @Column(name = "supplement_type", length = 100)
    private String supplementType;

    @Column(length = 100)
    private String dosage;

    @Column(length = 100)
    private String frequency;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "reason_prescribed", columnDefinition = "TEXT")
    private String reasonPrescribed;

    @Column(name = "effectiveness_rating")
    private Integer effectivenessRating;

    @Column(name = "side_effects", columnDefinition = "TEXT")
    private String sideEffects;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    // Constructors
    public SupplementTracking() {
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Nutriologist getNutritionist() {
        return nutritionist;
    }

    public void setNutritionist(Nutriologist nutritionist) {
        this.nutritionist = nutritionist;
    }

    public String getSupplementName() {
        return supplementName;
    }

    public void setSupplementName(String supplementName) {
        this.supplementName = supplementName;
    }

    public String getSupplementType() {
        return supplementType;
    }

    public void setSupplementType(String supplementType) {
        this.supplementType = supplementType;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getReasonPrescribed() {
        return reasonPrescribed;
    }

    public void setReasonPrescribed(String reasonPrescribed) {
        this.reasonPrescribed = reasonPrescribed;
    }

    public Integer getEffectivenessRating() {
        return effectivenessRating;
    }

    public void setEffectivenessRating(Integer effectivenessRating) {
        this.effectivenessRating = effectivenessRating;
    }

    public String getSideEffects() {
        return sideEffects;
    }

    public void setSideEffects(String sideEffects) {
        this.sideEffects = sideEffects;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
