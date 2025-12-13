package com.atlantis.nutritionist.grpc;

import com.atlantis.nutritionist.grpc.nutritionplan.*;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * gRPC service implementation for Nutrition Plan operations.
 * Handles nutrition plan creation, updates, and management.
 *
 * TODO: Implement actual business logic with JPA repositories and services
 */
@GrpcService
public class NutritionPlanServiceImpl extends NutritionPlanServiceGrpc.NutritionPlanServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(NutritionPlanServiceImpl.class);

    @Override
    public void createNutritionPlan(CreateNutritionPlanRequest request, StreamObserver<NutritionPlanResponse> responseObserver) {
        log.info("Creating nutrition plan '{}' for client ID: {} by nutritionist ID: {}",
                request.getPlanName(),
                request.getClientId().getValue(),
                request.getNutritionistId().getValue());

        try {
            // TODO: Implement actual nutrition plan creation logic
            // 1. Validate client and nutritionist exist
            // 2. Validate date format (ISO 8601)
            // 3. Validate macronutrient balance
            // 4. Check if client has active plan, deactivate if necessary
            // 5. Save nutrition plan to database
            // 6. Return created plan

            NutritionPlan.Builder planBuilder = NutritionPlan.newBuilder()
                    .setClientId(request.getClientId())
                    .setNutritionistId(request.getNutritionistId())
                    .setPlanName(request.getPlanName())
                    .setCaloriesTarget(request.getCaloriesTarget())
                    .setPlanType(request.getPlanType())
                    .setStartDate(request.getStartDate())
                    .setIsActive(true);

            // Add optional fields
            if (request.hasProteinGrams()) planBuilder.setProteinGrams(request.getProteinGrams());
            if (request.hasCarbsGrams()) planBuilder.setCarbsGrams(request.getCarbsGrams());
            if (request.hasFatsGrams()) planBuilder.setFatsGrams(request.getFatsGrams());
            if (request.hasFiberGrams()) planBuilder.setFiberGrams(request.getFiberGrams());
            if (request.hasFoodsToAvoid()) planBuilder.setFoodsToAvoid(request.getFoodsToAvoid());
            if (request.hasFoodsToLimit()) planBuilder.setFoodsToLimit(request.getFoodsToLimit());
            if (request.hasFoodsRecommended()) planBuilder.setFoodsRecommended(request.getFoodsRecommended());
            if (request.hasMedicalConditionTarget()) planBuilder.setMedicalConditionTarget(request.getMedicalConditionTarget());
            if (request.hasDescription()) planBuilder.setDescription(request.getDescription());
            if (request.hasEndDate()) planBuilder.setEndDate(request.getEndDate());

            NutritionPlanResponse response = NutritionPlanResponse.newBuilder()
                    .setPlan(planBuilder.build())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("Error creating nutrition plan", e);
            responseObserver.onError(
                    io.grpc.Status.INTERNAL
                            .withDescription("Error creating nutrition plan: " + e.getMessage())
                            .asRuntimeException()
            );
        }
    }

    @Override
    public void getNutritionPlan(GetNutritionPlanRequest request, StreamObserver<NutritionPlanResponse> responseObserver) {
        log.info("Getting nutrition plan with ID: {}", request.getId().getValue());

        try {
            // TODO: Implement actual nutrition plan retrieval logic
            // 1. Query database for plan by ID
            // 2. Return plan data

            responseObserver.onError(
                    io.grpc.Status.UNIMPLEMENTED
                            .withDescription("Method not yet implemented")
                            .asRuntimeException()
            );
        } catch (Exception e) {
            log.error("Error getting nutrition plan", e);
            responseObserver.onError(
                    io.grpc.Status.INTERNAL
                            .withDescription("Error getting nutrition plan: " + e.getMessage())
                            .asRuntimeException()
            );
        }
    }

    @Override
    public void updateNutritionPlan(UpdateNutritionPlanRequest request, StreamObserver<NutritionPlanResponse> responseObserver) {
        log.info("Updating nutrition plan with ID: {}", request.getId().getValue());

        try {
            // TODO: Implement actual nutrition plan update logic
            // 1. Validate plan exists
            // 2. Update only provided fields (all fields are optional)
            // 3. Validate macronutrient balance if updated
            // 4. Save updated plan
            // 5. Return updated plan

            responseObserver.onError(
                    io.grpc.Status.UNIMPLEMENTED
                            .withDescription("Method not yet implemented")
                            .asRuntimeException()
            );
        } catch (Exception e) {
            log.error("Error updating nutrition plan", e);
            responseObserver.onError(
                    io.grpc.Status.INTERNAL
                            .withDescription("Error updating nutrition plan: " + e.getMessage())
                            .asRuntimeException()
            );
        }
    }

    @Override
    public void deactivatePlan(DeactivatePlanRequest request, StreamObserver<NutritionPlanResponse> responseObserver) {
        log.info("Deactivating nutrition plan with ID: {}", request.getId().getValue());

        try {
            // TODO: Implement actual plan deactivation logic
            // 1. Validate plan exists
            // 2. Set is_active to false
            // 3. Save updated plan
            // 4. Return deactivated plan

            responseObserver.onError(
                    io.grpc.Status.UNIMPLEMENTED
                            .withDescription("Method not yet implemented")
                            .asRuntimeException()
            );
        } catch (Exception e) {
            log.error("Error deactivating nutrition plan", e);
            responseObserver.onError(
                    io.grpc.Status.INTERNAL
                            .withDescription("Error deactivating nutrition plan: " + e.getMessage())
                            .asRuntimeException()
            );
        }
    }

    @Override
    public void getClientPlans(GetClientPlansRequest request, StreamObserver<ListNutritionPlansResponse> responseObserver) {
        log.info("Getting nutrition plans for client ID: {}", request.getClientId().getValue());

        try {
            // TODO: Implement actual client plans retrieval logic
            // 1. Apply pagination from page_request
            // 2. Filter by active_only if provided
            // 3. Query database for client's plans
            // 4. Order by created_at descending (most recent first)
            // 5. Return list with page response metadata

            ListNutritionPlansResponse response = ListNutritionPlansResponse.newBuilder()
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("Error getting client plans", e);
            responseObserver.onError(
                    io.grpc.Status.INTERNAL
                            .withDescription("Error getting client plans: " + e.getMessage())
                            .asRuntimeException()
            );
        }
    }

    @Override
    public void getActivePlan(GetActivePlanRequest request, StreamObserver<NutritionPlanResponse> responseObserver) {
        log.info("Getting active nutrition plan for client ID: {}", request.getClientId().getValue());

        try {
            // TODO: Implement actual active plan retrieval logic
            // 1. Query database for active plan for client (is_active = true)
            // 2. Should only be one active plan per client
            // 3. Return active plan or NOT_FOUND if no active plan

            responseObserver.onError(
                    io.grpc.Status.UNIMPLEMENTED
                            .withDescription("Method not yet implemented")
                            .asRuntimeException()
            );
        } catch (Exception e) {
            log.error("Error getting active plan", e);
            responseObserver.onError(
                    io.grpc.Status.INTERNAL
                            .withDescription("Error getting active plan: " + e.getMessage())
                            .asRuntimeException()
            );
        }
    }
}
