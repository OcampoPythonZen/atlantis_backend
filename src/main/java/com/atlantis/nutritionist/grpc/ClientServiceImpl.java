package com.atlantis.nutritionist.grpc;

import com.atlantis.nutritionist.entity.Client;
import com.atlantis.nutritionist.entity.User;
import com.atlantis.nutritionist.exception.EntityAlreadyExistsException;
import com.atlantis.nutritionist.exception.EntityNotFoundException;
import com.atlantis.nutritionist.exception.ValidationException;
import com.atlantis.nutritionist.grpc.client.*;
import com.atlantis.nutritionist.grpc.common.Empty;
import com.atlantis.nutritionist.grpc.common.Gender;
import com.atlantis.nutritionist.grpc.common.UserInfo;
import com.atlantis.nutritionist.repository.ClientRepository;
import com.atlantis.nutritionist.service.ClientService;
import com.atlantis.nutritionist.validation.RequestValidator;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * gRPC service implementation for Client (Patient) operations.
 * Handles all patient/client profile management endpoints with full database integration.
 */
@GrpcService
public class ClientServiceImpl extends ClientServiceGrpc.ClientServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(ClientServiceImpl.class);

    private final ClientService clientService;
    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientService clientService, ClientRepository clientRepository) {
        this.clientService = clientService;
        this.clientRepository = clientRepository;
    }

    @Override
    public void createClient(CreateClientRequest request, StreamObserver<ClientResponse> responseObserver) {
        log.info("Creating client for user ID: {}", request.getUserId().getValue());

        try {
            // Validate input
            Map<String, String> errors = new HashMap<>();
            RequestValidator.validateRequired("user_id", request.getUserId(), errors);
            RequestValidator.validateRequired("date_of_birth", request.getDateOfBirth(), errors);
            RequestValidator.validateRequired("gender", request.getGender(), errors);
            RequestValidator.throwIfErrors(errors);

            UUID userId = UUID.fromString(request.getUserId().getValue());

            // Check if client already exists for this user
            if (clientRepository.existsByUserId(userId)) {
                throw new EntityAlreadyExistsException("Client profile already exists for user: " + userId);
            }

            // Parse date of birth
            LocalDate dateOfBirth = LocalDate.parse(request.getDateOfBirth());

            // Convert gender enum to string
            String gender = convertGenderToString(request.getGender());

            // Create client
            Client client = clientService.createClient(
                userId,
                dateOfBirth,
                gender,
                request.hasBloodType() ? request.getBloodType() : null,
                request.hasNationality() ? request.getNationality() : null
            );

            // Update emergency contact if provided
            if (request.hasEmergencyContactName() || request.hasEmergencyContactPhone() ||
                request.hasEmergencyContactRelationship()) {
                client = clientService.updateEmergencyContact(
                    client.getId(),
                    request.hasEmergencyContactName() ? request.getEmergencyContactName() : null,
                    request.hasEmergencyContactPhone() ? request.getEmergencyContactPhone() : null,
                    request.hasEmergencyContactRelationship() ? request.getEmergencyContactRelationship() : null
                );
            }

            // Update Rh factor if provided
            if (request.hasRhFactor()) {
                client.setRhFactor(request.getRhFactor());
                client = clientRepository.save(client);
            }

            log.info("Client created successfully with ID: {}", client.getId());

            // Build response
            ClientResponse response = ClientResponse.newBuilder()
                    .setClient(toProto(client))
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (ValidationException | EntityAlreadyExistsException | EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error creating client", e);
            throw new ValidationException("Failed to create client: " + e.getMessage());
        }
    }

    @Override
    public void getClient(GetClientRequest request, StreamObserver<ClientResponse> responseObserver) {
        log.info("Getting client with ID: {}", request.getId().getValue());

        try {
            // Validate input
            Map<String, String> errors = new HashMap<>();
            RequestValidator.validateRequired("id", request.getId(), errors);
            RequestValidator.throwIfErrors(errors);

            UUID clientId = UUID.fromString(request.getId().getValue());

            // Find client
            Client client = clientService.findById(clientId);

            log.info("Client retrieved successfully: {}", clientId);

            // Build response
            ClientResponse response = ClientResponse.newBuilder()
                    .setClient(toProto(client))
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (ValidationException | EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error getting client", e);
            throw new ValidationException("Failed to get client: " + e.getMessage());
        }
    }

    @Override
    public void updateClient(UpdateClientRequest request, StreamObserver<ClientResponse> responseObserver) {
        log.info("Updating client with ID: {}", request.getId().getValue());

        try {
            // Validate input
            Map<String, String> errors = new HashMap<>();
            RequestValidator.validateRequired("id", request.getId(), errors);
            RequestValidator.throwIfErrors(errors);

            UUID clientId = UUID.fromString(request.getId().getValue());

            // Get existing client
            Client client = clientService.findById(clientId);

            // Update basic fields
            LocalDate dateOfBirth = request.hasDateOfBirth() ? LocalDate.parse(request.getDateOfBirth()) : null;
            String gender = request.hasGender() ? convertGenderToString(request.getGender()) : null;
            String bloodType = request.hasBloodType() ? request.getBloodType() : null;
            String nationality = request.hasNationality() ? request.getNationality() : null;

            if (dateOfBirth != null || gender != null || bloodType != null || nationality != null) {
                client = clientService.updateClient(clientId, dateOfBirth, gender, bloodType, nationality);
            }

            // Update Rh factor if provided
            if (request.hasRhFactor()) {
                client.setRhFactor(request.getRhFactor());
                client = clientRepository.save(client);
            }

            // Update emergency contact if any field is provided
            if (request.hasEmergencyContactName() || request.hasEmergencyContactPhone() ||
                request.hasEmergencyContactRelationship()) {
                client = clientService.updateEmergencyContact(
                    clientId,
                    request.hasEmergencyContactName() ? request.getEmergencyContactName() : client.getEmergencyContactName(),
                    request.hasEmergencyContactPhone() ? request.getEmergencyContactPhone() : client.getEmergencyContactPhone(),
                    request.hasEmergencyContactRelationship() ? request.getEmergencyContactRelationship() : client.getEmergencyContactRelationship()
                );
            }

            log.info("Client updated successfully: {}", clientId);

            // Build response
            ClientResponse response = ClientResponse.newBuilder()
                    .setClient(toProto(client))
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (ValidationException | EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error updating client", e);
            throw new ValidationException("Failed to update client: " + e.getMessage());
        }
    }

    @Override
    public void deleteClient(DeleteClientRequest request, StreamObserver<Empty> responseObserver) {
        log.info("Deleting client with ID: {}", request.getId().getValue());

        try {
            // Validate input
            Map<String, String> errors = new HashMap<>();
            RequestValidator.validateRequired("id", request.getId(), errors);
            RequestValidator.throwIfErrors(errors);

            UUID clientId = UUID.fromString(request.getId().getValue());

            // Delete client
            clientService.deleteClient(clientId);

            log.info("Client deleted successfully: {}", clientId);

            // Return empty response
            Empty response = Empty.newBuilder().build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (ValidationException | EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error deleting client", e);
            throw new ValidationException("Failed to delete client: " + e.getMessage());
        }
    }

    @Override
    public void listClients(ListClientsRequest request, StreamObserver<ListClientsResponse> responseObserver) {
        log.info("Listing clients with pagination - page: {}, size: {}",
                request.hasPageRequest() ? request.getPageRequest().getPage() : 0,
                request.hasPageRequest() ? request.getPageRequest().getSize() : 10);

        try {
            // Get all clients (TODO: implement pagination in future)
            List<Client> clients = clientService.findAll();

            // Convert to proto
            List<com.atlantis.nutritionist.grpc.client.Client> protoClients = clients.stream()
                    .map(this::toProto)
                    .collect(Collectors.toList());

            log.info("Listed {} clients", clients.size());

            // Build response
            ListClientsResponse response = ListClientsResponse.newBuilder()
                    .addAllClients(protoClients)
                    .setPageResponse(com.atlantis.nutritionist.grpc.common.PageResponse.newBuilder()
                            .setTotalPages(1)
                            .setTotalElements(clients.size())
                            .setCurrentPage(0)
                            .setSize(clients.size())
                            .build())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            log.error("Error listing clients", e);
            throw new ValidationException("Failed to list clients: " + e.getMessage());
        }
    }

    @Override
    public void getClientByUserId(GetClientByUserIdRequest request, StreamObserver<ClientResponse> responseObserver) {
        log.info("Getting client by user ID: {}", request.getUserId().getValue());

        try {
            // Validate input
            Map<String, String> errors = new HashMap<>();
            RequestValidator.validateRequired("user_id", request.getUserId(), errors);
            RequestValidator.throwIfErrors(errors);

            UUID userId = UUID.fromString(request.getUserId().getValue());

            // Find client by user ID
            Client client = clientService.findByUserId(userId);

            log.info("Client retrieved successfully by user ID: {}", userId);

            // Build response
            ClientResponse response = ClientResponse.newBuilder()
                    .setClient(toProto(client))
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (ValidationException | EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error getting client by user ID", e);
            throw new ValidationException("Failed to get client by user ID: " + e.getMessage());
        }
    }

    /**
     * Convert Client entity to proto message.
     */
    private com.atlantis.nutritionist.grpc.client.Client toProto(Client client) {
        var builder = com.atlantis.nutritionist.grpc.client.Client.newBuilder()
                .setId(com.atlantis.nutritionist.grpc.common.UUID.newBuilder()
                        .setValue(client.getId().toString())
                        .build())
                .setUserId(com.atlantis.nutritionist.grpc.common.UUID.newBuilder()
                        .setValue(client.getUser().getId().toString())
                        .build())
                .setDateOfBirth(client.getDateOfBirth().toString())
                .setGender(convertStringToGender(client.getGender()))
                .setCreatedAt(com.atlantis.nutritionist.grpc.common.Timestamp.newBuilder()
                        .setSeconds(client.getCreatedAt().getEpochSecond())
                        .setNanos(client.getCreatedAt().getNano())
                        .build())
                .setUpdatedAt(com.atlantis.nutritionist.grpc.common.Timestamp.newBuilder()
                        .setSeconds(client.getUpdatedAt().getEpochSecond())
                        .setNanos(client.getUpdatedAt().getNano())
                        .build());

        // Add optional fields
        if (client.getBloodType() != null) {
            builder.setBloodType(client.getBloodType());
        }
        if (client.getRhFactor() != null) {
            builder.setRhFactor(client.getRhFactor());
        }
        if (client.getNationality() != null) {
            builder.setNationality(client.getNationality());
        }
        if (client.getEmergencyContactName() != null) {
            builder.setEmergencyContactName(client.getEmergencyContactName());
        }
        if (client.getEmergencyContactPhone() != null) {
            builder.setEmergencyContactPhone(client.getEmergencyContactPhone());
        }
        if (client.getEmergencyContactRelationship() != null) {
            builder.setEmergencyContactRelationship(client.getEmergencyContactRelationship());
        }

        // Add user info
        User user = client.getUser();
        UserInfo userInfo = UserInfo.newBuilder()
                .setEmail(user.getEmail())
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setPhone(user.getPhone() != null ? user.getPhone() : "")
                .build();
        builder.setUserInfo(userInfo);

        return builder.build();
    }

    /**
     * Convert Gender enum to string for database.
     */
    private String convertGenderToString(Gender gender) {
        return switch (gender) {
            case MALE -> "MALE";
            case FEMALE -> "FEMALE";
            case OTHER -> "OTHER";
            default -> throw new ValidationException("Invalid gender: " + gender);
        };
    }

    /**
     * Convert string to Gender enum for proto.
     */
    private Gender convertStringToGender(String gender) {
        if (gender == null) {
            return Gender.GENDER_UNSPECIFIED;
        }
        return switch (gender.toUpperCase()) {
            case "MALE" -> Gender.MALE;
            case "FEMALE" -> Gender.FEMALE;
            case "OTHER" -> Gender.OTHER;
            default -> Gender.GENDER_UNSPECIFIED;
        };
    }
}
