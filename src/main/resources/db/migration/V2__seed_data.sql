-- ============================================
-- SEED DATA FOR NUTRITIONIST APPLICATION
-- ============================================
-- Password for all users: "password123"
-- BCrypt hash (cost factor 10, compatible with Spring Security BCryptPasswordEncoder):
-- $2b$10$ykKqBdzuQLsfKxf7GHpvSe1S9dFR0/ynL.eJUid25QbSRhvlX.pT6

-- ============================================
-- USERS
-- ============================================

-- Nutriologist 1: Dr. Maria Rodriguez
INSERT INTO users (id, email, password, first_name, last_name, phone, is_active)
VALUES
('11111111-1111-1111-1111-111111111111', 'maria.rodriguez@nutrition.com', '$2b$10$ykKqBdzuQLsfKxf7GHpvSe1S9dFR0/ynL.eJUid25QbSRhvlX.pT6', 'Maria', 'Rodriguez', '+1-555-0101', true);

-- Nutriologist 2: Dr. John Smith
INSERT INTO users (id, email, password, first_name, last_name, phone, is_active)
VALUES
('22222222-2222-2222-2222-222222222222', 'john.smith@nutrition.com', '$2b$10$ykKqBdzuQLsfKxf7GHpvSe1S9dFR0/ynL.eJUid25QbSRhvlX.pT6', 'John', 'Smith', '+1-555-0102', true);

-- Patient 1: Sarah Johnson
INSERT INTO users (id, email, password, first_name, last_name, phone, is_active)
VALUES
('33333333-3333-3333-3333-333333333333', 'sarah.johnson@email.com', '$2b$10$ykKqBdzuQLsfKxf7GHpvSe1S9dFR0/ynL.eJUid25QbSRhvlX.pT6', 'Sarah', 'Johnson', '+1-555-0201', true);

-- Patient 2: Michael Chen
INSERT INTO users (id, email, password, first_name, last_name, phone, is_active)
VALUES
('44444444-4444-4444-4444-444444444444', 'michael.chen@email.com', '$2b$10$ykKqBdzuQLsfKxf7GHpvSe1S9dFR0/ynL.eJUid25QbSRhvlX.pT6', 'Michael', 'Chen', '+1-555-0202', true);

-- Patient 3: Emma Davis
INSERT INTO users (id, email, password, first_name, last_name, phone, is_active)
VALUES
('55555555-5555-5555-5555-555555555555', 'emma.davis@email.com', '$2b$10$ykKqBdzuQLsfKxf7GHpvSe1S9dFR0/ynL.eJUid25QbSRhvlX.pT6', 'Emma', 'Davis', '+1-555-0203', true);

-- ============================================
-- USER ROLES
-- ============================================

-- Assign nutriologist role
INSERT INTO user_roles (user_id, role_id)
VALUES
('11111111-1111-1111-1111-111111111111', 1),
('22222222-2222-2222-2222-222222222222', 1);

-- Assign patient role
INSERT INTO user_roles (user_id, role_id)
VALUES
('33333333-3333-3333-3333-333333333333', 2),
('44444444-4444-4444-4444-444444444444', 2),
('55555555-5555-5555-5555-555555555555', 2);

-- ============================================
-- NUTRIOLOGISTS
-- ============================================

INSERT INTO nutriologists (id, user_id, professional_license, specialization, bio, clinic_name, address, city, state, country, phone_clinic, years_experience, verified)
VALUES
('a1111111-1111-1111-1111-111111111111', '11111111-1111-1111-1111-111111111111', 'NUT-2015-001234', 'Clinical Nutrition',
'Specialized in metabolic disorders, diabetes management, and weight loss. Over 10 years of experience helping patients achieve their health goals.',
'Rodriguez Nutrition Clinic', '123 Health Street', 'Los Angeles', 'California', 'USA', '+1-555-1001', 10, true);

INSERT INTO nutriologists (id, user_id, professional_license, specialization, bio, clinic_name, address, city, state, country, phone_clinic, years_experience, verified)
VALUES
('b2222222-2222-2222-2222-222222222222', '22222222-2222-2222-2222-222222222222', 'NUT-2018-005678', 'Sports Nutrition',
'Expert in sports nutrition, muscle gain, and athletic performance optimization. Worked with professional athletes and fitness enthusiasts.',
'Smith Performance Nutrition', '456 Fitness Avenue', 'New York', 'New York', 'USA', '+1-555-1002', 7, true);

