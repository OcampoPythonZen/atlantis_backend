package com.atlantis.nutritionist.grpc;

import com.atlantis.nutritionist.entity.Client;
import com.atlantis.nutritionist.entity.Measurement;
import com.atlantis.nutritionist.entity.Nutriologist;
import com.atlantis.nutritionist.exception.EntityNotFoundException;
import com.atlantis.nutritionist.exception.ValidationException;
import com.atlantis.nutritionist.grpc.common.Empty;
import com.atlantis.nutritionist.grpc.common.PageResponse;
import com.atlantis.nutritionist.grpc.common.Timestamp;
import com.atlantis.nutritionist.grpc.common.UUID;
import com.atlantis.nutritionist.grpc.measurement.*;
import com.atlantis.nutritionist.repository.ClientRepository;
import com.atlantis.nutritionist.repository.MeasurementRepository;
import com.atlantis.nutritionist.repository.NutriologistRepository;
import com.atlantis.nutritionist.validation.RequestValidator;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * gRPC service implementation for Measurement operations.
 * Handles biometric measurements and lab results tracking with full database integration.
 */
@GrpcService
public class MeasurementServiceImpl extends MeasurementServiceGrpc.MeasurementServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(MeasurementServiceImpl.class);

    private final MeasurementRepository measurementRepository;
    private final ClientRepository clientRepository;
    private final NutriologistRepository nutriologistRepository;

    public MeasurementServiceImpl(MeasurementRepository measurementRepository,
                                   ClientRepository clientRepository,
                                   NutriologistRepository nutriologistRepository) {
        this.measurementRepository = measurementRepository;
        this.clientRepository = clientRepository;
        this.nutriologistRepository = nutriologistRepository;
    }

    @Override
    public void createMeasurement(CreateMeasurementRequest request, StreamObserver<MeasurementResponse> responseObserver) {
        log.info("Creating measurement for client ID: {} by nutritionist ID: {}",
                request.getClientId().getValue(),
                request.getNutritionistId().getValue());

        try {
            // Validate input
            Map<String, String> errors = new HashMap<>();
            RequestValidator.validateRequired("client_id", request.getClientId(), errors);
            RequestValidator.validateRequired("nutritionist_id", request.getNutritionistId(), errors);
            RequestValidator.throwIfErrors(errors);

            java.util.UUID clientId = java.util.UUID.fromString(request.getClientId().getValue());
            java.util.UUID nutritionistId = java.util.UUID.fromString(request.getNutritionistId().getValue());

            // Validate client exists
            Client client = clientRepository.findById(clientId)
                    .orElseThrow(() -> new EntityNotFoundException("Client not found: " + clientId));

            // Validate nutritionist exists
            Nutriologist nutritionist = nutriologistRepository.findById(nutritionistId)
                    .orElseThrow(() -> new EntityNotFoundException("Nutritionist not found: " + nutritionistId));

            // Create measurement entity
            Measurement measurement = new Measurement();
            measurement.setClient(client);
            measurement.setNutritionist(nutritionist);
            measurement.setMeasurementDate(Instant.now());

            // Set basic measurements
            if (request.hasWeightKg()) {
                measurement.setWeightKg(BigDecimal.valueOf(request.getWeightKg()));
            }
            if (request.hasHeightCm()) {
                measurement.setHeightCm(BigDecimal.valueOf(request.getHeightCm()));
            }

            // Calculate IMC if height and weight are provided
            if (request.hasWeightKg() && request.hasHeightCm()) {
                double heightM = request.getHeightCm() / 100.0;
                double imc = request.getWeightKg() / (heightM * heightM);
                measurement.setImc(BigDecimal.valueOf(imc));
            }

            // Set body composition
            if (request.hasBodyFatPercentage()) {
                measurement.setBodyFatPercentage(BigDecimal.valueOf(request.getBodyFatPercentage()));
            }
            if (request.hasMuscleMassKg()) {
                measurement.setMuscleMassKg(BigDecimal.valueOf(request.getMuscleMassKg()));
            }

            // Set circumferences
            if (request.hasWaistCm()) {
                measurement.setWaistCm(BigDecimal.valueOf(request.getWaistCm()));
            }
            if (request.hasHipCm()) {
                measurement.setHipCm(BigDecimal.valueOf(request.getHipCm()));
            }

            // Calculate waist-hip ratio if both are provided
            if (request.hasWaistCm() && request.hasHipCm()) {
                double waistHipRatio = request.getWaistCm() / request.getHipCm();
                measurement.setWaistHipRatio(BigDecimal.valueOf(waistHipRatio));
            }

            // Set vital signs
            if (request.hasBloodPressureSystolic()) {
                measurement.setBloodPressureSystolic(request.getBloodPressureSystolic());
            }
            if (request.hasBloodPressureDiastolic()) {
                measurement.setBloodPressureDiastolic(request.getBloodPressureDiastolic());
            }
            if (request.hasHeartRateBpm()) {
                measurement.setHeartRateBpm(request.getHeartRateBpm());
            }

            // Set lab results
            if (request.hasFastingGlucoseMgDl()) {
                measurement.setFastingGlucoseMgDl(BigDecimal.valueOf(request.getFastingGlucoseMgDl()));
            }
            if (request.hasTotalCholesterolMgDl()) {
                measurement.setTotalCholesterolMgDl(BigDecimal.valueOf(request.getTotalCholesterolMgDl()));
            }
            if (request.hasLdlCholesterolMgDl()) {
                measurement.setLdlCholesterolMgDl(BigDecimal.valueOf(request.getLdlCholesterolMgDl()));
            }
            if (request.hasHdlCholesterolMgDl()) {
                measurement.setHdlCholesterolMgDl(BigDecimal.valueOf(request.getHdlCholesterolMgDl()));
            }
            if (request.hasTriglyceridesMgDl()) {
                measurement.setTriglyceridesMgDl(BigDecimal.valueOf(request.getTriglyceridesMgDl()));
            }
            if (request.hasHemoglobinA1CPercent()) {
                measurement.setHemoglobinA1cPercent(BigDecimal.valueOf(request.getHemoglobinA1CPercent()));
            }

            // Set notes
            if (request.hasNotes()) {
                measurement.setNotes(request.getNotes());
            }

            // Save to database
            measurement = measurementRepository.save(measurement);

            log.info("Measurement created successfully with ID: {}", measurement.getId());

            // Build response
            MeasurementResponse response = MeasurementResponse.newBuilder()
                    .setMeasurement(toProto(measurement))
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (ValidationException | EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error creating measurement", e);
            throw new ValidationException("Failed to create measurement: " + e.getMessage());
        }
    }

    @Override
    public void getMeasurement(GetMeasurementRequest request, StreamObserver<MeasurementResponse> responseObserver) {
        log.info("Getting measurement with ID: {}", request.getId().getValue());

        try {
            // Validate input
            Map<String, String> errors = new HashMap<>();
            RequestValidator.validateRequired("id", request.getId(), errors);
            RequestValidator.throwIfErrors(errors);

            java.util.UUID measurementId = java.util.UUID.fromString(request.getId().getValue());

            // Find measurement
            Measurement measurement = measurementRepository.findById(measurementId)
                    .orElseThrow(() -> new EntityNotFoundException("Measurement not found: " + measurementId));

            log.info("Measurement retrieved successfully: {}", measurementId);

            // Build response
            MeasurementResponse response = MeasurementResponse.newBuilder()
                    .setMeasurement(toProto(measurement))
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (ValidationException | EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error getting measurement", e);
            throw new ValidationException("Failed to get measurement: " + e.getMessage());
        }
    }

    @Override
    public void getClientMeasurements(GetClientMeasurementsRequest request, StreamObserver<ListMeasurementsResponse> responseObserver) {
        log.info("Getting measurements for client ID: {}", request.getClientId().getValue());

        try {
            // Validate input
            Map<String, String> errors = new HashMap<>();
            RequestValidator.validateRequired("client_id", request.getClientId(), errors);
            RequestValidator.throwIfErrors(errors);

            java.util.UUID clientId = java.util.UUID.fromString(request.getClientId().getValue());

            // Validate client exists
            if (!clientRepository.existsById(clientId)) {
                throw new EntityNotFoundException("Client not found: " + clientId);
            }

            List<Measurement> measurements;

            // Check if date range is provided
            if (request.hasStartDate() && request.hasEndDate()) {
                Instant startDate = Instant.ofEpochSecond(
                        request.getStartDate().getSeconds(),
                        request.getStartDate().getNanos());
                Instant endDate = Instant.ofEpochSecond(
                        request.getEndDate().getSeconds(),
                        request.getEndDate().getNanos());

                measurements = measurementRepository.findByClientIdAndDateRange(clientId, startDate, endDate);
            } else {
                measurements = measurementRepository.findByClientIdOrderByMeasurementDateDesc(clientId);
            }

            // Convert to proto
            List<com.atlantis.nutritionist.grpc.measurement.Measurement> protoMeasurements = measurements.stream()
                    .map(this::toProto)
                    .collect(Collectors.toList());

            log.info("Retrieved {} measurements for client: {}", measurements.size(), clientId);

            // Build response with pagination metadata
            ListMeasurementsResponse response = ListMeasurementsResponse.newBuilder()
                    .addAllMeasurements(protoMeasurements)
                    .setPageResponse(PageResponse.newBuilder()
                            .setTotalPages(1)
                            .setTotalElements(measurements.size())
                            .setCurrentPage(0)
                            .setSize(measurements.size())
                            .build())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (ValidationException | EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error getting client measurements", e);
            throw new ValidationException("Failed to get client measurements: " + e.getMessage());
        }
    }

    @Override
    public void getLatestMeasurement(GetLatestMeasurementRequest request, StreamObserver<MeasurementResponse> responseObserver) {
        log.info("Getting latest measurement for client ID: {}", request.getClientId().getValue());

        try {
            // Validate input
            Map<String, String> errors = new HashMap<>();
            RequestValidator.validateRequired("client_id", request.getClientId(), errors);
            RequestValidator.throwIfErrors(errors);

            java.util.UUID clientId = java.util.UUID.fromString(request.getClientId().getValue());

            // Validate client exists
            if (!clientRepository.existsById(clientId)) {
                throw new EntityNotFoundException("Client not found: " + clientId);
            }

            // Find latest measurement
            Measurement measurement = measurementRepository.findLatestByClientId(clientId);

            if (measurement == null) {
                throw new EntityNotFoundException("No measurements found for client: " + clientId);
            }

            log.info("Latest measurement retrieved for client: {}", clientId);

            // Build response
            MeasurementResponse response = MeasurementResponse.newBuilder()
                    .setMeasurement(toProto(measurement))
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (ValidationException | EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error getting latest measurement", e);
            throw new ValidationException("Failed to get latest measurement: " + e.getMessage());
        }
    }

    @Override
    public void deleteMeasurement(DeleteMeasurementRequest request, StreamObserver<Empty> responseObserver) {
        log.info("Deleting measurement with ID: {}", request.getId().getValue());

        try {
            // Validate input
            Map<String, String> errors = new HashMap<>();
            RequestValidator.validateRequired("id", request.getId(), errors);
            RequestValidator.throwIfErrors(errors);

            java.util.UUID measurementId = java.util.UUID.fromString(request.getId().getValue());

            // Validate measurement exists
            if (!measurementRepository.existsById(measurementId)) {
                throw new EntityNotFoundException("Measurement not found: " + measurementId);
            }

            // Delete measurement
            measurementRepository.deleteById(measurementId);

            log.info("Measurement deleted successfully: {}", measurementId);

            // Return empty response
            Empty response = Empty.newBuilder().build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (ValidationException | EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error deleting measurement", e);
            throw new ValidationException("Failed to delete measurement: " + e.getMessage());
        }
    }

    /**
     * Convert Measurement entity to proto message.
     */
    private com.atlantis.nutritionist.grpc.measurement.Measurement toProto(Measurement measurement) {
        var builder = com.atlantis.nutritionist.grpc.measurement.Measurement.newBuilder()
                .setId(UUID.newBuilder().setValue(measurement.getId().toString()).build())
                .setClientId(UUID.newBuilder().setValue(measurement.getClient().getId().toString()).build())
                .setNutritionistId(UUID.newBuilder().setValue(measurement.getNutritionist().getId().toString()).build())
                .setMeasurementDate(Timestamp.newBuilder()
                        .setSeconds(measurement.getMeasurementDate().getEpochSecond())
                        .setNanos(measurement.getMeasurementDate().getNano())
                        .build())
                .setCreatedAt(Timestamp.newBuilder()
                        .setSeconds(measurement.getCreatedAt().getEpochSecond())
                        .setNanos(measurement.getCreatedAt().getNano())
                        .build())
                .setUpdatedAt(Timestamp.newBuilder()
                        .setSeconds(measurement.getUpdatedAt().getEpochSecond())
                        .setNanos(measurement.getUpdatedAt().getNano())
                        .build());

        // Add optional fields - basic measurements
        if (measurement.getWeightKg() != null) {
            builder.setWeightKg(measurement.getWeightKg().doubleValue());
        }
        if (measurement.getHeightCm() != null) {
            builder.setHeightCm(measurement.getHeightCm().doubleValue());
        }
        if (measurement.getImc() != null) {
            builder.setImc(measurement.getImc().doubleValue());
        }

        // Body composition
        if (measurement.getBodyFatPercentage() != null) {
            builder.setBodyFatPercentage(measurement.getBodyFatPercentage().doubleValue());
        }
        if (measurement.getMuscleMassKg() != null) {
            builder.setMuscleMassKg(measurement.getMuscleMassKg().doubleValue());
        }
        if (measurement.getVisceralFatPercentage() != null) {
            builder.setVisceralFatPercentage(measurement.getVisceralFatPercentage().doubleValue());
        }
        if (measurement.getWaterPercentage() != null) {
            builder.setWaterPercentage(measurement.getWaterPercentage().doubleValue());
        }

        // Circumferences
        if (measurement.getWaistCm() != null) {
            builder.setWaistCm(measurement.getWaistCm().doubleValue());
        }
        if (measurement.getHipCm() != null) {
            builder.setHipCm(measurement.getHipCm().doubleValue());
        }
        if (measurement.getWaistHipRatio() != null) {
            builder.setWaistHipRatio(measurement.getWaistHipRatio().doubleValue());
        }

        // Vital signs
        if (measurement.getBloodPressureSystolic() != null) {
            builder.setBloodPressureSystolic(measurement.getBloodPressureSystolic());
        }
        if (measurement.getBloodPressureDiastolic() != null) {
            builder.setBloodPressureDiastolic(measurement.getBloodPressureDiastolic());
        }
        if (measurement.getHeartRateBpm() != null) {
            builder.setHeartRateBpm(measurement.getHeartRateBpm());
        }

        // Lab results
        if (measurement.getFastingGlucoseMgDl() != null) {
            builder.setFastingGlucoseMgDl(measurement.getFastingGlucoseMgDl().doubleValue());
        }
        if (measurement.getTotalCholesterolMgDl() != null) {
            builder.setTotalCholesterolMgDl(measurement.getTotalCholesterolMgDl().doubleValue());
        }
        if (measurement.getLdlCholesterolMgDl() != null) {
            builder.setLdlCholesterolMgDl(measurement.getLdlCholesterolMgDl().doubleValue());
        }
        if (measurement.getHdlCholesterolMgDl() != null) {
            builder.setHdlCholesterolMgDl(measurement.getHdlCholesterolMgDl().doubleValue());
        }
        if (measurement.getTriglyceridesMgDl() != null) {
            builder.setTriglyceridesMgDl(measurement.getTriglyceridesMgDl().doubleValue());
        }
        if (measurement.getHemoglobinA1cPercent() != null) {
            builder.setHemoglobinA1CPercent(measurement.getHemoglobinA1cPercent().doubleValue());
        }

        // Notes
        if (measurement.getNotes() != null) {
            builder.setNotes(measurement.getNotes());
        }

        return builder.build();
    }
}
