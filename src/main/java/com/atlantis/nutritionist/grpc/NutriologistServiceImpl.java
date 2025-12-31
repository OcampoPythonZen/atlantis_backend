package com.atlantis.nutritionist.grpc;

import com.atlantis.nutritionist.entity.Nutriologist;
import com.atlantis.nutritionist.entity.User;
import com.atlantis.nutritionist.exception.EntityAlreadyExistsException;
import com.atlantis.nutritionist.exception.EntityNotFoundException;
import com.atlantis.nutritionist.exception.ValidationException;
import com.atlantis.nutritionist.grpc.common.Empty;
import com.atlantis.nutritionist.grpc.common.UserInfo;
import com.atlantis.nutritionist.grpc.nutriologist.*;
import com.atlantis.nutritionist.repository.NutriologistRepository;
import com.atlantis.nutritionist.service.NutriologistService;
import com.atlantis.nutritionist.validation.RequestValidator;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * gRPC service implementation for Nutriologist operations.
 * Handles all nutritionist profile management endpoints with full database integration.
 */
@GrpcService
public class NutriologistServiceImpl extends NutriologistServiceGrpc.NutriologistServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(NutriologistServiceImpl.class);

    private final NutriologistService nutriologistService;
    private final NutriologistRepository nutriologistRepository;

    public NutriologistServiceImpl(NutriologistService nutriologistService,
                                   NutriologistRepository nutriologistRepository) {
        this.nutriologistService = nutriologistService;
        this.nutriologistRepository = nutriologistRepository;
    }

    @Override
    public void createNutriologist(CreateNutriologistRequest request, StreamObserver<NutriologistResponse> responseObserver) {
        log.info("Creating nutriologist for user ID: {}", request.getUserId().getValue());

        try {
            // Validate input
            Map<String, String> errors = new HashMap<>();
            RequestValidator.validateRequired("user_id", request.getUserId(), errors);
            RequestValidator.throwIfErrors(errors);

            UUID userId = UUID.fromString(request.getUserId().getValue());

            // Check if nutriologist already exists for this user
            if (nutriologistRepository.existsByUserId(userId)) {
                throw new EntityAlreadyExistsException("Nutriologist profile already exists for user: " + userId);
            }

            // Check if professional license already exists
            if (request.hasProfessionalLicense()) {
                nutriologistRepository.findByProfessionalLicense(request.getProfessionalLicense())
                    .ifPresent(n -> {
                        throw new EntityAlreadyExistsException(
                            "Professional license already registered: " + request.getProfessionalLicense());
                    });
            }

            // Create nutriologist
            Nutriologist nutriologist = nutriologistService.createNutriologist(
                userId,
                request.hasProfessionalLicense() ? request.getProfessionalLicense() : null,
                request.hasSpecialization() ? request.getSpecialization() : null,
                request.hasBio() ? request.getBio() : null,
                request.hasClinicName() ? request.getClinicName() : null
            );

            // Update clinic address if provided
            if (request.hasAddress() || request.hasCity() || request.hasState() || request.hasCountry()) {
                nutriologist = nutriologistService.updateClinicAddress(
                    nutriologist.getId(),
                    request.hasAddress() ? request.getAddress() : null,
                    request.hasCity() ? request.getCity() : null,
                    request.hasState() ? request.getState() : null,
                    request.hasCountry() ? request.getCountry() : null
                );
            }

            // Update additional fields
            if (request.hasPhoneClinic()) {
                nutriologist.setPhoneClinic(request.getPhoneClinic());
            }
            if (request.hasYearsExperience()) {
                nutriologist.setYearsExperience(request.getYearsExperience());
            }

            if (request.hasPhoneClinic() || request.hasYearsExperience()) {
                nutriologist = nutriologistRepository.save(nutriologist);
            }

            log.info("Nutriologist created successfully with ID: {}", nutriologist.getId());

            // Build response
            NutriologistResponse response = NutriologistResponse.newBuilder()
                    .setNutriologist(toProto(nutriologist))
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (ValidationException | EntityAlreadyExistsException | EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error creating nutriologist", e);
            throw new ValidationException("Failed to create nutriologist: " + e.getMessage());
        }
    }

    @Override
    public void getNutriologist(GetNutriologistRequest request, StreamObserver<NutriologistResponse> responseObserver) {
        log.info("Getting nutriologist with ID: {}", request.getId().getValue());

        try {
            // Validate input
            Map<String, String> errors = new HashMap<>();
            RequestValidator.validateRequired("id", request.getId(), errors);
            RequestValidator.throwIfErrors(errors);

            UUID nutriologistId = UUID.fromString(request.getId().getValue());

            // Find nutriologist
            Nutriologist nutriologist = nutriologistService.findById(nutriologistId);

            log.info("Nutriologist retrieved successfully: {}", nutriologistId);

            // Build response
            NutriologistResponse response = NutriologistResponse.newBuilder()
                    .setNutriologist(toProto(nutriologist))
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (ValidationException | EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error getting nutriologist", e);
            throw new ValidationException("Failed to get nutriologist: " + e.getMessage());
        }
    }

    @Override
    public void updateNutriologist(UpdateNutriologistRequest request, StreamObserver<NutriologistResponse> responseObserver) {
        log.info("Updating nutriologist with ID: {}", request.getId().getValue());

        try {
            // Validate input
            Map<String, String> errors = new HashMap<>();
            RequestValidator.validateRequired("id", request.getId(), errors);
            RequestValidator.throwIfErrors(errors);

            UUID nutriologistId = UUID.fromString(request.getId().getValue());

            // Get existing nutriologist
            Nutriologist nutriologist = nutriologistService.findById(nutriologistId);

            // Update professional license if provided
            if (request.hasProfessionalLicense()) {
                String newLicense = request.getProfessionalLicense();
                if (!newLicense.equals(nutriologist.getProfessionalLicense())) {
                    // Check if new license already exists
                    nutriologistRepository.findByProfessionalLicense(newLicense)
                        .ifPresent(n -> {
                            throw new EntityAlreadyExistsException(
                                "Professional license already registered: " + newLicense);
                        });
                    nutriologist.setProfessionalLicense(newLicense);
                }
            }

            // Update basic fields
            String specialization = request.hasSpecialization() ? request.getSpecialization() : null;
            String bio = request.hasBio() ? request.getBio() : null;
            String clinicName = request.hasClinicName() ? request.getClinicName() : null;
            Integer yearsExperience = request.hasYearsExperience() ? request.getYearsExperience() : null;

            if (specialization != null || bio != null || clinicName != null || yearsExperience != null) {
                nutriologist = nutriologistService.updateNutriologist(
                    nutriologistId, specialization, bio, clinicName, yearsExperience);
            }

            // Update clinic address if any field is provided
            if (request.hasAddress() || request.hasCity() || request.hasState() || request.hasCountry()) {
                nutriologist = nutriologistService.updateClinicAddress(
                    nutriologistId,
                    request.hasAddress() ? request.getAddress() : nutriologist.getAddress(),
                    request.hasCity() ? request.getCity() : nutriologist.getCity(),
                    request.hasState() ? request.getState() : nutriologist.getState(),
                    request.hasCountry() ? request.getCountry() : nutriologist.getCountry()
                );
            }

            // Update additional optional fields
            boolean needsSave = false;
            if (request.hasPhoneClinic()) {
                nutriologist.setPhoneClinic(request.getPhoneClinic());
                needsSave = true;
            }
            if (request.hasProfileImageUrl()) {
                nutriologist.setProfileImageUrl(request.getProfileImageUrl());
                needsSave = true;
            }
            if (request.hasVerified()) {
                nutriologist.setVerified(request.getVerified());
                needsSave = true;
            }

            if (needsSave) {
                nutriologist = nutriologistRepository.save(nutriologist);
            }

            log.info("Nutriologist updated successfully: {}", nutriologistId);

            // Build response
            NutriologistResponse response = NutriologistResponse.newBuilder()
                    .setNutriologist(toProto(nutriologist))
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (ValidationException | EntityNotFoundException | EntityAlreadyExistsException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error updating nutriologist", e);
            throw new ValidationException("Failed to update nutriologist: " + e.getMessage());
        }
    }

    @Override
    public void deleteNutriologist(DeleteNutriologistRequest request, StreamObserver<Empty> responseObserver) {
        log.info("Deleting nutriologist with ID: {}", request.getId().getValue());

        try {
            // Validate input
            Map<String, String> errors = new HashMap<>();
            RequestValidator.validateRequired("id", request.getId(), errors);
            RequestValidator.throwIfErrors(errors);

            UUID nutriologistId = UUID.fromString(request.getId().getValue());

            // Delete nutriologist
            nutriologistService.deleteNutriologist(nutriologistId);

            log.info("Nutriologist deleted successfully: {}", nutriologistId);

            // Return empty response
            Empty response = Empty.newBuilder().build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (ValidationException | EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error deleting nutriologist", e);
            throw new ValidationException("Failed to delete nutriologist: " + e.getMessage());
        }
    }

    @Override
    public void listNutriologists(ListNutriologistsRequest request, StreamObserver<ListNutriologistsResponse> responseObserver) {
        log.info("Listing nutriologists with pagination - page: {}, size: {}, verified_only: {}",
                request.hasPageRequest() ? request.getPageRequest().getPage() : 0,
                request.hasPageRequest() ? request.getPageRequest().getSize() : 10,
                request.hasVerifiedOnly() ? request.getVerifiedOnly() : false);

        try {
            // Get nutriologists (filtered by verified if requested)
            List<Nutriologist> nutriologists;
            if (request.hasVerifiedOnly() && request.getVerifiedOnly()) {
                nutriologists = nutriologistService.findAllVerified();
            } else {
                nutriologists = nutriologistService.findAll();
            }

            // Convert to proto
            List<com.atlantis.nutritionist.grpc.nutriologist.Nutriologist> protoNutriologists =
                    nutriologists.stream()
                            .map(this::toProto)
                            .collect(Collectors.toList());

            log.info("Listed {} nutriologists", nutriologists.size());

            // Build response
            ListNutriologistsResponse response = ListNutriologistsResponse.newBuilder()
                    .addAllNutriologists(protoNutriologists)
                    .setPageResponse(com.atlantis.nutritionist.grpc.common.PageResponse.newBuilder()
                            .setTotalPages(1)
                            .setTotalElements(nutriologists.size())
                            .setCurrentPage(0)
                            .setSize(nutriologists.size())
                            .build())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            log.error("Error listing nutriologists", e);
            throw new ValidationException("Failed to list nutriologists: " + e.getMessage());
        }
    }

    @Override
    public void getNutriologistByUserId(GetNutriologistByUserIdRequest request, StreamObserver<NutriologistResponse> responseObserver) {
        log.info("Getting nutriologist by user ID: {}", request.getUserId().getValue());

        try {
            // Validate input
            Map<String, String> errors = new HashMap<>();
            RequestValidator.validateRequired("user_id", request.getUserId(), errors);
            RequestValidator.throwIfErrors(errors);

            UUID userId = UUID.fromString(request.getUserId().getValue());

            // Find nutriologist by user ID
            Nutriologist nutriologist = nutriologistService.findByUserId(userId);

            log.info("Nutriologist retrieved successfully by user ID: {}", userId);

            // Build response
            NutriologistResponse response = NutriologistResponse.newBuilder()
                    .setNutriologist(toProto(nutriologist))
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (ValidationException | EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error getting nutriologist by user ID", e);
            throw new ValidationException("Failed to get nutriologist by user ID: " + e.getMessage());
        }
    }

    @Override
    public void searchNutriologists(SearchNutriologistsRequest request, StreamObserver<ListNutriologistsResponse> responseObserver) {
        log.info("Searching nutriologists - specialization: {}, city: {}, state: {}, verified_only: {}",
                request.hasSpecialization() ? request.getSpecialization() : "any",
                request.hasCity() ? request.getCity() : "any",
                request.hasState() ? request.getState() : "any",
                request.hasVerifiedOnly() ? request.getVerifiedOnly() : false);

        try {
            // Start with all nutriologists or verified only
            List<Nutriologist> nutriologists;
            if (request.hasVerifiedOnly() && request.getVerifiedOnly()) {
                nutriologists = nutriologistService.findAllVerified();
            } else {
                nutriologists = nutriologistService.findAll();
            }

            // Apply filters in memory (TODO: move to repository with native queries for better performance)
            if (request.hasSpecialization()) {
                String specialization = request.getSpecialization().toLowerCase();
                nutriologists = nutriologists.stream()
                    .filter(n -> n.getSpecialization() != null &&
                                 n.getSpecialization().toLowerCase().contains(specialization))
                    .collect(Collectors.toList());
            }

            if (request.hasCity()) {
                String city = request.getCity().toLowerCase();
                nutriologists = nutriologists.stream()
                    .filter(n -> n.getCity() != null &&
                                 n.getCity().toLowerCase().contains(city))
                    .collect(Collectors.toList());
            }

            if (request.hasState()) {
                String state = request.getState().toLowerCase();
                nutriologists = nutriologists.stream()
                    .filter(n -> n.getState() != null &&
                                 n.getState().toLowerCase().contains(state))
                    .collect(Collectors.toList());
            }

            // Convert to proto
            List<com.atlantis.nutritionist.grpc.nutriologist.Nutriologist> protoNutriologists =
                    nutriologists.stream()
                            .map(this::toProto)
                            .collect(Collectors.toList());

            log.info("Search returned {} nutriologists", nutriologists.size());

            // Build response
            ListNutriologistsResponse response = ListNutriologistsResponse.newBuilder()
                    .addAllNutriologists(protoNutriologists)
                    .setPageResponse(com.atlantis.nutritionist.grpc.common.PageResponse.newBuilder()
                            .setTotalPages(1)
                            .setTotalElements(nutriologists.size())
                            .setCurrentPage(0)
                            .setSize(nutriologists.size())
                            .build())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            log.error("Error searching nutriologists", e);
            throw new ValidationException("Failed to search nutriologists: " + e.getMessage());
        }
    }

    /**
     * Convert Nutriologist entity to proto message.
     */
    private com.atlantis.nutritionist.grpc.nutriologist.Nutriologist toProto(Nutriologist nutriologist) {
        var builder = com.atlantis.nutritionist.grpc.nutriologist.Nutriologist.newBuilder()
                .setId(com.atlantis.nutritionist.grpc.common.UUID.newBuilder()
                        .setValue(nutriologist.getId().toString())
                        .build())
                .setUserId(com.atlantis.nutritionist.grpc.common.UUID.newBuilder()
                        .setValue(nutriologist.getUser().getId().toString())
                        .build())
                .setVerified(nutriologist.getVerified() != null ? nutriologist.getVerified() : false)
                .setCreatedAt(com.atlantis.nutritionist.grpc.common.Timestamp.newBuilder()
                        .setSeconds(nutriologist.getCreatedAt().getEpochSecond())
                        .setNanos(nutriologist.getCreatedAt().getNano())
                        .build())
                .setUpdatedAt(com.atlantis.nutritionist.grpc.common.Timestamp.newBuilder()
                        .setSeconds(nutriologist.getUpdatedAt().getEpochSecond())
                        .setNanos(nutriologist.getUpdatedAt().getNano())
                        .build());

        // Add optional fields
        if (nutriologist.getProfessionalLicense() != null) {
            builder.setProfessionalLicense(nutriologist.getProfessionalLicense());
        }
        if (nutriologist.getSpecialization() != null) {
            builder.setSpecialization(nutriologist.getSpecialization());
        }
        if (nutriologist.getBio() != null) {
            builder.setBio(nutriologist.getBio());
        }
        if (nutriologist.getProfileImageUrl() != null) {
            builder.setProfileImageUrl(nutriologist.getProfileImageUrl());
        }
        if (nutriologist.getClinicName() != null) {
            builder.setClinicName(nutriologist.getClinicName());
        }
        if (nutriologist.getAddress() != null) {
            builder.setAddress(nutriologist.getAddress());
        }
        if (nutriologist.getCity() != null) {
            builder.setCity(nutriologist.getCity());
        }
        if (nutriologist.getState() != null) {
            builder.setState(nutriologist.getState());
        }
        if (nutriologist.getCountry() != null) {
            builder.setCountry(nutriologist.getCountry());
        }
        if (nutriologist.getPhoneClinic() != null) {
            builder.setPhoneClinic(nutriologist.getPhoneClinic());
        }
        if (nutriologist.getYearsExperience() != null) {
            builder.setYearsExperience(nutriologist.getYearsExperience());
        }

        // Add user info
        User user = nutriologist.getUser();
        UserInfo userInfo = UserInfo.newBuilder()
                .setEmail(user.getEmail())
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setPhone(user.getPhone() != null ? user.getPhone() : "")
                .build();
        builder.setUserInfo(userInfo);

        return builder.build();
    }
}