-- ============================================
-- CLIENTS
-- ============================================

INSERT INTO clients (id, user_id, date_of_birth, gender, blood_type, rh_factor, nationality, emergency_contact_name, emergency_contact_phone, emergency_contact_relationship)
VALUES
('c3333333-3333-3333-3333-333333333333', '33333333-3333-3333-3333-333333333333', '1985-06-15', 'Female', 'A+', '+', 'USA', 'Robert Johnson', '+1-555-9001', 'Husband');

INSERT INTO clients (id, user_id, date_of_birth, gender, blood_type, rh_factor, nationality, emergency_contact_name, emergency_contact_phone, emergency_contact_relationship)
VALUES
('d4444444-4444-4444-4444-444444444444', '44444444-4444-4444-4444-444444444444', '1990-03-22', 'Male', 'O+', '+', 'USA', 'Lisa Chen', '+1-555-9002', 'Wife');

INSERT INTO clients (id, user_id, date_of_birth, gender, blood_type, rh_factor, nationality, emergency_contact_name, emergency_contact_phone, emergency_contact_relationship)
VALUES
('e5555555-5555-5555-5555-555555555555', '55555555-5555-5555-5555-555555555555', '1978-11-08', 'Female', 'B+', '+', 'USA', 'David Davis', '+1-555-9003', 'Brother');

-- ============================================
-- MEDICAL HISTORY
-- ============================================

-- Sarah Johnson - Pre-diabetic, weight management
INSERT INTO medical_history (
    id, client_id, has_diabetes, diabetes_type, has_hypertension, has_thyroid_disorder, thyroid_condition,
    food_allergies, medication_allergies, smoking_status, alcohol_consumption, exercise_frequency,
    hours_sleep_per_night, family_history_diabetes, family_history_hypertension, menstrual_cycle_regular,
    menopausal_status, lactose_intolerance, notes
)
VALUES (
    'f1111111-1111-1111-1111-111111111111', 'c3333333-3333-3333-3333-333333333333',
    false, null, false, true, 'hypothyroidism',
    'Shellfish', 'Penicillin', 'never', 'occasional', 'lightly_active',
    7.0, true, true, true, 'pre-menopausal', true,
    'Patient shows signs of insulin resistance. Family history of Type 2 diabetes. Currently taking levothyroxine for hypothyroidism.'
);

-- Michael Chen - Athlete, muscle gain focus
INSERT INTO medical_history (
    id, client_id, has_diabetes, has_hypertension, has_gastrointestinal_issues, gi_condition_details,
    food_allergies, smoking_status, alcohol_consumption, exercise_frequency, exercise_type,
    hours_sleep_per_night, lactose_intolerance, notes
)
VALUES (
    'f2222222-2222-2222-2222-222222222222', 'd4444444-4444-4444-4444-444444444444',
    false, false, false, null,
    'None', 'never', 'never', 'very_active', 'Weight training, Running, Swimming',
    8.0, false,
    'Competitive athlete training for marathon. No significant medical history. Excellent health status.'
);

-- Emma Davis - Type 2 diabetes management
INSERT INTO medical_history (
    id, client_id, has_diabetes, diabetes_type, diabetes_diagnosis_date, has_hypertension, hypertension_diagnosis_date,
    has_heart_disease, current_medications, food_allergies, medication_allergies,
    smoking_status, alcohol_consumption, exercise_frequency, hours_sleep_per_night,
    family_history_diabetes, family_history_heart_disease, family_history_obesity, menstrual_cycle_regular,
    menopausal_status, celiac_disease, notes
)
VALUES (
    'f3333333-3333-3333-3333-333333333333', 'e5555555-5555-5555-5555-555555555555',
    true, 'type_2', '2020-01-15', true, '2019-06-01',
    false, 'Metformin 1000mg twice daily, Lisinopril 10mg daily', 'Peanuts', 'Sulfa drugs',
    'former', 'never', 'moderately_active', 6.5,
    true, true, true, null, 'post-menopausal', false,
    'Type 2 diabetes diagnosed 5 years ago. Currently well-controlled with medication and diet. Recent HbA1c 6.8%.'
);

-- ============================================
-- MEASUREMENTS
-- ============================================

