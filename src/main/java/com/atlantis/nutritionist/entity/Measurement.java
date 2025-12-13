package com.atlantis.nutritionist.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Measurement entity representing biometric measurements and lab results.
 * Maps to the 'measurements' table in the database.
 *
 * Contains extensive biometric data including anthropometric measurements,
 * body composition, blood pressure, metabolic parameters, lipid profile,
 * liver and kidney function, electrolytes, minerals, and hormones.
 *
 * @see Client
 * @see Nutriologist
 */
@Entity
@Table(name = "measurements", indexes = {
    @Index(name = "idx_measurement_client", columnList = "client_id"),
    @Index(name = "idx_measurement_date", columnList = "measurement_date"),
    @Index(name = "idx_measurement_nutritionist", columnList = "nutritionist_id")
})
public class Measurement {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nutritionist_id", nullable = false)
    private Nutriologist nutritionist;

    @Column(name = "measurement_date", nullable = false)
    private Instant measurementDate;

    // Basic anthropometric measurements
    @Column(name = "weight_kg", precision = 5, scale = 2)
    private BigDecimal weightKg;

    @Column(name = "height_cm", precision = 5, scale = 2)
    private BigDecimal heightCm;

    @Column(name = "imc", precision = 4, scale = 2)
    private BigDecimal imc;

    // Body composition (bioimpedance)
    @Column(name = "body_fat_percentage", precision = 5, scale = 2)
    private BigDecimal bodyFatPercentage;

    @Column(name = "muscle_mass_kg", precision = 5, scale = 2)
    private BigDecimal muscleMassKg;

    @Column(name = "visceral_fat_percentage", precision = 5, scale = 2)
    private BigDecimal visceralFatPercentage;

    @Column(name = "subcutaneous_fat_percentage", precision = 5, scale = 2)
    private BigDecimal subcutaneousFatPercentage;

    @Column(name = "water_percentage", precision = 5, scale = 2)
    private BigDecimal waterPercentage;

    @Column(name = "bone_mass_kg", precision = 5, scale = 2)
    private BigDecimal boneMassKg;

    @Column(name = "metabolic_age")
    private Integer metabolicAge;

    @Column(name = "basal_metabolic_rate")
    private Integer basalMetabolicRate;

    // Body circumferences
    @Column(name = "waist_cm", precision = 5, scale = 2)
    private BigDecimal waistCm;

    @Column(name = "hip_cm", precision = 5, scale = 2)
    private BigDecimal hipCm;

    @Column(name = "waist_hip_ratio", precision = 4, scale = 2)
    private BigDecimal waistHipRatio;

    @Column(name = "chest_cm", precision = 5, scale = 2)
    private BigDecimal chestCm;

    @Column(name = "arm_cm", precision = 5, scale = 2)
    private BigDecimal armCm;

    @Column(name = "thigh_cm", precision = 5, scale = 2)
    private BigDecimal thighCm;

    // Blood pressure
    @Column(name = "blood_pressure_systolic")
    private Integer bloodPressureSystolic;

    @Column(name = "blood_pressure_diastolic")
    private Integer bloodPressureDiastolic;

    @Column(name = "blood_pressure_notes", length = 255)
    private String bloodPressureNotes;

    // Cardiovascular parameters
    @Column(name = "heart_rate_bpm")
    private Integer heartRateBpm;

    @Column(name = "resting_heart_rate")
    private Integer restingHeartRate;

    // Metabolic parameters (glucose)
    @Column(name = "fasting_glucose_mg_dl", precision = 6, scale = 2)
    private BigDecimal fastingGlucoseMgDl;

    @Column(name = "glucose_2hr_post_meal", precision = 6, scale = 2)
    private BigDecimal glucose2hrPostMeal;

    // Lipid profile
    @Column(name = "total_cholesterol_mg_dl", precision = 6, scale = 2)
    private BigDecimal totalCholesterolMgDl;

    @Column(name = "ldl_cholesterol_mg_dl", precision = 6, scale = 2)
    private BigDecimal ldlCholesterolMgDl;

