package com.atlantis.nutritionist.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

/**
 * ClinicalNote entity representing clinical consultation notes.
 * Maps to the 'clinical_notes' table in the database.
 *
 * @see Client
 * @see Nutriologist
 */
@Entity
@Table(name = "clinical_notes", indexes = {
    @Index(name = "idx_clinical_notes_client", columnList = "client_id"),
    @Index(name = "idx_clinical_notes_date", columnList = "consultation_date")
})
public class ClinicalNote {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nutritionist_id", nullable = false)
    private Nutriologist nutritionist;

    @Column(name = "consultation_date", nullable = false)
    private Instant consultationDate;

    @Column(name = "consultation_type", length = 100)
    private String consultationType;

    @Column(name = "chief_complaint", columnDefinition = "TEXT")
    private String chiefComplaint;

    @Column(name = "clinical_assessment", columnDefinition = "TEXT")
    private String clinicalAssessment;

    @Column(name = "diagnosis_or_impression", columnDefinition = "TEXT")
    private String diagnosisOrImpression;

    @Column(name = "plan_of_action", columnDefinition = "TEXT")
    private String planOfAction;

    @Column(name = "follow_up_date")
    private LocalDate followUpDate;

    @Column(name = "weight_at_visit_kg", precision = 5, scale = 2)
    private BigDecimal weightAtVisitKg;

    @Column(name = "overall_health_status", length = 100)
    private String overallHealthStatus;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        if (consultationDate == null) {
            consultationDate = Instant.now();
        }
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    // Constructors
    public ClinicalNote() {
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

    public Instant getConsultationDate() {
        return consultationDate;
    }

    public void setConsultationDate(Instant consultationDate) {
        this.consultationDate = consultationDate;
    }

    public String getConsultationType() {
        return consultationType;
    }

    public void setConsultationType(String consultationType) {
        this.consultationType = consultationType;
    }

    public String getChiefComplaint() {
        return chiefComplaint;
    }

    public void setChiefComplaint(String chiefComplaint) {
        this.chiefComplaint = chiefComplaint;
    }

    public String getClinicalAssessment() {
        return clinicalAssessment;
    }

    public void setClinicalAssessment(String clinicalAssessment) {
        this.clinicalAssessment = clinicalAssessment;
    }

    public String getDiagnosisOrImpression() {
        return diagnosisOrImpression;
    }

    public void setDiagnosisOrImpression(String diagnosisOrImpression) {
        this.diagnosisOrImpression = diagnosisOrImpression;
    }

    public String getPlanOfAction() {
        return planOfAction;
    }

    public void setPlanOfAction(String planOfAction) {
        this.planOfAction = planOfAction;
    }

    public LocalDate getFollowUpDate() {
        return followUpDate;
    }

    public void setFollowUpDate(LocalDate followUpDate) {
        this.followUpDate = followUpDate;
    }

    public BigDecimal getWeightAtVisitKg() {
        return weightAtVisitKg;
    }

    public void setWeightAtVisitKg(BigDecimal weightAtVisitKg) {
        this.weightAtVisitKg = weightAtVisitKg;
    }

    public String getOverallHealthStatus() {
        return overallHealthStatus;
    }

    public void setOverallHealthStatus(String overallHealthStatus) {
        this.overallHealthStatus = overallHealthStatus;
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