-- Sarah Johnson - Initial consultation
INSERT INTO measurements (
    id, client_id, nutritionist_id, measurement_date,
    weight_kg, height_cm, imc, body_fat_percentage, muscle_mass_kg, visceral_fat_percentage,
    waist_cm, hip_cm, waist_hip_ratio, blood_pressure_systolic, blood_pressure_diastolic,
    heart_rate_bpm, fasting_glucose_mg_dl, total_cholesterol_mg_dl, ldl_cholesterol_mg_dl,
    hdl_cholesterol_mg_dl, triglycerides_mg_dl, tsh_miu_l, vitamin_d_ng_ml, notes
)
VALUES (
    'a7777777-7777-7777-7777-777777777771', 'c3333333-3333-3333-3333-333333333333',
    'a1111111-1111-1111-1111-111111111111', '2024-11-01 10:00:00',
    78.5, 165.0, 28.8, 35.2, 45.5, 9.5,
    89.0, 105.0, 0.85, 125, 82,
    72, 105.0, 215.0, 135.0,
    48.0, 160.0, 3.8, 22.0, 'Initial assessment. Patient is overweight with signs of metabolic syndrome.'
);

-- Michael Chen - Athletic baseline
INSERT INTO measurements (
    id, client_id, nutritionist_id, measurement_date,
    weight_kg, height_cm, imc, body_fat_percentage, muscle_mass_kg, visceral_fat_percentage,
    waist_cm, hip_cm, blood_pressure_systolic, blood_pressure_diastolic,
    heart_rate_bpm, resting_heart_rate, fasting_glucose_mg_dl, notes
)
VALUES (
    'a7777777-7777-7777-7777-777777777772', 'd4444444-4444-4444-4444-444444444444',
    'b2222222-2222-2222-2222-222222222222', '2024-11-05 14:30:00',
    75.0, 180.0, 23.1, 12.5, 62.0, 3.2,
    82.0, 98.0, 118, 75,
    58, 52, 88.0, 'Excellent athletic condition. Goal is to increase muscle mass by 3-5kg while maintaining low body fat.'
);

-- Emma Davis - Diabetic monitoring
INSERT INTO measurements (
    id, client_id, nutritionist_id, measurement_date,
    weight_kg, height_cm, imc, body_fat_percentage, waist_cm, hip_cm, waist_hip_ratio,
    blood_pressure_systolic, blood_pressure_diastolic, fasting_glucose_mg_dl, hemoglobin_a1c_percent,
    total_cholesterol_mg_dl, ldl_cholesterol_mg_dl, hdl_cholesterol_mg_dl, triglycerides_mg_dl,
    creatinine_mg_dl, gfr_ml_min, notes
)
VALUES (
    'a7777777-7777-7777-7777-777777777773', 'e5555555-5555-5555-5555-555555555555',
    'a1111111-1111-1111-1111-111111111111', '2024-11-10 09:00:00',
    92.0, 162.0, 35.0, 42.0, 98.0, 110.0, 0.89,
    135, 88, 128.0, 6.8,
    198.0, 115.0, 52.0, 155.0,
    0.9, 75.0, 'Diabetes well-controlled. Continue current medication regimen with dietary modifications.'
);

-- ============================================
-- FOOD DIARY ENTRIES
-- ============================================

-- Sarah Johnson - Sample breakfast
INSERT INTO food_diary_entries (
    id, client_id, entry_date, meal_type, meal_time, food_description,
    quantity, unit, calories, protein_grams, carbs_grams, fats_grams, fiber_grams, sugar_grams
)
VALUES (
    'a8888888-8888-8888-8888-888888888881', 'c3333333-3333-3333-3333-333333333333',
    '2024-11-15', 'breakfast', '07:30', 'Oatmeal with berries and almonds',
    250, 'grams', 320, 12.0, 52.0, 9.0, 8.0, 12.0
);

-- Michael Chen - Post-workout meal
INSERT INTO food_diary_entries (
    id, client_id, entry_date, meal_type, meal_time, food_description,
    quantity, unit, calories, protein_grams, carbs_grams, fats_grams, fiber_grams
)
VALUES (
    'a8888888-8888-8888-8888-888888888882', 'd4444444-4444-4444-4444-444444444444',
    '2024-11-15', 'snack', '11:00', 'Protein shake with banana',
    400, 'ml', 285, 30.0, 35.0, 3.5, 4.0
);