    @Column(name = "hdl_cholesterol_mg_dl", precision = 6, scale = 2)
    private BigDecimal hdlCholesterolMgDl;

    @Column(name = "triglycerides_mg_dl", precision = 6, scale = 2)
    private BigDecimal triglyceridesMgDl;

    // Liver function
    @Column(name = "ast_units_l", precision = 6, scale = 2)
    private BigDecimal astUnitsL;

    @Column(name = "alt_units_l", precision = 6, scale = 2)
    private BigDecimal altUnitsL;

    @Column(name = "alkaline_phosphatase", precision = 6, scale = 2)
    private BigDecimal alkalinePhosphatase;

    @Column(name = "total_bilirubin", precision = 6, scale = 2)
    private BigDecimal totalBilirubin;

    // Kidney function
    @Column(name = "creatinine_mg_dl", precision = 6, scale = 2)
    private BigDecimal creatinineMgDl;

    @Column(name = "bun_mg_dl", precision = 6, scale = 2)
    private BigDecimal bunMgDl;

    @Column(name = "gfr_ml_min", precision = 6, scale = 2)
    private BigDecimal gfrMlMin;

    // Electrolytes
    @Column(name = "sodium_mmol_l", precision = 6, scale = 2)
    private BigDecimal sodiumMmolL;

    @Column(name = "potassium_mmol_l", precision = 6, scale = 2)
    private BigDecimal potassiumMmolL;

    @Column(name = "chloride_mmol_l", precision = 6, scale = 2)
    private BigDecimal chlorideMmolL;

    @Column(name = "calcium_mg_dl", precision = 6, scale = 2)
    private BigDecimal calciumMgDl;

    @Column(name = "magnesium_mg_dl", precision = 6, scale = 2)
    private BigDecimal magnesiumMgDl;

    @Column(name = "phosphorus_mg_dl", precision = 6, scale = 2)
    private BigDecimal phosphorusMgDl;

    // Minerals and micronutrients
    @Column(name = "iron_mcg_dl", precision = 6, scale = 2)
    private BigDecimal ironMcgDl;

    @Column(name = "ferritin_ng_ml", precision = 6, scale = 2)
    private BigDecimal ferritinNgMl;

    @Column(name = "zinc_mcg_dl", precision = 6, scale = 2)
    private BigDecimal zincMcgDl;

    @Column(name = "vitamin_d_ng_ml", precision = 6, scale = 2)
    private BigDecimal vitaminDNgMl;

    @Column(name = "vitamin_b12_pg_ml", precision = 6, scale = 2)
    private BigDecimal vitaminB12PgMl;

    @Column(name = "folate_ng_ml", precision = 6, scale = 2)
    private BigDecimal folateNgMl;

    // Inflammation and others
    @Column(name = "c_reactive_protein_mg_l", precision = 6, scale = 2)
    private BigDecimal cReactiveProteinMgL;

    @Column(name = "hemoglobin_a1c_percent", precision = 4, scale = 2)
    private BigDecimal hemoglobinA1cPercent;

    // Hormones
    @Column(name = "tsh_miu_l", precision = 6, scale = 2)
    private BigDecimal tshMiuL;

    @Column(name = "t3_pg_ml", precision = 6, scale = 2)
    private BigDecimal t3PgMl;

    @Column(name = "t4_ng_dl", precision = 6, scale = 2)
    private BigDecimal t4NgDl;

    @Column(name = "cortisol_mcg_dl", precision = 6, scale = 2)
    private BigDecimal cortisolMcgDl;

    // Metadata
    @Column(name = "measurement_device", length = 100)
    private String measurementDevice;

    @Column(name = "lab_name", length = 255)
    private String labName;

