package com.atlantis.nutritionist.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

/**
 * FoodDiaryEntry entity representing daily food intake tracking.
 * Maps to the 'food_diary_entries' table in the database.
 *
 * @see Client
 */
@Entity
@Table(name = "food_diary_entries", indexes = {
    @Index(name = "idx_diary_client", columnList = "client_id"),
    @Index(name = "idx_diary_date", columnList = "entry_date")
})
public class FoodDiaryEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(name = "entry_date", nullable = false)
    private LocalDate entryDate;

    @Column(name = "meal_type", length = 50)
    private String mealType;

    @Column(name = "meal_time")
    private LocalTime mealTime;

    @Column(name = "food_description", nullable = false, columnDefinition = "TEXT")
    private String foodDescription;

    @Column(precision = 6, scale = 2)
    private BigDecimal quantity;

    @Column(length = 50)
    private String unit;

    // Macronutrients
    @Column(precision = 8, scale = 2)
    private BigDecimal calories;

    @Column(name = "protein_grams", precision = 6, scale = 2)
    private BigDecimal proteinGrams;

    @Column(name = "carbs_grams", precision = 6, scale = 2)
    private BigDecimal carbsGrams;

    @Column(name = "fats_grams", precision = 6, scale = 2)
    private BigDecimal fatsGrams;

    @Column(name = "fiber_grams", precision = 6, scale = 2)
    private BigDecimal fiberGrams;

    @Column(name = "sugar_grams", precision = 6, scale = 2)
    private BigDecimal sugarGrams;

    @Column(name = "sodium_mg", precision = 8, scale = 2)
    private BigDecimal sodiumMg;

    // Micronutrients
    @Column(name = "calcium_mg", precision = 8, scale = 2)
    private BigDecimal calciumMg;

    @Column(name = "iron_mg", precision = 6, scale = 2)
    private BigDecimal ironMg;

    @Column(name = "potassium_mg", precision = 8, scale = 2)
    private BigDecimal potassiumMg;

    @Column(name = "magnesium_mg", precision = 6, scale = 2)
    private BigDecimal magnesiumMg;

    @Column(name = "zinc_mg", precision = 6, scale = 2)
    private BigDecimal zincMg;

    @Column(name = "vitamin_c_mg", precision = 6, scale = 2)
    private BigDecimal vitaminCMg;

    @Column(name = "vitamin_a_iu", precision = 8, scale = 2)
    private BigDecimal vitaminAIu;

    @Column(name = "water_ml")
    private Integer waterMl;

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
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    // Constructors
    public FoodDiaryEntry() {
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

    public LocalDate getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
    }

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public LocalTime getMealTime() {
        return mealTime;
    }

    public void setMealTime(LocalTime mealTime) {
        this.mealTime = mealTime;
    }

    public String getFoodDescription() {
        return foodDescription;
    }

    public void setFoodDescription(String foodDescription) {
        this.foodDescription = foodDescription;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getCalories() {
        return calories;
    }

    public void setCalories(BigDecimal calories) {
        this.calories = calories;
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

    public BigDecimal getSugarGrams() {
        return sugarGrams;
    }

    public void setSugarGrams(BigDecimal sugarGrams) {
        this.sugarGrams = sugarGrams;
    }

    public BigDecimal getSodiumMg() {
        return sodiumMg;
    }

    public void setSodiumMg(BigDecimal sodiumMg) {
        this.sodiumMg = sodiumMg;
    }

    public BigDecimal getCalciumMg() {
        return calciumMg;
    }

    public void setCalciumMg(BigDecimal calciumMg) {
        this.calciumMg = calciumMg;
    }

    public BigDecimal getIronMg() {
        return ironMg;
    }

    public void setIronMg(BigDecimal ironMg) {
        this.ironMg = ironMg;
    }

    public BigDecimal getPotassiumMg() {
        return potassiumMg;
    }

    public void setPotassiumMg(BigDecimal potassiumMg) {
        this.potassiumMg = potassiumMg;
    }

    public BigDecimal getMagnesiumMg() {
        return magnesiumMg;
    }

    public void setMagnesiumMg(BigDecimal magnesiumMg) {
        this.magnesiumMg = magnesiumMg;
    }

    public BigDecimal getZincMg() {
        return zincMg;
    }

    public void setZincMg(BigDecimal zincMg) {
        this.zincMg = zincMg;
    }

    public BigDecimal getVitaminCMg() {
        return vitaminCMg;
    }

    public void setVitaminCMg(BigDecimal vitaminCMg) {
        this.vitaminCMg = vitaminCMg;
    }

    public BigDecimal getVitaminAIu() {
        return vitaminAIu;
    }

    public void setVitaminAIu(BigDecimal vitaminAIu) {
        this.vitaminAIu = vitaminAIu;
    }

    public Integer getWaterMl() {
        return waterMl;
    }

    public void setWaterMl(Integer waterMl) {
        this.waterMl = waterMl;
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