-- Emma Davis - Diabetic-friendly lunch
INSERT INTO food_diary_entries (
    id, client_id, entry_date, meal_type, meal_time, food_description,
    quantity, unit, calories, protein_grams, carbs_grams, fats_grams, fiber_grams, sugar_grams
)
VALUES (
    'a8888888-8888-8888-8888-888888888883', 'e5555555-5555-5555-5555-555555555555',
    '2024-11-15', 'lunch', '12:30', 'Grilled chicken salad with olive oil dressing',
    350, 'grams', 385, 35.0, 18.0, 22.0, 6.0, 5.0
);

-- ============================================
-- CLIENT GOALS
-- ============================================

-- Sarah Johnson - Weight loss goal
INSERT INTO client_goals (
    id, client_id, nutritionist_id, goal_type, target_value, target_unit, current_value,
    start_date, target_date, status, description, clinical_reason
)
VALUES (
    'a9999999-9999-9999-9999-999999999991', 'c3333333-3333-3333-3333-333333333333',
    'a1111111-1111-1111-1111-111111111111', 'weight_loss', 68.0, 'kg', 78.5,
    '2024-11-01', '2025-05-01', 'active', 'Lose 10.5 kg to achieve healthy BMI',
    'Reduce insulin resistance and lower risk of Type 2 diabetes. Family history indicates high risk.'
);

-- Michael Chen - Muscle gain goal
INSERT INTO client_goals (
    id, client_id, nutritionist_id, goal_type, target_value, target_unit, current_value,
    start_date, target_date, status, description, clinical_reason
)
VALUES (
    'a9999999-9999-9999-9999-999999999992', 'd4444444-4444-4444-4444-444444444444',
    'b2222222-2222-2222-2222-222222222222', 'muscle_gain', 80.0, 'kg', 75.0,
    '2024-11-05', '2025-02-05', 'active', 'Gain 5 kg lean muscle mass',
    'Improve athletic performance for upcoming marathon. Increase strength-to-weight ratio.'
);

-- Emma Davis - Blood sugar management
INSERT INTO client_goals (
    id, client_id, nutritionist_id, goal_type, target_value, target_unit, current_value,
    start_date, target_date, status, description, clinical_reason
)
VALUES (
    'a9999999-9999-9999-9999-999999999993', 'e5555555-5555-5555-5555-555555555555',
    'a1111111-1111-1111-1111-111111111111', 'health_improvement', 6.0, '%', 6.8,
    '2024-11-10', '2025-05-10', 'active', 'Reduce HbA1c to 6.0% or below',
    'Better diabetes control to prevent complications. Reduce medication dependence.'
);

-- ============================================
-- NUTRITION PLANS
-- ============================================

-- Sarah Johnson - Low carb plan
INSERT INTO nutrition_plans (
    id, client_id, nutritionist_id, plan_name, calories_target, protein_grams, carbs_grams, fats_grams, fiber_grams,
    foods_to_avoid, foods_to_limit, foods_recommended, plan_type, medical_condition_target,
    description, start_date, end_date, is_active
)
VALUES (
    'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaa001', 'c3333333-3333-3333-3333-333333333333',
    'a1111111-1111-1111-1111-111111111111', 'Weight Loss - Low Carb', 1600, 100.0, 120.0, 70.0, 30.0,
    'White bread, sugary drinks, processed foods, shellfish (allergy)',
    'Rice, pasta, potatoes, dairy (lactose intolerance)',
    'Lean proteins, vegetables, nuts, seeds, berries, olive oil, fatty fish',
    'low_carb', 'Pre-diabetes and weight management',
    'Moderate calorie deficit with focus on complex carbs and lean proteins. Thyroid-supportive foods included.',
    '2024-11-01', '2025-05-01', true
);

-- Michael Chen - High protein athletic plan
INSERT INTO nutrition_plans (
    id, client_id, nutritionist_id, plan_name, calories_target, protein_grams, carbs_grams, fats_grams, fiber_grams,
    foods_to_avoid, foods_to_limit, foods_recommended, plan_type, medical_condition_target,
    description, start_date, end_date, is_active
)
VALUES (
    'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaa002', 'd4444444-4444-4444-4444-444444444444',
    'b2222222-2222-2222-2222-222222222222', 'Muscle Gain - Athletic Performance', 3200, 180.0, 380.0, 95.0, 35.0,
    'None',
    'Alcohol, processed sugars',
    'Chicken, fish, eggs, brown rice, sweet potatoes, quinoa, vegetables, fruits, nuts, protein supplements',
    'balanced', 'Athletic performance and muscle gain',
    'High calorie surplus with optimal protein timing. Pre/post workout nutrition emphasized.',
    '2024-11-05', '2025-02-05', true
);