    @Column(name = "measurement_location", length = 100)
    private String measurementLocation;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        if (measurementDate == null) {
            measurementDate = Instant.now();
        }
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    // Constructors
    public Measurement() {
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

    public Instant getMeasurementDate() {
        return measurementDate;
    }

    public void setMeasurementDate(Instant measurementDate) {
        this.measurementDate = measurementDate;
    }

    public BigDecimal getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(BigDecimal weightKg) {
        this.weightKg = weightKg;
    }

    public BigDecimal getHeightCm() {
        return heightCm;
    }

    public void setHeightCm(BigDecimal heightCm) {
        this.heightCm = heightCm;
    }

    public BigDecimal getImc() {
        return imc;
    }

    public void setImc(BigDecimal imc) {
        this.imc = imc;
    }

    public BigDecimal getBodyFatPercentage() {
        return bodyFatPercentage;
    }

    public void setBodyFatPercentage(BigDecimal bodyFatPercentage) {
        this.bodyFatPercentage = bodyFatPercentage;
    }

    public BigDecimal getMuscleMassKg() {
        return muscleMassKg;
    }

    public void setMuscleMassKg(BigDecimal muscleMassKg) {
        this.muscleMassKg = muscleMassKg;
    }

    public BigDecimal getVisceralFatPercentage() {
        return visceralFatPercentage;
    }

    public void setVisceralFatPercentage(BigDecimal visceralFatPercentage) {
        this.visceralFatPercentage = visceralFatPercentage;
    }

    public BigDecimal getSubcutaneousFatPercentage() {
        return subcutaneousFatPercentage;
    }

    public void setSubcutaneousFatPercentage(BigDecimal subcutaneousFatPercentage) {
        this.subcutaneousFatPercentage = subcutaneousFatPercentage;
    }

    public BigDecimal getWaterPercentage() {
        return waterPercentage;
    }

    public void setWaterPercentage(BigDecimal waterPercentage) {
        this.waterPercentage = waterPercentage;
    }

    public BigDecimal getBoneMassKg() {
        return boneMassKg;
    }

    public void setBoneMassKg(BigDecimal boneMassKg) {
        this.boneMassKg = boneMassKg;
    }

    public Integer getMetabolicAge() {
        return metabolicAge;
    }

    public void setMetabolicAge(Integer metabolicAge) {
        this.metabolicAge = metabolicAge;
    }

    public Integer getBasalMetabolicRate() {
        return basalMetabolicRate;
    }

    public void setBasalMetabolicRate(Integer basalMetabolicRate) {
        this.basalMetabolicRate = basalMetabolicRate;
    }

    public BigDecimal getWaistCm() {
        return waistCm;
    }

    public void setWaistCm(BigDecimal waistCm) {
        this.waistCm = waistCm;
    }

    public BigDecimal getHipCm() {
        return hipCm;
    }

    public void setHipCm(BigDecimal hipCm) {
        this.hipCm = hipCm;
    }

    public BigDecimal getWaistHipRatio() {
        return waistHipRatio;
    }

    public void setWaistHipRatio(BigDecimal waistHipRatio) {
        this.waistHipRatio = waistHipRatio;
    }

    public BigDecimal getChestCm() {
        return chestCm;
    }

    public void setChestCm(BigDecimal chestCm) {
        this.chestCm = chestCm;
    }

    public BigDecimal getArmCm() {
        return armCm;
    }

    public void setArmCm(BigDecimal armCm) {
        this.armCm = armCm;
    }

    public BigDecimal getThighCm() {
        return thighCm;
    }

    public void setThighCm(BigDecimal thighCm) {
        this.thighCm = thighCm;
    }

    public Integer getBloodPressureSystolic() {
        return bloodPressureSystolic;
    }

    public void setBloodPressureSystolic(Integer bloodPressureSystolic) {
        this.bloodPressureSystolic = bloodPressureSystolic;
    }

    public Integer getBloodPressureDiastolic() {
        return bloodPressureDiastolic;
    }

    public void setBloodPressureDiastolic(Integer bloodPressureDiastolic) {
        this.bloodPressureDiastolic = bloodPressureDiastolic;
    }

    public String getBloodPressureNotes() {
        return bloodPressureNotes;
    }

    public void setBloodPressureNotes(String bloodPressureNotes) {
        this.bloodPressureNotes = bloodPressureNotes;
    }

    public Integer getHeartRateBpm() {
        return heartRateBpm;
    }

    public void setHeartRateBpm(Integer heartRateBpm) {
        this.heartRateBpm = heartRateBpm;
    }

    public Integer getRestingHeartRate() {
        return restingHeartRate;
    }

    public void setRestingHeartRate(Integer restingHeartRate) {
        this.restingHeartRate = restingHeartRate;
    }

    public BigDecimal getFastingGlucoseMgDl() {
        return fastingGlucoseMgDl;
    }

    public void setFastingGlucoseMgDl(BigDecimal fastingGlucoseMgDl) {
        this.fastingGlucoseMgDl = fastingGlucoseMgDl;
    }

    public BigDecimal getGlucose2hrPostMeal() {
        return glucose2hrPostMeal;
    }

    public void setGlucose2hrPostMeal(BigDecimal glucose2hrPostMeal) {
        this.glucose2hrPostMeal = glucose2hrPostMeal;
    }

    public BigDecimal getTotalCholesterolMgDl() {
        return totalCholesterolMgDl;
    }

    public void setTotalCholesterolMgDl(BigDecimal totalCholesterolMgDl) {
        this.totalCholesterolMgDl = totalCholesterolMgDl;
    }

    public BigDecimal getLdlCholesterolMgDl() {
        return ldlCholesterolMgDl;
    }

    public void setLdlCholesterolMgDl(BigDecimal ldlCholesterolMgDl) {
        this.ldlCholesterolMgDl = ldlCholesterolMgDl;
    }

    public BigDecimal getHdlCholesterolMgDl() {
        return hdlCholesterolMgDl;
    }

    public void setHdlCholesterolMgDl(BigDecimal hdlCholesterolMgDl) {
        this.hdlCholesterolMgDl = hdlCholesterolMgDl;
    }

    public BigDecimal getTriglyceridesMgDl() {
        return triglyceridesMgDl;
    }

    public void setTriglyceridesMgDl(BigDecimal triglyceridesMgDl) {
        this.triglyceridesMgDl = triglyceridesMgDl;
    }

    public BigDecimal getAstUnitsL() {
        return astUnitsL;
    }

    public void setAstUnitsL(BigDecimal astUnitsL) {
        this.astUnitsL = astUnitsL;
    }

    public BigDecimal getAltUnitsL() {
        return altUnitsL;
    }

    public void setAltUnitsL(BigDecimal altUnitsL) {
        this.altUnitsL = altUnitsL;
    }

    public BigDecimal getAlkalinePhosphatase() {
        return alkalinePhosphatase;
    }

    public void setAlkalinePhosphatase(BigDecimal alkalinePhosphatase) {
        this.alkalinePhosphatase = alkalinePhosphatase;
    }

    public BigDecimal getTotalBilirubin() {
        return totalBilirubin;
    }

    public void setTotalBilirubin(BigDecimal totalBilirubin) {
        this.totalBilirubin = totalBilirubin;
    }

    public BigDecimal getCreatinineMgDl() {
        return creatinineMgDl;
    }

    public void setCreatinineMgDl(BigDecimal creatinineMgDl) {
        this.creatinineMgDl = creatinineMgDl;
    }

    public BigDecimal getBunMgDl() {
        return bunMgDl;
    }

    public void setBunMgDl(BigDecimal bunMgDl) {
        this.bunMgDl = bunMgDl;
    }

    public BigDecimal getGfrMlMin() {
        return gfrMlMin;
    }

    public void setGfrMlMin(BigDecimal gfrMlMin) {
        this.gfrMlMin = gfrMlMin;
    }

    public BigDecimal getSodiumMmolL() {
        return sodiumMmolL;
    }

    public void setSodiumMmolL(BigDecimal sodiumMmolL) {
        this.sodiumMmolL = sodiumMmolL;
    }

    public BigDecimal getPotassiumMmolL() {
        return potassiumMmolL;
    }

    public void setPotassiumMmolL(BigDecimal potassiumMmolL) {
        this.potassiumMmolL = potassiumMmolL;
    }

    public BigDecimal getChlorideMmolL() {
        return chlorideMmolL;
    }

    public void setChlorideMmolL(BigDecimal chlorideMmolL) {
        this.chlorideMmolL = chlorideMmolL;
    }

    public BigDecimal getCalciumMgDl() {
        return calciumMgDl;
    }

    public void setCalciumMgDl(BigDecimal calciumMgDl) {
        this.calciumMgDl = calciumMgDl;
    }

    public BigDecimal getMagnesiumMgDl() {
        return magnesiumMgDl;
    }

    public void setMagnesiumMgDl(BigDecimal magnesiumMgDl) {
        this.magnesiumMgDl = magnesiumMgDl;
    }

    public BigDecimal getPhosphorusMgDl() {
        return phosphorusMgDl;
    }

    public void setPhosphorusMgDl(BigDecimal phosphorusMgDl) {
        this.phosphorusMgDl = phosphorusMgDl;
    }

    public BigDecimal getIronMcgDl() {
        return ironMcgDl;
    }

    public void setIronMcgDl(BigDecimal ironMcgDl) {
        this.ironMcgDl = ironMcgDl;
    }

    public BigDecimal getFerritinNgMl() {
        return ferritinNgMl;
    }

    public void setFerritinNgMl(BigDecimal ferritinNgMl) {
        this.ferritinNgMl = ferritinNgMl;
    }

    public BigDecimal getZincMcgDl() {
        return zincMcgDl;
    }

    public void setZincMcgDl(BigDecimal zincMcgDl) {
        this.zincMcgDl = zincMcgDl;
    }

    public BigDecimal getVitaminDNgMl() {
        return vitaminDNgMl;
    }

    public void setVitaminDNgMl(BigDecimal vitaminDNgMl) {
        this.vitaminDNgMl = vitaminDNgMl;
    }

    public BigDecimal getVitaminB12PgMl() {
        return vitaminB12PgMl;
    }

    public void setVitaminB12PgMl(BigDecimal vitaminB12PgMl) {
        this.vitaminB12PgMl = vitaminB12PgMl;
    }

    public BigDecimal getFolateNgMl() {
        return folateNgMl;
    }

    public void setFolateNgMl(BigDecimal folateNgMl) {
        this.folateNgMl = folateNgMl;
    }

    public BigDecimal getCReactiveProteinMgL() {
        return cReactiveProteinMgL;
    }

    public void setCReactiveProteinMgL(BigDecimal cReactiveProteinMgL) {
        this.cReactiveProteinMgL = cReactiveProteinMgL;
    }

    public BigDecimal getHemoglobinA1cPercent() {
        return hemoglobinA1cPercent;
    }

    public void setHemoglobinA1cPercent(BigDecimal hemoglobinA1cPercent) {
        this.hemoglobinA1cPercent = hemoglobinA1cPercent;
    }

    public BigDecimal getTshMiuL() {
        return tshMiuL;
    }

    public void setTshMiuL(BigDecimal tshMiuL) {
        this.tshMiuL = tshMiuL;
    }

    public BigDecimal getT3PgMl() {
        return t3PgMl;
    }

    public void setT3PgMl(BigDecimal t3PgMl) {
        this.t3PgMl = t3PgMl;
    }

    public BigDecimal getT4NgDl() {
        return t4NgDl;
    }

    public void setT4NgDl(BigDecimal t4NgDl) {
        this.t4NgDl = t4NgDl;
    }

    public BigDecimal getCortisolMcgDl() {
        return cortisolMcgDl;
    }

    public void setCortisolMcgDl(BigDecimal cortisolMcgDl) {
        this.cortisolMcgDl = cortisolMcgDl;
    }

    public String getMeasurementDevice() {
        return measurementDevice;
    }

    public void setMeasurementDevice(String measurementDevice) {
        this.measurementDevice = measurementDevice;
    }

    public String getLabName() {
        return labName;
    }

    public void setLabName(String labName) {
        this.labName = labName;
    }

    public String getMeasurementLocation() {
        return measurementLocation;
    }

    public void setMeasurementLocation(String measurementLocation) {
        this.measurementLocation = measurementLocation;
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
