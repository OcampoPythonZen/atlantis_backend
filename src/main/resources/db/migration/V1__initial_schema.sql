-- ============================================
-- SCHEMA: AUTHENTICATION AND BASE USERS
-- ============================================

CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    phone VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT true
);

CREATE TABLE roles (
    id SMALLINT PRIMARY KEY,
    role_name VARCHAR(50) UNIQUE NOT NULL,
    description VARCHAR(255)
);

INSERT INTO roles (id, role_name, description) VALUES
(1, 'NUTRIOLOGIST', 'Professional nutritionist'),
(2, 'PATIENT', 'Patient/Client');

CREATE TABLE user_roles (
    user_id UUID NOT NULL,
    role_id SMALLINT NOT NULL,
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- ============================================
-- SCHEMA: NUTRITIONISTS
-- ============================================

CREATE TABLE nutriologists (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL UNIQUE,
    professional_license VARCHAR(100) UNIQUE,
    specialization VARCHAR(100),
    bio TEXT,
    profile_image_url VARCHAR(500),
    clinic_name VARCHAR(255),
    address VARCHAR(500),
    city VARCHAR(100),
    state VARCHAR(100),
    country VARCHAR(100),
    phone_clinic VARCHAR(20),
    years_experience INT,
    verified BOOLEAN DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_nutriologist_user ON nutriologists(user_id);
CREATE INDEX idx_nutriologist_verified ON nutriologists(verified);

-- ============================================
-- SCHEMA: CLIENTS/PATIENTS - BASIC DATA
-- ============================================

CREATE TABLE clients (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL UNIQUE,
    date_of_birth DATE NOT NULL,
    gender VARCHAR(20),
    blood_type VARCHAR(10), -- O+, O-, A+, A-, B+, B-, AB+, AB-
    rh_factor VARCHAR(5),
    nationality VARCHAR(100),
    emergency_contact_name VARCHAR(255),
    emergency_contact_phone VARCHAR(20),
    emergency_contact_relationship VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_client_user ON clients(user_id);

-- ============================================
-- SCHEMA: MEDICAL HISTORY AND PATHOLOGIES
-- ============================================

CREATE TABLE medical_history (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    client_id UUID NOT NULL UNIQUE,

    -- Chronic conditions
    has_diabetes BOOLEAN DEFAULT false,
    diabetes_type VARCHAR(50), -- type_1, type_2, gestational
    diabetes_diagnosis_date DATE,

    has_hypertension BOOLEAN DEFAULT false,
    hypertension_diagnosis_date DATE,

    has_thyroid_disorder BOOLEAN DEFAULT false,
    thyroid_condition VARCHAR(100), -- hyperthyroidism, hypothyroidism

    has_heart_disease BOOLEAN DEFAULT false,
    heart_condition_details TEXT,

    has_kidney_disease BOOLEAN DEFAULT false,
    kidney_condition_details TEXT,

    has_liver_disease BOOLEAN DEFAULT false,
    liver_condition_details TEXT,

    has_gastrointestinal_issues BOOLEAN DEFAULT false,
    gi_condition_details TEXT,

    has_autoimmune_disease BOOLEAN DEFAULT false,
    autoimmune_type VARCHAR(100),

    has_mental_health_condition BOOLEAN DEFAULT false,
    mental_health_details TEXT,

    has_cancer_history BOOLEAN DEFAULT false,
    cancer_details TEXT,
    cancer_remission_date DATE,

    -- Surgeries
    surgical_history TEXT,

    -- Current medications
    current_medications TEXT,
    medication_last_updated TIMESTAMP,

    -- Allergies
    food_allergies TEXT,
    medication_allergies TEXT,
    environmental_allergies TEXT,

    -- Habits
    smoking_status VARCHAR(50), -- never, former, current
    alcohol_consumption VARCHAR(50), -- never, occasional, moderate, heavy
    drug_use_history VARCHAR(50),

    -- Physical activity
    exercise_frequency VARCHAR(50), -- sedentary, lightly_active, moderately_active, very_active
    exercise_type VARCHAR(255), -- types of exercise performed
    hours_sleep_per_night DECIMAL(3, 1),

    -- Family history
    family_history_diabetes BOOLEAN DEFAULT false,
    family_history_hypertension BOOLEAN DEFAULT false,
    family_history_heart_disease BOOLEAN DEFAULT false,
    family_history_cancer BOOLEAN DEFAULT false,
    family_history_obesity BOOLEAN DEFAULT false,
    family_history_notes TEXT,

    -- Gynecological (if applicable)
    menstrual_cycle_regular BOOLEAN,
    menopausal_status VARCHAR(50), -- pre-menopausal, menopausal, post-menopausal
    contraceptive_method VARCHAR(100),
    pregnant BOOLEAN DEFAULT false,
    breastfeeding BOOLEAN DEFAULT false,

    -- Digestion and GI
    ibs_diagnosis BOOLEAN DEFAULT false,
    celiac_disease BOOLEAN DEFAULT false,
    lactose_intolerance BOOLEAN DEFAULT false,
    gluten_sensitivity BOOLEAN DEFAULT false,

    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (client_id) REFERENCES clients(id) ON DELETE CASCADE
);

CREATE INDEX idx_medical_history_client ON medical_history(client_id);

-- ============================================
-- SCHEMA: MEASUREMENTS AND BIOMETRIC DATA
-- ============================================

CREATE TABLE measurements (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    client_id UUID NOT NULL,
    nutritionist_id UUID NOT NULL,
    measurement_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    -- Basic anthropometric measurements
    weight_kg DECIMAL(5, 2),
    height_cm DECIMAL(5, 2),
    imc DECIMAL(4, 2),

    -- Body composition (bioimpedance)
    body_fat_percentage DECIMAL(5, 2),
    muscle_mass_kg DECIMAL(5, 2),
    visceral_fat_percentage DECIMAL(5, 2),
    subcutaneous_fat_percentage DECIMAL(5, 2),
    water_percentage DECIMAL(5, 2),
    bone_mass_kg DECIMAL(5, 2),
    metabolic_age INT,
    basal_metabolic_rate INT, -- BMR in calories

    -- Body circumferences
    waist_cm DECIMAL(5, 2),
    hip_cm DECIMAL(5, 2),
    waist_hip_ratio DECIMAL(4, 2),
    chest_cm DECIMAL(5, 2),
    arm_cm DECIMAL(5, 2),
    thigh_cm DECIMAL(5, 2),

    -- Blood pressure
    blood_pressure_systolic INT,
    blood_pressure_diastolic INT,
    blood_pressure_notes VARCHAR(255),

    -- Cardiovascular parameters
    heart_rate_bpm INT,
    resting_heart_rate INT,

    -- Metabolic parameters (glucose)
    fasting_glucose_mg_dl DECIMAL(6, 2),
    glucose_2hr_post_meal DECIMAL(6, 2),

    -- Lipid profile
    total_cholesterol_mg_dl DECIMAL(6, 2),
    ldl_cholesterol_mg_dl DECIMAL(6, 2),
    hdl_cholesterol_mg_dl DECIMAL(6, 2),
    triglycerides_mg_dl DECIMAL(6, 2),

    -- Liver function
    ast_units_l DECIMAL(6, 2), -- Aspartate aminotransferase
    alt_units_l DECIMAL(6, 2), -- Alanine aminotransferase
    alkaline_phosphatase DECIMAL(6, 2),
    total_bilirubin DECIMAL(6, 2),

    -- Kidney function
    creatinine_mg_dl DECIMAL(6, 2),
    bun_mg_dl DECIMAL(6, 2), -- Blood Urea Nitrogen
    gfr_ml_min DECIMAL(6, 2), -- Glomerular Filtration Rate

    -- Electrolytes
    sodium_mmol_l DECIMAL(6, 2),
    potassium_mmol_l DECIMAL(6, 2),
    chloride_mmol_l DECIMAL(6, 2),
    calcium_mg_dl DECIMAL(6, 2),
    magnesium_mg_dl DECIMAL(6, 2),
    phosphorus_mg_dl DECIMAL(6, 2),

    -- Minerals and micronutrients
    iron_mcg_dl DECIMAL(6, 2),
    ferritin_ng_ml DECIMAL(6, 2),
    zinc_mcg_dl DECIMAL(6, 2),
    vitamin_d_ng_ml DECIMAL(6, 2),
    vitamin_b12_pg_ml DECIMAL(6, 2),
    folate_ng_ml DECIMAL(6, 2),

    -- Inflammation and others
    c_reactive_protein_mg_l DECIMAL(6, 2),
    hemoglobin_a1c_percent DECIMAL(4, 2), -- For diabetics

    -- Hormones (if required)
    tsh_miu_l DECIMAL(6, 2),
    t3_pg_ml DECIMAL(6, 2),
    t4_ng_dl DECIMAL(6, 2),
    cortisol_mcg_dl DECIMAL(6, 2),

    -- Metadata
    measurement_device VARCHAR(100),
    lab_name VARCHAR(255),
    measurement_location VARCHAR(100), -- clinic, home, lab
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (client_id) REFERENCES clients(id) ON DELETE CASCADE,
    FOREIGN KEY (nutritionist_id) REFERENCES nutriologists(id) ON DELETE CASCADE
);

CREATE INDEX idx_measurement_client ON measurements(client_id);
CREATE INDEX idx_measurement_date ON measurements(measurement_date);
CREATE INDEX idx_measurement_nutritionist ON measurements(nutritionist_id);

-- ============================================
-- SCHEMA: DIETARY TRACKING AND CONSUMPTION
-- ============================================

CREATE TABLE food_diary_entries (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    client_id UUID NOT NULL,
    entry_date DATE NOT NULL,
    meal_type VARCHAR(50), -- breakfast, lunch, dinner, snack
    meal_time TIME,

    food_description TEXT NOT NULL,
    quantity DECIMAL(6, 2),
    unit VARCHAR(50), -- grams, cups, pieces, ml

    -- Macronutrients
    calories DECIMAL(8, 2),
    protein_grams DECIMAL(6, 2),
    carbs_grams DECIMAL(6, 2),
    fats_grams DECIMAL(6, 2),
    fiber_grams DECIMAL(6, 2),
    sugar_grams DECIMAL(6, 2),
    sodium_mg DECIMAL(8, 2),

    -- Micronutrients
    calcium_mg DECIMAL(8, 2),
    iron_mg DECIMAL(6, 2),
    potassium_mg DECIMAL(8, 2),
    magnesium_mg DECIMAL(6, 2),
    zinc_mg DECIMAL(6, 2),
    vitamin_c_mg DECIMAL(6, 2),
    vitamin_a_iu DECIMAL(8, 2),

    water_ml INT,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (client_id) REFERENCES clients(id) ON DELETE CASCADE
);

CREATE INDEX idx_diary_client ON food_diary_entries(client_id);
CREATE INDEX idx_diary_date ON food_diary_entries(entry_date);

-- ============================================
-- SCHEMA: SYMPTOMS AND SIDE EFFECTS
-- ============================================

CREATE TABLE symptom_logs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    client_id UUID NOT NULL,
    log_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    symptom_name VARCHAR(255),
    severity_level INT, -- 1-10
    symptom_description TEXT,

    -- Context
    associated_food VARCHAR(255),
    time_of_day VARCHAR(50),
    duration_hours INT,
    trigger_suspected VARCHAR(255),

    -- Response
    medication_taken VARCHAR(255),
    relief_level INT, -- 1-10

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (client_id) REFERENCES clients(id) ON DELETE CASCADE
);

CREATE INDEX idx_symptom_client ON symptom_logs(client_id);
CREATE INDEX idx_symptom_date ON symptom_logs(log_date);

-- ============================================
-- SCHEMA: CLIENT GOALS/OBJECTIVES
-- ============================================

CREATE TABLE client_goals (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    client_id UUID NOT NULL,
    nutritionist_id UUID NOT NULL,
    goal_type VARCHAR(100), -- weight_loss, muscle_gain, health_improvement, disease_management
    target_value DECIMAL(8, 2),
    target_unit VARCHAR(50), -- kg, %, etc
    current_value DECIMAL(8, 2),
    start_date DATE,
    target_date DATE,
    status VARCHAR(50) DEFAULT 'active', -- active, completed, abandoned
    description TEXT,
    clinical_reason TEXT, -- Medical reason for the goal
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (client_id) REFERENCES clients(id) ON DELETE CASCADE,
    FOREIGN KEY (nutritionist_id) REFERENCES nutriologists(id) ON DELETE CASCADE
);

CREATE INDEX idx_goal_client ON client_goals(client_id);
CREATE INDEX idx_goal_status ON client_goals(status);

-- ============================================
-- SCHEMA: NUTRITION PLANS
-- ============================================

CREATE TABLE nutrition_plans (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    client_id UUID NOT NULL,
    nutritionist_id UUID NOT NULL,
    plan_name VARCHAR(255),

    -- Macronutrients
    calories_target INT,
    protein_grams DECIMAL(6, 2),
    carbs_grams DECIMAL(6, 2),
    fats_grams DECIMAL(6, 2),
    fiber_grams DECIMAL(6, 2),

    -- Medical restrictions
    foods_to_avoid TEXT,
    foods_to_limit TEXT,
    foods_recommended TEXT,

    -- Clinical
    plan_type VARCHAR(100), -- balanced, low_carb, low_fat, diabetic, renal, cardiac
    medical_condition_target VARCHAR(255),

    description TEXT,
    start_date DATE,
    end_date DATE,
    is_active BOOLEAN DEFAULT true,

    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (client_id) REFERENCES clients(id) ON DELETE CASCADE,
    FOREIGN KEY (nutritionist_id) REFERENCES nutriologists(id) ON DELETE CASCADE
);

CREATE INDEX idx_plan_client ON nutrition_plans(client_id);
CREATE INDEX idx_plan_active ON nutrition_plans(is_active);

-- ============================================
-- SCHEMA: SUPPLEMENTATION
-- ============================================

CREATE TABLE supplement_tracking (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    client_id UUID NOT NULL,
    nutritionist_id UUID NOT NULL,

    supplement_name VARCHAR(255),
    supplement_type VARCHAR(100), -- vitamin, mineral, protein, probiotic, etc
    dosage VARCHAR(100),
    frequency VARCHAR(100), -- daily, twice_daily, weekly, etc

    start_date DATE,
    end_date DATE,
    reason_prescribed TEXT,

    effectiveness_rating INT, -- 1-10
    side_effects TEXT,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (client_id) REFERENCES clients(id) ON DELETE CASCADE,
    FOREIGN KEY (nutritionist_id) REFERENCES nutriologists(id) ON DELETE CASCADE
);

CREATE INDEX idx_supplement_client ON supplement_tracking(client_id);

-- ============================================
-- SCHEMA: CLINICAL NOTES AND FOLLOW-UP
-- ============================================

CREATE TABLE clinical_notes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    client_id UUID NOT NULL,
    nutritionist_id UUID NOT NULL,

    consultation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    consultation_type VARCHAR(100), -- initial, follow_up, emergency

    chief_complaint TEXT,
    clinical_assessment TEXT,
    diagnosis_or_impression TEXT,
    plan_of_action TEXT,
    follow_up_date DATE,

    weight_at_visit_kg DECIMAL(5, 2),
    overall_health_status VARCHAR(100),

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (client_id) REFERENCES clients(id) ON DELETE CASCADE,
    FOREIGN KEY (nutritionist_id) REFERENCES nutriologists(id) ON DELETE CASCADE
);

CREATE INDEX idx_clinical_notes_client ON clinical_notes(client_id);
CREATE INDEX idx_clinical_notes_date ON clinical_notes(consultation_date);

-- ============================================
-- SCHEMA: AUDIT AND SECURITY
-- ============================================

CREATE TABLE audit_logs (
    id BIGSERIAL PRIMARY KEY,
    user_id UUID,
    action VARCHAR(255),
    entity_type VARCHAR(100),
    entity_id UUID,
    old_values JSONB,
    new_values JSONB,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ip_address VARCHAR(45),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);

CREATE INDEX idx_audit_user ON audit_logs(user_id);
CREATE INDEX idx_audit_timestamp ON audit_logs(timestamp);
CREATE INDEX idx_audit_entity ON audit_logs(entity_type, entity_id);
