package com.atlantis.nutritionist.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

/**
 * SymptomLog entity representing symptom tracking over time.
 * Maps to the 'symptom_logs' table in the database.
 *
 * @see Client
 */
@Entity
@Table(name = "symptom_logs", indexes = {
    @Index(name = "idx_symptom_client", columnList = "client_id"),
    @Index(name = "idx_symptom_date", columnList = "log_date")
})
public class SymptomLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(name = "log_date", nullable = false)
    private Instant logDate;

    @Column(name = "symptom_name", length = 255)
    private String symptomName;

    @Column(name = "severity_level")
    private Integer severityLevel;

    @Column(name = "symptom_description", columnDefinition = "TEXT")
    private String symptomDescription;

    // Context
    @Column(name = "associated_food", length = 255)
    private String associatedFood;

    @Column(name = "time_of_day", length = 50)
    private String timeOfDay;

    @Column(name = "duration_hours")
    private Integer durationHours;

    @Column(name = "trigger_suspected", length = 255)
    private String triggerSuspected;

    // Response
    @Column(name = "medication_taken", length = 255)
    private String medicationTaken;

    @Column(name = "relief_level")
    private Integer reliefLevel;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        if (logDate == null) {
            logDate = Instant.now();
        }
        createdAt = Instant.now();
    }

    // Constructors
    public SymptomLog() {
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

    public Instant getLogDate() {
        return logDate;
    }

    public void setLogDate(Instant logDate) {
        this.logDate = logDate;
    }

    public String getSymptomName() {
        return symptomName;
    }

    public void setSymptomName(String symptomName) {
        this.symptomName = symptomName;
    }

    public Integer getSeverityLevel() {
        return severityLevel;
    }

    public void setSeverityLevel(Integer severityLevel) {
        this.severityLevel = severityLevel;
    }

    public String getSymptomDescription() {
        return symptomDescription;
    }

    public void setSymptomDescription(String symptomDescription) {
        this.symptomDescription = symptomDescription;
    }

    public String getAssociatedFood() {
        return associatedFood;
    }

    public void setAssociatedFood(String associatedFood) {
        this.associatedFood = associatedFood;
    }

    public String getTimeOfDay() {
        return timeOfDay;
    }

    public void setTimeOfDay(String timeOfDay) {
        this.timeOfDay = timeOfDay;
    }

    public Integer getDurationHours() {
        return durationHours;
    }

    public void setDurationHours(Integer durationHours) {
        this.durationHours = durationHours;
    }

    public String getTriggerSuspected() {
        return triggerSuspected;
    }

    public void setTriggerSuspected(String triggerSuspected) {
        this.triggerSuspected = triggerSuspected;
    }

    public String getMedicationTaken() {
        return medicationTaken;
    }

    public void setMedicationTaken(String medicationTaken) {
        this.medicationTaken = medicationTaken;
    }

    public Integer getReliefLevel() {
        return reliefLevel;
    }

    public void setReliefLevel(Integer reliefLevel) {
        this.reliefLevel = reliefLevel;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
