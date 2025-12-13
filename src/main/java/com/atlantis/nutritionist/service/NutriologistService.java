package com.atlantis.nutritionist.service;

import com.atlantis.nutritionist.entity.Nutriologist;
import com.atlantis.nutritionist.entity.User;
import com.atlantis.nutritionist.exception.EntityNotFoundException;
import com.atlantis.nutritionist.repository.NutriologistRepository;
import com.atlantis.nutritionist.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Service class for Nutriologist entity operations.
 * Handles nutritionist profile management.
 */
@Service
@Transactional
public class NutriologistService {

    private final NutriologistRepository nutriologistRepository;
    private final UserRepository userRepository;

    public NutriologistService(NutriologistRepository nutriologistRepository,
                               UserRepository userRepository) {
        this.nutriologistRepository = nutriologistRepository;
        this.userRepository = userRepository;
    }

    /**
     * Create a new nutriologist profile.
     * @param userId User UUID
     * @param professionalLicense Professional license number
     * @param specialization Specialization
     * @param bio Biography
     * @param clinicName Clinic name
     * @return Created nutriologist
     */
    public Nutriologist createNutriologist(UUID userId, String professionalLicense,
                                          String specialization, String bio, String clinicName) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));

        Nutriologist nutriologist = new Nutriologist();
        nutriologist.setUser(user);
        nutriologist.setProfessionalLicense(professionalLicense);
        nutriologist.setSpecialization(specialization);
        nutriologist.setBio(bio);
        nutriologist.setClinicName(clinicName);

        return nutriologistRepository.save(nutriologist);
    }

    /**
     * Find nutriologist by ID.
     * @param nutriologistId Nutriologist UUID
     * @return Nutriologist entity
     */
    @Transactional(readOnly = true)
    public Nutriologist findById(UUID nutriologistId) {
        return nutriologistRepository.findById(nutriologistId)
            .orElseThrow(() -> new EntityNotFoundException("Nutriologist not found: " + nutriologistId));
    }

    /**
     * Find nutriologist by user ID.
     * @param userId User UUID
     * @return Nutriologist entity
     */
    @Transactional(readOnly = true)
    public Nutriologist findByUserId(UUID userId) {
        return nutriologistRepository.findByUserId(userId)
            .orElseThrow(() -> new EntityNotFoundException("Nutriologist not found for user: " + userId));
    }

    /**
     * Update nutriologist profile.
     * @param nutriologistId Nutriologist UUID
     * @param specialization Specialization
     * @param bio Biography
     * @param clinicName Clinic name
     * @param yearsExperience Years of experience
     * @return Updated nutriologist
     */
    public Nutriologist updateNutriologist(UUID nutriologistId, String specialization,
                                          String bio, String clinicName, Integer yearsExperience) {
        Nutriologist nutriologist = findById(nutriologistId);

        if (specialization != null) nutriologist.setSpecialization(specialization);
        if (bio != null) nutriologist.setBio(bio);
        if (clinicName != null) nutriologist.setClinicName(clinicName);
        if (yearsExperience != null) nutriologist.setYearsExperience(yearsExperience);

        return nutriologistRepository.save(nutriologist);
    }

    /**
     * Update clinic address.
     * @param nutriologistId Nutriologist UUID
     * @param address Street address
     * @param city City
     * @param state State
     * @param country Country
     * @return Updated nutriologist
     */
    public Nutriologist updateClinicAddress(UUID nutriologistId, String address,
                                           String city, String state, String country) {
        Nutriologist nutriologist = findById(nutriologistId);

        if (address != null) nutriologist.setAddress(address);
        if (city != null) nutriologist.setCity(city);
        if (state != null) nutriologist.setState(state);
        if (country != null) nutriologist.setCountry(country);

        return nutriologistRepository.save(nutriologist);
    }

    /**
     * Verify nutriologist account.
     * @param nutriologistId Nutriologist UUID
     */
    public void verifyNutriologist(UUID nutriologistId) {
        Nutriologist nutriologist = findById(nutriologistId);
        nutriologist.setVerified(true);
        nutriologistRepository.save(nutriologist);
    }

    /**
     * Get all verified nutriologists.
     * @return List of verified nutriologists
     */
    @Transactional(readOnly = true)
    public List<Nutriologist> findAllVerified() {
        return nutriologistRepository.findByVerifiedTrue();
    }

    /**
     * Get all nutriologists.
     * @return List of all nutriologists
     */
    @Transactional(readOnly = true)
    public List<Nutriologist> findAll() {
        return nutriologistRepository.findAll();
    }

    /**
     * Delete nutriologist.
     * @param nutriologistId Nutriologist UUID
     */
    public void deleteNutriologist(UUID nutriologistId) {
        Nutriologist nutriologist = findById(nutriologistId);
        nutriologistRepository.delete(nutriologist);
    }
}