-- Emma Davis - Diabetic plan
INSERT INTO nutrition_plans (
    id, client_id, nutritionist_id, plan_name, calories_target, protein_grams, carbs_grams, fats_grams, fiber_grams,
    foods_to_avoid, foods_to_limit, foods_recommended, plan_type, medical_condition_target,
    description, start_date, is_active
)
VALUES (
    'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaa003', 'e5555555-5555-5555-5555-555555555555',
    'a1111111-1111-1111-1111-111111111111', 'Diabetic Management Plan', 1800, 90.0, 180.0, 60.0, 35.0,
    'Peanuts (allergy), white sugar, refined carbs, high-sodium foods',
    'Fruits (portion control), whole grains (moderate)',
    'Non-starchy vegetables, lean proteins, legumes, healthy fats, low-GI foods',
    'diabetic', 'Type 2 diabetes and hypertension',
    'Balanced diabetic diet with focus on blood sugar control and heart health. Low sodium for hypertension.',
    '2024-11-10', true
);

-- ============================================
-- SUPPLEMENT TRACKING
-- ============================================

-- Sarah Johnson supplements
INSERT INTO supplement_tracking (
    id, client_id, nutritionist_id, supplement_name, supplement_type, dosage, frequency,
    start_date, reason_prescribed
)
VALUES (
    'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbb01', 'c3333333-3333-3333-3333-333333333333',
    'a1111111-1111-1111-1111-111111111111', 'Vitamin D3', 'vitamin', '2000 IU', 'daily',
    '2024-11-01', 'Low vitamin D levels (22 ng/ml). Support thyroid function and bone health.'
);

INSERT INTO supplement_tracking (
    id, client_id, nutritionist_id, supplement_name, supplement_type, dosage, frequency,
    start_date, reason_prescribed
)
VALUES (
    'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbb02', 'c3333333-3333-3333-3333-333333333333',
    'a1111111-1111-1111-1111-111111111111', 'Omega-3 Fish Oil', 'supplement', '1000mg', 'daily',
    '2024-11-01', 'Support cardiovascular health and reduce inflammation.'
);

-- Michael Chen supplements
INSERT INTO supplement_tracking (
    id, client_id, nutritionist_id, supplement_name, supplement_type, dosage, frequency,
    start_date, reason_prescribed
)
VALUES (
    'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbb03', 'd4444444-4444-4444-4444-444444444444',
    'b2222222-2222-2222-2222-222222222222', 'Whey Protein', 'protein', '30g', 'twice_daily',
    '2024-11-05', 'Support muscle recovery and growth. Post-workout nutrition.'
);

INSERT INTO supplement_tracking (
    id, client_id, nutritionist_id, supplement_name, supplement_type, dosage, frequency,
    start_date, reason_prescribed
)
VALUES (
    'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbb04', 'd4444444-4444-4444-4444-444444444444',
    'b2222222-2222-2222-2222-222222222222', 'Creatine Monohydrate', 'supplement', '5g', 'daily',
    '2024-11-05', 'Improve strength and power output. Enhance athletic performance.'
);

-- Emma Davis supplements
INSERT INTO supplement_tracking (
    id, client_id, nutritionist_id, supplement_name, supplement_type, dosage, frequency,
    start_date, reason_prescribed
)
VALUES (
    'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbb05', 'e5555555-5555-5555-5555-555555555555',
    'a1111111-1111-1111-1111-111111111111', 'Chromium Picolinate', 'mineral', '200mcg', 'daily',
    '2024-11-10', 'Support blood sugar regulation and insulin sensitivity.'
);

INSERT INTO supplement_tracking (
    id, client_id, nutritionist_id, supplement_name, supplement_type, dosage, frequency,
    start_date, reason_prescribed
)
VALUES (
    'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbb06', 'e5555555-5555-5555-5555-555555555555',
    'a1111111-1111-1111-1111-111111111111', 'Magnesium Glycinate', 'mineral', '400mg', 'daily',
    '2024-11-10', 'Support cardiovascular health, blood pressure, and glucose metabolism.'
);

-- ============================================
-- CLINICAL NOTES
-- ============================================

