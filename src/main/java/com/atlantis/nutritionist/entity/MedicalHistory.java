package com.atlantis.nutritionist.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

/**
 * MedicalHistory entity representing comprehensive medical history.
 * Maps to the 'medical_history' table in the database.
 *
 * @see Client
 */
@Entity
@Table(name = "medical_history")
public class MedicalHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false, unique = true)
    private Client client;

    // Chronic conditions - Diabetes
    @Column(name = "has_diabetes")
    private Boolean hasDiabetes = false;

    @Column(name = "diabetes_type", length = 50)
    private String diabetesType;

    @Column(name = "diabetes_diagnosis_date")
    private LocalDate diabetesDiagnosisDate;

    // Hypertension
    @Column(name = "has_hypertension")
    private Boolean hasHypertension = false;

    @Column(name = "hypertension_diagnosis_date")
    private LocalDate hypertensionDiagnosisDate;

    // Thyroid
    @Column(name = "has_thyroid_disorder")
    private Boolean hasThyroidDisorder = false;

    @Column(name = "thyroid_condition", length = 100)
    private String thyroidCondition;

    // Heart disease
    @Column(name = "has_heart_disease")
    private Boolean hasHeartDisease = false;

    @Column(name = "heart_condition_details", columnDefinition = "TEXT")
    private String heartConditionDetails;

    // Kidney disease
    @Column(name = "has_kidney_disease")
    private Boolean hasKidneyDisease = false;

    @Column(name = "kidney_condition_details", columnDefinition = "TEXT")
    private String kidneyConditionDetails;

    // Liver disease
    @Column(name = "has_liver_disease")
    private Boolean hasLiverDisease = false;

    @Column(name = "liver_condition_details", columnDefinition = "TEXT")
    private String liverConditionDetails;

    // Gastrointestinal issues
    @Column(name = "has_gastrointestinal_issues")
    private Boolean hasGastrointestinalIssues = false;

    @Column(name = "gi_condition_details", columnDefinition = "TEXT")
    private String giConditionDetails;

    // Autoimmune disease
    @Column(name = "has_autoimmune_disease")
    private Boolean hasAutoimmuneDisease = false;

    @Column(name = "autoimmune_type", length = 100)
    private String autoimmuneType;

    // Mental health
    @Column(name = "has_mental_health_condition")
    private Boolean hasMentalHealthCondition = false;

    @Column(name = "mental_health_details", columnDefinition = "TEXT")
    private String mentalHealthDetails;

    // Cancer history
    @Column(name = "has_cancer_history")
    private Boolean hasCancerHistory = false;

    @Column(name = "cancer_details", columnDefinition = "TEXT")
    private String cancerDetails;

    @Column(name = "cancer_remission_date")
    private LocalDate cancerRemissionDate;

    // Surgeries
    @Column(name = "surgical_history", columnDefinition = "TEXT")
    private String surgicalHistory;

    // Current medications
    @Column(name = "current_medications", columnDefinition = "TEXT")
    private String currentMedications;

    @Column(name = "medication_last_updated")
    private Instant medicationLastUpdated;

    // Allergies
    @Column(name = "food_allergies", columnDefinition = "TEXT")
    private String foodAllergies;

    @Column(name = "medication_allergies", columnDefinition = "TEXT")
    private String medicationAllergies;

    @Column(name = "environmental_allergies", columnDefinition = "TEXT")
    private String environmentalAllergies;

    // Habits
    @Column(name = "smoking_status", length = 50)
    private String smokingStatus;

    @Column(name = "alcohol_consumption", length = 50)
    private String alcoholConsumption;

    @Column(name = "drug_use_history", length = 50)
    private String drugUseHistory;

    // Physical activity
    @Column(name = "exercise_frequency", length = 50)
    private String exerciseFrequency;

    @Column(name = "exercise_type", length = 255)
    private String exerciseType;

    @Column(name = "hours_sleep_per_night", precision = 3, scale = 1)
    private BigDecimal hoursSleepPerNight;

    // Family history
    @Column(name = "family_history_diabetes")
    private Boolean familyHistoryDiabetes = false;

    @Column(name = "family_history_hypertension")
    private Boolean familyHistoryHypertension = false;

    @Column(name = "family_history_heart_disease")
    private Boolean familyHistoryHeartDisease = false;

    @Column(name = "family_history_cancer")
    private Boolean familyHistoryCancer = false;

    @Column(name = "family_history_obesity")
    private Boolean familyHistoryObesity = false;

    @Column(name = "family_history_notes", columnDefinition = "TEXT")
    private String familyHistoryNotes;

    // Gynecological (if applicable)
    @Column(name = "menstrual_cycle_regular")
    private Boolean menstrualCycleRegular;

    @Column(name = "menopausal_status", length = 50)
    private String menopausalStatus;

    @Column(name = "contraceptive_method", length = 100)
    private String contraceptiveMethod;

    @Column(name = "pregnant")
    private Boolean pregnant = false;

    @Column(name = "breastfeeding")
    private Boolean breastfeeding = false;

    // Digestion and GI
    @Column(name = "ibs_diagnosis")
    private Boolean ibsDiagnosis = false;

    @Column(name = "celiac_disease")
    private Boolean celiacDisease = false;

    @Column(name = "lactose_intolerance")
    private Boolean lactoseIntolerance = false;

    @Column(name = "gluten_sensitivity")
    private Boolean glutenSensitivity = false;

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
    public MedicalHistory() {
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

    public Boolean getHasDiabetes() {
        return hasDiabetes;
    }

    public void setHasDiabetes(Boolean hasDiabetes) {
        this.hasDiabetes = hasDiabetes;
    }

    public String getDiabetesType() {
        return diabetesType;
    }

    public void setDiabetesType(String diabetesType) {
        this.diabetesType = diabetesType;
    }

    public LocalDate getDiabetesDiagnosisDate() {
        return diabetesDiagnosisDate;
    }

    public void setDiabetesDiagnosisDate(LocalDate diabetesDiagnosisDate) {
        this.diabetesDiagnosisDate = diabetesDiagnosisDate;
    }

    public Boolean getHasHypertension() {
        return hasHypertension;
    }

    public void setHasHypertension(Boolean hasHypertension) {
        this.hasHypertension = hasHypertension;
    }

    public LocalDate getHypertensionDiagnosisDate() {
        return hypertensionDiagnosisDate;
    }

    public void setHypertensionDiagnosisDate(LocalDate hypertensionDiagnosisDate) {
        this.hypertensionDiagnosisDate = hypertensionDiagnosisDate;
    }

    public Boolean getHasThyroidDisorder() {
        return hasThyroidDisorder;
    }

    public void setHasThyroidDisorder(Boolean hasThyroidDisorder) {
        this.hasThyroidDisorder = hasThyroidDisorder;
    }

    public String getThyroidCondition() {
        return thyroidCondition;
    }

    public void setThyroidCondition(String thyroidCondition) {
        this.thyroidCondition = thyroidCondition;
    }

    public Boolean getHasHeartDisease() {
        return hasHeartDisease;
    }

    public void setHasHeartDisease(Boolean hasHeartDisease) {
        this.hasHeartDisease = hasHeartDisease;
    }

    public String getHeartConditionDetails() {
        return heartConditionDetails;
    }

    public void setHeartConditionDetails(String heartConditionDetails) {
        this.heartConditionDetails = heartConditionDetails;
    }

    public Boolean getHasKidneyDisease() {
        return hasKidneyDisease;
    }

    public void setHasKidneyDisease(Boolean hasKidneyDisease) {
        this.hasKidneyDisease = hasKidneyDisease;
    }

    public String getKidneyConditionDetails() {
        return kidneyConditionDetails;
    }

    public void setKidneyConditionDetails(String kidneyConditionDetails) {
        this.kidneyConditionDetails = kidneyConditionDetails;
    }

    public Boolean getHasLiverDisease() {
        return hasLiverDisease;
    }

    public void setHasLiverDisease(Boolean hasLiverDisease) {
        this.hasLiverDisease = hasLiverDisease;
    }

    public String getLiverConditionDetails() {
        return liverConditionDetails;
    }

    public void setLiverConditionDetails(String liverConditionDetails) {
        this.liverConditionDetails = liverConditionDetails;
    }

    public Boolean getHasGastrointestinalIssues() {
        return hasGastrointestinalIssues;
    }

    public void setHasGastrointestinalIssues(Boolean hasGastrointestinalIssues) {
        this.hasGastrointestinalIssues = hasGastrointestinalIssues;
    }

    public String getGiConditionDetails() {
        return giConditionDetails;
    }

    public void setGiConditionDetails(String giConditionDetails) {
        this.giConditionDetails = giConditionDetails;
    }

    public Boolean getHasAutoimmuneDisease() {
        return hasAutoimmuneDisease;
    }

    public void setHasAutoimmuneDisease(Boolean hasAutoimmuneDisease) {
        this.hasAutoimmuneDisease = hasAutoimmuneDisease;
    }

    public String getAutoimmuneType() {
        return autoimmuneType;
    }

    public void setAutoimmuneType(String autoimmuneType) {
        this.autoimmuneType = autoimmuneType;
    }

    public Boolean getHasMentalHealthCondition() {
        return hasMentalHealthCondition;
    }

    public void setHasMentalHealthCondition(Boolean hasMentalHealthCondition) {
        this.hasMentalHealthCondition = hasMentalHealthCondition;
    }

    public String getMentalHealthDetails() {
        return mentalHealthDetails;
    }

    public void setMentalHealthDetails(String mentalHealthDetails) {
        this.mentalHealthDetails = mentalHealthDetails;
    }

    public Boolean getHasCancerHistory() {
        return hasCancerHistory;
    }

    public void setHasCancerHistory(Boolean hasCancerHistory) {
        this.hasCancerHistory = hasCancerHistory;
    }

    public String getCancerDetails() {
        return cancerDetails;
    }

    public void setCancerDetails(String cancerDetails) {
        this.cancerDetails = cancerDetails;
    }

    public LocalDate getCancerRemissionDate() {
        return cancerRemissionDate;
    }

    public void setCancerRemissionDate(LocalDate cancerRemissionDate) {
        this.cancerRemissionDate = cancerRemissionDate;
    }

    public String getSurgicalHistory() {
        return surgicalHistory;
    }

    public void setSurgicalHistory(String surgicalHistory) {
        this.surgicalHistory = surgicalHistory;
    }

    public String getCurrentMedications() {
        return currentMedications;
    }

    public void setCurrentMedications(String currentMedications) {
        this.currentMedications = currentMedications;
    }

    public Instant getMedicationLastUpdated() {
        return medicationLastUpdated;
    }

    public void setMedicationLastUpdated(Instant medicationLastUpdated) {
        this.medicationLastUpdated = medicationLastUpdated;
    }

    public String getFoodAllergies() {
        return foodAllergies;
    }

    public void setFoodAllergies(String foodAllergies) {
        this.foodAllergies = foodAllergies;
    }

    public String getMedicationAllergies() {
        return medicationAllergies;
    }

    public void setMedicationAllergies(String medicationAllergies) {
        this.medicationAllergies = medicationAllergies;
    }

    public String getEnvironmentalAllergies() {
        return environmentalAllergies;
    }

    public void setEnvironmentalAllergies(String environmentalAllergies) {
        this.environmentalAllergies = environmentalAllergies;
    }

    public String getSmokingStatus() {
        return smokingStatus;
    }

    public void setSmokingStatus(String smokingStatus) {
        this.smokingStatus = smokingStatus;
    }

    public String getAlcoholConsumption() {
        return alcoholConsumption;
    }

    public void setAlcoholConsumption(String alcoholConsumption) {
        this.alcoholConsumption = alcoholConsumption;
    }

    public String getDrugUseHistory() {
        return drugUseHistory;
    }

    public void setDrugUseHistory(String drugUseHistory) {
        this.drugUseHistory = drugUseHistory;
    }

    public String getExerciseFrequency() {
        return exerciseFrequency;
    }

    public void setExerciseFrequency(String exerciseFrequency) {
        this.exerciseFrequency = exerciseFrequency;
    }

    public String getExerciseType() {
        return exerciseType;
    }

    public void setExerciseType(String exerciseType) {
        this.exerciseType = exerciseType;
    }

    public BigDecimal getHoursSleepPerNight() {
        return hoursSleepPerNight;
    }

    public void setHoursSleepPerNight(BigDecimal hoursSleepPerNight) {
        this.hoursSleepPerNight = hoursSleepPerNight;
    }

    public Boolean getFamilyHistoryDiabetes() {
        return familyHistoryDiabetes;
    }

    public void setFamilyHistoryDiabetes(Boolean familyHistoryDiabetes) {
        this.familyHistoryDiabetes = familyHistoryDiabetes;
    }

    public Boolean getFamilyHistoryHypertension() {
        return familyHistoryHypertension;
    }

    public void setFamilyHistoryHypertension(Boolean familyHistoryHypertension) {
        this.familyHistoryHypertension = familyHistoryHypertension;
    }

    public Boolean getFamilyHistoryHeartDisease() {
        return familyHistoryHeartDisease;
    }

    public void setFamilyHistoryHeartDisease(Boolean familyHistoryHeartDisease) {
        this.familyHistoryHeartDisease = familyHistoryHeartDisease;
    }

    public Boolean getFamilyHistoryCancer() {
        return familyHistoryCancer;
    }

    public void setFamilyHistoryCancer(Boolean familyHistoryCancer) {
        this.familyHistoryCancer = familyHistoryCancer;
    }

    public Boolean getFamilyHistoryObesity() {
        return familyHistoryObesity;
    }

    public void setFamilyHistoryObesity(Boolean familyHistoryObesity) {
        this.familyHistoryObesity = familyHistoryObesity;
    }

    public String getFamilyHistoryNotes() {
        return familyHistoryNotes;
    }

    public void setFamilyHistoryNotes(String familyHistoryNotes) {
        this.familyHistoryNotes = familyHistoryNotes;
    }

    public Boolean getMenstrualCycleRegular() {
        return menstrualCycleRegular;
    }

    public void setMenstrualCycleRegular(Boolean menstrualCycleRegular) {
        this.menstrualCycleRegular = menstrualCycleRegular;
    }

    public String getMenopausalStatus() {
        return menopausalStatus;
    }

    public void setMenopausalStatus(String menopausalStatus) {
        this.menopausalStatus = menopausalStatus;
    }

    public String getContraceptiveMethod() {
        return contraceptiveMethod;
    }

    public void setContraceptiveMethod(String contraceptiveMethod) {
        this.contraceptiveMethod = contraceptiveMethod;
    }

    public Boolean getPregnant() {
        return pregnant;
    }

    public void setPregnant(Boolean pregnant) {
        this.pregnant = pregnant;
    }

    public Boolean getBreastfeeding() {
        return breastfeeding;
    }

    public void setBreastfeeding(Boolean breastfeeding) {
        this.breastfeeding = breastfeeding;
    }

    public Boolean getIbsDiagnosis() {
        return ibsDiagnosis;
    }

    public void setIbsDiagnosis(Boolean ibsDiagnosis) {
        this.ibsDiagnosis = ibsDiagnosis;
    }

    public Boolean getCeliacDisease() {
        return celiacDisease;
    }

    public void setCeliacDisease(Boolean celiacDisease) {
        this.celiacDisease = celiacDisease;
    }

    public Boolean getLactoseIntolerance() {
        return lactoseIntolerance;
    }

    public void setLactoseIntolerance(Boolean lactoseIntolerance) {
        this.lactoseIntolerance = lactoseIntolerance;
    }

    public Boolean getGlutenSensitivity() {
        return glutenSensitivity;
    }

    public void setGlutenSensitivity(Boolean glutenSensitivity) {
        this.glutenSensitivity = glutenSensitivity;
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
