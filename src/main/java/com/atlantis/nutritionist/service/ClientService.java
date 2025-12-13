package com.atlantis.nutritionist.service;

import com.atlantis.nutritionist.entity.Client;
import com.atlantis.nutritionist.entity.User;
import com.atlantis.nutritionist.exception.EntityNotFoundException;
import com.atlantis.nutritionist.repository.ClientRepository;
import com.atlantis.nutritionist.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Service class for Client entity operations.
 * Handles client profile management.
 */
@Service
@Transactional
public class ClientService {

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;

    public ClientService(ClientRepository clientRepository, UserRepository userRepository) {
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
    }

    /**
     * Create a new client profile.
     * @param userId User UUID
     * @param dateOfBirth Date of birth
     * @param gender Gender
     * @param bloodType Blood type
     * @param nationality Nationality
     * @return Created client
     */
    public Client createClient(UUID userId, LocalDate dateOfBirth, String gender,
                              String bloodType, String nationality) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));

        Client client = new Client();
        client.setUser(user);
        client.setDateOfBirth(dateOfBirth);
        client.setGender(gender);
        client.setBloodType(bloodType);
        client.setNationality(nationality);

        return clientRepository.save(client);
    }

    /**
     * Find client by ID.
     * @param clientId Client UUID
     * @return Client entity
     */
    @Transactional(readOnly = true)
    public Client findById(UUID clientId) {
        return clientRepository.findById(clientId)
            .orElseThrow(() -> new EntityNotFoundException("Client not found: " + clientId));
    }

    /**
     * Find client by user ID.
     * @param userId User UUID
     * @return Client entity
     */
    @Transactional(readOnly = true)
    public Client findByUserId(UUID userId) {
        return clientRepository.findByUserId(userId)
            .orElseThrow(() -> new EntityNotFoundException("Client not found for user: " + userId));
    }

    /**
     * Update client profile.
     * @param clientId Client UUID
     * @param dateOfBirth Date of birth
     * @param gender Gender
     * @param bloodType Blood type
     * @param nationality Nationality
     * @return Updated client
     */
    public Client updateClient(UUID clientId, LocalDate dateOfBirth, String gender,
                              String bloodType, String nationality) {
        Client client = findById(clientId);

        if (dateOfBirth != null) client.setDateOfBirth(dateOfBirth);
        if (gender != null) client.setGender(gender);
        if (bloodType != null) client.setBloodType(bloodType);
        if (nationality != null) client.setNationality(nationality);

        return clientRepository.save(client);
    }

    /**
     * Update emergency contact information.
     * @param clientId Client UUID
     * @param name Emergency contact name
     * @param phone Emergency contact phone
     * @param relationship Emergency contact relationship
     * @return Updated client
     */
    public Client updateEmergencyContact(UUID clientId, String name, String phone, String relationship) {
        Client client = findById(clientId);

        client.setEmergencyContactName(name);
        client.setEmergencyContactPhone(phone);
        client.setEmergencyContactRelationship(relationship);

        return clientRepository.save(client);
    }

    /**
     * Get all clients.
     * @return List of all clients
     */
    @Transactional(readOnly = true)
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    /**
     * Delete client.
     * @param clientId Client UUID
     */
    public void deleteClient(UUID clientId) {
        Client client = findById(clientId);
        clientRepository.delete(client);
    }
}