-- Sarah Johnson - Initial consultation
INSERT INTO clinical_notes (
    id, client_id, nutritionist_id, consultation_date, consultation_type,
    chief_complaint, clinical_assessment, diagnosis_or_impression, plan_of_action,
    follow_up_date, weight_at_visit_kg, overall_health_status
)
VALUES (
    'cccccccc-cccc-cccc-cccc-cccccccccc01', 'c3333333-3333-3333-3333-333333333333',
    'a1111111-1111-1111-1111-111111111111', '2024-11-01 10:00:00', 'initial',
    'Patient seeking weight loss guidance and concerned about family history of diabetes.',
    'BMI 28.8 (overweight). Fasting glucose 105 mg/dL (pre-diabetic range). Hypothyroidism well-controlled with medication. Lactose intolerance noted.',
    'Metabolic syndrome risk. Pre-diabetes. Overweight with hypothyroidism.',
    'Implement low-carb nutrition plan targeting 1600 kcal/day. Start Vitamin D and Omega-3 supplementation. Regular physical activity 4-5x/week. Monitor thyroid levels. Follow-up in 4 weeks.',
    '2024-12-01', 78.5, 'Fair - metabolic concerns'
);

-- Michael Chen - Initial consultation
INSERT INTO clinical_notes (
    id, client_id, nutritionist_id, consultation_date, consultation_type,
    chief_complaint, clinical_assessment, diagnosis_or_impression, plan_of_action,
    follow_up_date, weight_at_visit_kg, overall_health_status
)
VALUES (
    'cccccccc-cccc-cccc-cccc-cccccccccc02', 'd4444444-4444-4444-4444-444444444444',
    'b2222222-2222-2222-2222-222222222222', '2024-11-05 14:30:00', 'initial',
    'Competitive athlete seeking to gain lean muscle mass while training for marathon.',
    'Excellent baseline health. BMI 23.1, body fat 12.5%, very active. No medical concerns. Strong cardiovascular fitness.',
    'Healthy athletic individual seeking performance optimization.',
    'Implement high-protein plan with 3200 kcal/day. Protein supplementation post-workout. Creatine for strength. Monitor body composition bi-weekly. Adjust calories based on training volume.',
    '2024-12-05', 75.0, 'Excellent'
);

-- Emma Davis - Initial consultation
INSERT INTO clinical_notes (
    id, client_id, nutritionist_id, consultation_date, consultation_type,
    chief_complaint, clinical_assessment, diagnosis_or_impression, plan_of_action,
    follow_up_date, weight_at_visit_kg, overall_health_status
)
VALUES (
    'cccccccc-cccc-cccc-cccc-cccccccccc03', 'e5555555-5555-5555-5555-555555555555',
    'a1111111-1111-1111-1111-111111111111', '2024-11-10 09:00:00', 'initial',
    'Type 2 diabetic patient seeking better blood sugar control through diet.',
    'Established Type 2 diabetes (HbA1c 6.8%), hypertension controlled with medication. BMI 35.0 (obese). Currently on Metformin and Lisinopril. BP 135/88.',
    'Type 2 diabetes mellitus. Obesity. Hypertension. Good medication compliance.',
    'Implement diabetic nutrition plan 1800 kcal/day focused on low-GI foods. Add chromium and magnesium supplementation. Encourage moderate exercise. Coordinate with PCP for medication monitoring. Monthly follow-ups.',
    '2024-12-10', 92.0, 'Fair - chronic conditions managed'
);

-- Sarah Johnson - Follow-up
INSERT INTO clinical_notes (
    id, client_id, nutritionist_id, consultation_date, consultation_type,
    chief_complaint, clinical_assessment, diagnosis_or_impression, plan_of_action,
    follow_up_date, weight_at_visit_kg, overall_health_status
)
VALUES (
    'cccccccc-cccc-cccc-cccc-cccccccccc04', 'c3333333-3333-3333-3333-333333333333',
    'a1111111-1111-1111-1111-111111111111', '2024-12-01 10:00:00', 'follow_up',
    'Four-week follow-up. Patient reports good adherence to plan.',
    'Weight down to 76.2 kg (2.3 kg loss in 4 weeks). Patient reports increased energy, better sleep. Good compliance with meal plan and supplement regimen.',
    'Positive progress on weight loss plan.',
    'Continue current plan. Patient motivated and compliant. Encourage strength training. Next follow-up in 4 weeks.',
    '2025-01-01', 76.2, 'Good - showing improvement'
);
