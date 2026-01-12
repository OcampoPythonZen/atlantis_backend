-- ============================================
-- FIX SEED DATA PASSWORDS
-- ============================================
-- The original seed data (V2) had an incorrect BCrypt hash.
-- This migration updates all seed users with the correct hash for "password123"
-- BCrypt hash generated with cost factor 10 (compatible with Spring Security BCryptPasswordEncoder)

UPDATE users
SET password = '$2b$10$ykKqBdzuQLsfKxf7GHpvSe1S9dFR0/ynL.eJUid25QbSRhvlX.pT6'
WHERE email IN (
    'maria.rodriguez@nutrition.com',
    'john.smith@nutrition.com',
    'sarah.johnson@email.com',
    'michael.chen@email.com',
    'emma.davis@email.com'
);
