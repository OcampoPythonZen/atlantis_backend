package com.atlantis.nutritionist.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

/**
 * NutritionPlan entity representing personalized nutrition plans.
 * Maps to the 'nutrition_plans' table in the database.
 *
 * @see Client
 * @see Nutriologist
 */
@Entity
@Table(name = "nutrition_plans", indexes = {
    @Index(name = "idx_plan_client", columnList = "client_id"),
    @Index(name = "idx_plan_active", columnList = "is_active")
})
public class NutritionPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nutritionist_id", nullable = false)
    private Nutriologist nutritionist;

    @Column(name = "plan_name", length = 255)
    private String planName;

    // Macronutrients
    @Column(name = "calories_target")
    private Integer caloriesTarget;

    @Column(name = "protein_grams", precision = 6, scale = 2)
    private BigDecimal proteinGrams;

    @Column(name = "carbs_grams", precision = 6, scale = 2)
    private BigDecimal carbsGrams;

    @Column(name = "fats_grams", precision = 6, scale = 2)
    private BigDecimal fatsGrams;

    @Column(name = "fiber_grams", precision = 6, scale = 2)
    private BigDecimal fiberGrams;

    // Medical restrictions
    @Column(name = "foods_to_avoid", columnDefinition = "TEXT")
    private String foodsToAvoid;

    @Column(name = "foods_to_limit", columnDefinition = "TEXT")
    private String foodsToLimit;

    @Column(name = "foods_recommended", columnDefinition = "TEXT")
    private String foodsRecommended;

    // Clinical
    @Column(name = "plan_type", length = 100)
    private String planType;

    @Column(name = "medical_condition_target", length = 255)
    private String medicalConditionTarget;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
        if (isActive == null) {
            isActive = true;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    // Constructors
    public NutritionPlan() {
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

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public Integer getCaloriesTarget() {
        return caloriesTarget;
    }

    public void setCaloriesTarget(Integer caloriesTarget) {
        this.caloriesTarget = caloriesTarget;
    }

    public BigDecimal getProteinGrams() {
        return proteinGrams;
    }

    public void setProteinGrams(BigDecimal proteinGrams) {
        this.proteinGrams = proteinGrams;
    }

    public BigDecimal getCarbsGrams() {
        return carbsGrams;
    }

    public void setCarbsGrams(BigDecimal carbsGrams) {
        this.carbsGrams = carbsGrams;
    }

    public BigDecimal getFatsGrams() {
        return fatsGrams;
    }

    public void setFatsGrams(BigDecimal fatsGrams) {
        this.fatsGrams = fatsGrams;
    }

    public BigDecimal getFiberGrams() {
        return fiberGrams;
    }

    public void setFiberGrams(BigDecimal fiberGrams) {
        this.fiberGrams = fiberGrams;
    }

    public String getFoodsToAvoid() {
        return foodsToAvoid;
    }

    public void setFoodsToAvoid(String foodsToAvoid) {
        this.foodsToAvoid = foodsToAvoid;
    }

    public String getFoodsToLimit() {
        return foodsToLimit;
    }

    public void setFoodsToLimit(String foodsToLimit) {
        this.foodsToLimit = foodsToLimit;
    }

    public String getFoodsRecommended() {
        return foodsRecommended;
    }

    public void setFoodsRecommended(String foodsRecommended) {
        this.foodsRecommended = foodsRecommended;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public String getMedicalConditionTarget() {
        return medicalConditionTarget;
    }

    public void setMedicalConditionTarget(String medicalConditionTarget) {
        this.medicalConditionTarget = medicalConditionTarget;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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
