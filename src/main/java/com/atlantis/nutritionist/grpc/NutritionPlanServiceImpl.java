package com.atlantis.nutritionist.grpc;

import com.atlantis.nutritionist.entity.Client;
import com.atlantis.nutritionist.entity.NutritionPlan;
import com.atlantis.nutritionist.entity.Nutriologist;
import com.atlantis.nutritionist.exception.EntityNotFoundException;
import com.atlantis.nutritionist.exception.ValidationException;
import com.atlantis.nutritionist.grpc.common.PageResponse;
import com.atlantis.nutritionist.grpc.common.PlanType;
import com.atlantis.nutritionist.grpc.common.Timestamp;
import com.atlantis.nutritionist.grpc.common.UUID;
import com.atlantis.nutritionist.grpc.nutritionplan.*;
import com.atlantis.nutritionist.repository.ClientRepository;
import com.atlantis.nutritionist.repository.NutritionPlanRepository;
import com.atlantis.nutritionist.repository.NutriologistRepository;
import com.atlantis.nutritionist.validation.RequestValidator;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * gRPC service implementation for Nutrition Plan operations.
 * Handles nutrition plan creation, updates, and management with full database integration.
 */
@GrpcService
public class NutritionPlanServiceImpl extends NutritionPlanServiceGrpc.NutritionPlanServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(NutritionPlanServiceImpl.class);

    private final NutritionPlanRepository nutritionPlanRepository;
    private final ClientRepository clientRepository;
    private final NutriologistRepository nutriologistRepository;

    public NutritionPlanServiceImpl(NutritionPlanRepository nutritionPlanRepository,
                                     ClientRepository clientRepository,
                                     NutriologistRepository nutriologistRepository) {
        this.nutritionPlanRepository = nutritionPlanRepository;
        this.clientRepository = clientRepository;
        this.nutriologistRepository = nutriologistRepository;
    }

    @Override
    public void createNutritionPlan(CreateNutritionPlanRequest request, StreamObserver<NutritionPlanResponse> responseObserver) {
        log.info("Creating nutrition plan '{}' for client ID: {} by nutritionist ID: {}",
                request.getPlanName(),
                request.getClientId().getValue(),
                request.getNutritionistId().getValue());

        try {
            // Validate input
            Map<String, String> errors = new HashMap<>();
            RequestValidator.validateRequired("client_id", request.getClientId(), errors);
            RequestValidator.validateRequired("nutritionist_id", request.getNutritionistId(), errors);
            RequestValidator.validateRequired("plan_name", request.getPlanName(), errors);
            RequestValidator.validateRequired("start_date", request.getStartDate(), errors);
            RequestValidator.throwIfErrors(errors);

            java.util.UUID clientId = java.util.UUID.fromString(request.getClientId().getValue());
            java.util.UUID nutritionistId = java.util.UUID.fromString(request.getNutritionistId().getValue());

            // Validate client exists
            Client client = clientRepository.findById(clientId)
                    .orElseThrow(() -> new EntityNotFoundException("Client not found: " + clientId));

            // Validate nutritionist exists
            Nutriologist nutritionist = nutriologistRepository.findById(nutritionistId)
                    .orElseThrow(() -> new EntityNotFoundException("Nutritionist not found: " + nutritionistId));

            // Deactivate any existing active plan for the client
            List<NutritionPlan> activePlans = nutritionPlanRepository.findByClientIdAndIsActive(clientId, true);
            for (NutritionPlan activePlan : activePlans) {
                activePlan.setIsActive(false);
                nutritionPlanRepository.save(activePlan);
                log.info("Deactivated previous plan: {}", activePlan.getId());
            }

            // Create nutrition plan entity
            NutritionPlan plan = new NutritionPlan();
            plan.setClient(client);
            plan.setNutritionist(nutritionist);
            plan.setPlanName(request.getPlanName());
            plan.setCaloriesTarget(request.getCaloriesTarget());
            plan.setPlanType(convertPlanTypeToString(request.getPlanType()));
            plan.setStartDate(LocalDate.parse(request.getStartDate()));
            plan.setIsActive(true);

            // Set optional macronutrients
            if (request.hasProteinGrams()) {
                plan.setProteinGrams(BigDecimal.valueOf(request.getProteinGrams()));
            }
            if (request.hasCarbsGrams()) {
                plan.setCarbsGrams(BigDecimal.valueOf(request.getCarbsGrams()));
            }
            if (request.hasFatsGrams()) {
                plan.setFatsGrams(BigDecimal.valueOf(request.getFatsGrams()));
            }
            if (request.hasFiberGrams()) {
                plan.setFiberGrams(BigDecimal.valueOf(request.getFiberGrams()));
            }

            // Set dietary guidance
            if (request.hasFoodsToAvoid()) {
                plan.setFoodsToAvoid(request.getFoodsToAvoid());
            }
            if (request.hasFoodsToLimit()) {
                plan.setFoodsToLimit(request.getFoodsToLimit());
            }
            if (request.hasFoodsRecommended()) {
                plan.setFoodsRecommended(request.getFoodsRecommended());
            }

            // Set clinical info
            if (request.hasMedicalConditionTarget()) {
                plan.setMedicalConditionTarget(request.getMedicalConditionTarget());
            }
            if (request.hasDescription()) {
                plan.setDescription(request.getDescription());
            }

            // Set end date if provided
            if (request.hasEndDate()) {
                plan.setEndDate(LocalDate.parse(request.getEndDate()));
            }

            // Save to database
            plan = nutritionPlanRepository.save(plan);

            log.info("Nutrition plan created successfully with ID: {}", plan.getId());

            // Build response
            NutritionPlanResponse response = NutritionPlanResponse.newBuilder()
                    .setPlan(toProto(plan))
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (ValidationException | EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error creating nutrition plan", e);
            throw new ValidationException("Failed to create nutrition plan: " + e.getMessage());
        }
    }

    @Override
    public void getNutritionPlan(GetNutritionPlanRequest request, StreamObserver<NutritionPlanResponse> responseObserver) {
        log.info("Getting nutrition plan with ID: {}", request.getId().getValue());

        try {
            // Validate input
            Map<String, String> errors = new HashMap<>();
            RequestValidator.validateRequired("id", request.getId(), errors);
            RequestValidator.throwIfErrors(errors);

            java.util.UUID planId = java.util.UUID.fromString(request.getId().getValue());

            // Find plan
            NutritionPlan plan = nutritionPlanRepository.findById(planId)
                    .orElseThrow(() -> new EntityNotFoundException("Nutrition plan not found: " + planId));

            log.info("Nutrition plan retrieved successfully: {}", planId);

            // Build response
            NutritionPlanResponse response = NutritionPlanResponse.newBuilder()
                    .setPlan(toProto(plan))
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (ValidationException | EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error getting nutrition plan", e);
            throw new ValidationException("Failed to get nutrition plan: " + e.getMessage());
        }
    }

    @Override
    public void updateNutritionPlan(UpdateNutritionPlanRequest request, StreamObserver<NutritionPlanResponse> responseObserver) {
        log.info("Updating nutrition plan with ID: {}", request.getId().getValue());

        try {
            // Validate input
            Map<String, String> errors = new HashMap<>();
            RequestValidator.validateRequired("id", request.getId(), errors);
            RequestValidator.throwIfErrors(errors);

            java.util.UUID planId = java.util.UUID.fromString(request.getId().getValue());

            // Find existing plan
            NutritionPlan plan = nutritionPlanRepository.findById(planId)
                    .orElseThrow(() -> new EntityNotFoundException("Nutrition plan not found: " + planId));

            // Update fields if provided
            if (request.hasPlanName()) {
                plan.setPlanName(request.getPlanName());
            }
            if (request.hasCaloriesTarget()) {
                plan.setCaloriesTarget(request.getCaloriesTarget());
            }
            if (request.hasPlanType()) {
                plan.setPlanType(convertPlanTypeToString(request.getPlanType()));
            }

            // Update macronutrients
            if (request.hasProteinGrams()) {
                plan.setProteinGrams(BigDecimal.valueOf(request.getProteinGrams()));
            }
            if (request.hasCarbsGrams()) {
                plan.setCarbsGrams(BigDecimal.valueOf(request.getCarbsGrams()));
            }
            if (request.hasFatsGrams()) {
                plan.setFatsGrams(BigDecimal.valueOf(request.getFatsGrams()));
            }
            if (request.hasFiberGrams()) {
                plan.setFiberGrams(BigDecimal.valueOf(request.getFiberGrams()));
            }

            // Update dietary guidance
            if (request.hasFoodsToAvoid()) {
                plan.setFoodsToAvoid(request.getFoodsToAvoid());
            }
            if (request.hasFoodsToLimit()) {
                plan.setFoodsToLimit(request.getFoodsToLimit());
            }
            if (request.hasFoodsRecommended()) {
                plan.setFoodsRecommended(request.getFoodsRecommended());
            }

            // Update clinical info
            if (request.hasMedicalConditionTarget()) {
                plan.setMedicalConditionTarget(request.getMedicalConditionTarget());
            }
            if (request.hasDescription()) {
                plan.setDescription(request.getDescription());
            }

            // Update schedule
            if (request.hasEndDate()) {
                plan.setEndDate(LocalDate.parse(request.getEndDate()));
            }
            if (request.hasIsActive()) {
                plan.setIsActive(request.getIsActive());
            }
            if (request.hasNotes()) {
                plan.setNotes(request.getNotes());
            }

            // Save updated plan
            plan = nutritionPlanRepository.save(plan);

            log.info("Nutrition plan updated successfully: {}", planId);

            // Build response
            NutritionPlanResponse response = NutritionPlanResponse.newBuilder()
                    .setPlan(toProto(plan))
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (ValidationException | EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error updating nutrition plan", e);
            throw new ValidationException("Failed to update nutrition plan: " + e.getMessage());
        }
    }

    @Override
    public void deactivatePlan(DeactivatePlanRequest request, StreamObserver<NutritionPlanResponse> responseObserver) {
        log.info("Deactivating nutrition plan with ID: {}", request.getId().getValue());

        try {
            // Validate input
            Map<String, String> errors = new HashMap<>();
            RequestValidator.validateRequired("id", request.getId(), errors);
            RequestValidator.throwIfErrors(errors);

            java.util.UUID planId = java.util.UUID.fromString(request.getId().getValue());

            // Find plan
            NutritionPlan plan = nutritionPlanRepository.findById(planId)
                    .orElseThrow(() -> new EntityNotFoundException("Nutrition plan not found: " + planId));

            // Deactivate plan
            plan.setIsActive(false);
            plan = nutritionPlanRepository.save(plan);

            log.info("Nutrition plan deactivated successfully: {}", planId);

            // Build response
            NutritionPlanResponse response = NutritionPlanResponse.newBuilder()
                    .setPlan(toProto(plan))
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (ValidationException | EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error deactivating nutrition plan", e);
            throw new ValidationException("Failed to deactivate nutrition plan: " + e.getMessage());
        }
    }

    @Override
    public void getClientPlans(GetClientPlansRequest request, StreamObserver<ListNutritionPlansResponse> responseObserver) {
        log.info("Getting nutrition plans for client ID: {}", request.getClientId().getValue());

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

            List<NutritionPlan> plans;

            // Check if active_only filter is applied
            if (request.hasActiveOnly() && request.getActiveOnly()) {
                plans = nutritionPlanRepository.findByClientIdAndIsActive(clientId, true);
            } else {
                plans = nutritionPlanRepository.findByClientIdOrderByCreatedAtDesc(clientId);
            }

            // Convert to proto
            List<com.atlantis.nutritionist.grpc.nutritionplan.NutritionPlan> protoPlans = plans.stream()
                    .map(this::toProto)
                    .collect(Collectors.toList());

            log.info("Retrieved {} nutrition plans for client: {}", plans.size(), clientId);

            // Build response with pagination metadata
            ListNutritionPlansResponse response = ListNutritionPlansResponse.newBuilder()
                    .addAllPlans(protoPlans)
                    .setPageResponse(PageResponse.newBuilder()
                            .setTotalPages(1)
                            .setTotalElements(plans.size())
                            .setCurrentPage(0)
                            .setSize(plans.size())
                            .build())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (ValidationException | EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error getting client plans", e);
            throw new ValidationException("Failed to get client plans: " + e.getMessage());
        }
    }

    @Override
    public void getActivePlan(GetActivePlanRequest request, StreamObserver<NutritionPlanResponse> responseObserver) {
        log.info("Getting active nutrition plan for client ID: {}", request.getClientId().getValue());

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

            // Find active plan
            NutritionPlan plan = nutritionPlanRepository.findActiveByClientId(clientId)
                    .orElseThrow(() -> new EntityNotFoundException("No active nutrition plan found for client: " + clientId));

            log.info("Active nutrition plan retrieved for client: {}", clientId);

            // Build response
            NutritionPlanResponse response = NutritionPlanResponse.newBuilder()
                    .setPlan(toProto(plan))
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (ValidationException | EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error getting active plan", e);
            throw new ValidationException("Failed to get active plan: " + e.getMessage());
        }
    }

    /**
     * Convert NutritionPlan entity to proto message.
     */
    private com.atlantis.nutritionist.grpc.nutritionplan.NutritionPlan toProto(NutritionPlan plan) {
        var builder = com.atlantis.nutritionist.grpc.nutritionplan.NutritionPlan.newBuilder()
                .setId(UUID.newBuilder().setValue(plan.getId().toString()).build())
                .setClientId(UUID.newBuilder().setValue(plan.getClient().getId().toString()).build())
                .setNutritionistId(UUID.newBuilder().setValue(plan.getNutritionist().getId().toString()).build())
                .setPlanName(plan.getPlanName())
                .setCaloriesTarget(plan.getCaloriesTarget() != null ? plan.getCaloriesTarget() : 0)
                .setPlanType(convertStringToPlanType(plan.getPlanType()))
                .setStartDate(plan.getStartDate().toString())
                .setIsActive(plan.getIsActive() != null ? plan.getIsActive() : false)
                .setCreatedAt(Timestamp.newBuilder()
                        .setSeconds(plan.getCreatedAt().getEpochSecond())
                        .setNanos(plan.getCreatedAt().getNano())
                        .build())
                .setUpdatedAt(Timestamp.newBuilder()
                        .setSeconds(plan.getUpdatedAt().getEpochSecond())
                        .setNanos(plan.getUpdatedAt().getNano())
                        .build());

        // Add optional macronutrients
        if (plan.getProteinGrams() != null) {
            builder.setProteinGrams(plan.getProteinGrams().doubleValue());
        }
        if (plan.getCarbsGrams() != null) {
            builder.setCarbsGrams(plan.getCarbsGrams().doubleValue());
        }
        if (plan.getFatsGrams() != null) {
            builder.setFatsGrams(plan.getFatsGrams().doubleValue());
        }
        if (plan.getFiberGrams() != null) {
            builder.setFiberGrams(plan.getFiberGrams().doubleValue());
        }

        // Add dietary guidance
        if (plan.getFoodsToAvoid() != null) {
            builder.setFoodsToAvoid(plan.getFoodsToAvoid());
        }
        if (plan.getFoodsToLimit() != null) {
            builder.setFoodsToLimit(plan.getFoodsToLimit());
        }
        if (plan.getFoodsRecommended() != null) {
            builder.setFoodsRecommended(plan.getFoodsRecommended());
        }

        // Add clinical info
        if (plan.getMedicalConditionTarget() != null) {
            builder.setMedicalConditionTarget(plan.getMedicalConditionTarget());
        }
        if (plan.getDescription() != null) {
            builder.setDescription(plan.getDescription());
        }

        // Add end date
        if (plan.getEndDate() != null) {
            builder.setEndDate(plan.getEndDate().toString());
        }

        // Add notes
        if (plan.getNotes() != null) {
            builder.setNotes(plan.getNotes());
        }

        return builder.build();
    }

    /**
     * Convert PlanType enum to string for database.
     */
    private String convertPlanTypeToString(PlanType planType) {
        return switch (planType) {
            case BALANCED -> "BALANCED";
            case LOW_CARB -> "LOW_CARB";
            case LOW_FAT -> "LOW_FAT";
            case DIABETIC -> "DIABETIC";
            case RENAL -> "RENAL";
            case CARDIAC -> "CARDIAC";
            case KETOGENIC -> "KETOGENIC";
            default -> "UNSPECIFIED";
        };
    }

    /**
     * Convert string to PlanType enum for proto.
     */
    private PlanType convertStringToPlanType(String planType) {
        if (planType == null) {
            return PlanType.PLAN_UNSPECIFIED;
        }
        return switch (planType.toUpperCase()) {
            case "BALANCED" -> PlanType.BALANCED;
            case "LOW_CARB" -> PlanType.LOW_CARB;
            case "LOW_FAT" -> PlanType.LOW_FAT;
            case "DIABETIC" -> PlanType.DIABETIC;
            case "RENAL" -> PlanType.RENAL;
            case "CARDIAC" -> PlanType.CARDIAC;
            case "KETOGENIC" -> PlanType.KETOGENIC;
            default -> PlanType.PLAN_UNSPECIFIED;
        };
    }
}
