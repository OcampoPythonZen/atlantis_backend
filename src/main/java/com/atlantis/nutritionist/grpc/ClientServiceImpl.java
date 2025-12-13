package com.atlantis.nutritionist.grpc;

import com.atlantis.nutritionist.grpc.client.*;
import com.atlantis.nutritionist.grpc.common.Empty;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * gRPC service implementation for Client (Patient) operations.
 * Handles all patient/client profile management endpoints.
 *
 * TODO: Implement actual business logic with JPA repositories and services
 */
@GrpcService
public class ClientServiceImpl extends ClientServiceGrpc.ClientServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(ClientServiceImpl.class);

    @Override
    public void createClient(CreateClientRequest request, StreamObserver<ClientResponse> responseObserver) {
        log.info("Creating client for user ID: {}", request.getUserId().getValue());

        try {
            // TODO: Implement actual client creation logic
            // 1. Validate user exists and has PATIENT role
            // 2. Check if client profile already exists for user
            // 3. Validate date_of_birth format (ISO 8601)
            // 4. Save client profile to database
            // 5. Return created client with user info

            // Mock response for now
            ClientResponse response = ClientResponse.newBuilder()
                    .setClient(Client.newBuilder()
                            .setUserId(request.getUserId())
                            .setDateOfBirth(request.getDateOfBirth())
                            .setGender(request.getGender())
                            .setBloodType(request.hasBloodType() ? request.getBloodType() : "")
                            .setRhFactor(request.hasRhFactor() ? request.getRhFactor() : "")
                            .setNationality(request.hasNationality() ? request.getNationality() : "")
                            .setEmergencyContactName(request.hasEmergencyContactName() ? request.getEmergencyContactName() : "")
                            .setEmergencyContactPhone(request.hasEmergencyContactPhone() ? request.getEmergencyContactPhone() : "")
                            .setEmergencyContactRelationship(request.hasEmergencyContactRelationship() ? request.getEmergencyContactRelationship() : "")
                            .build())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("Error creating client", e);
            responseObserver.onError(
                    io.grpc.Status.INTERNAL
                            .withDescription("Error creating client: " + e.getMessage())
                            .asRuntimeException()
            );
        }
    }

    @Override
    public void getClient(GetClientRequest request, StreamObserver<ClientResponse> responseObserver) {
        log.info("Getting client with ID: {}", request.getId().getValue());

        try {
            // TODO: Implement actual client retrieval logic
            // 1. Query database for client by ID
            // 2. Join with user table to get user info
            // 3. Return client with nested user info

            responseObserver.onError(
                    io.grpc.Status.UNIMPLEMENTED
                            .withDescription("Method not yet implemented")
                            .asRuntimeException()
            );
        } catch (Exception e) {
            log.error("Error getting client", e);
            responseObserver.onError(
                    io.grpc.Status.INTERNAL
                            .withDescription("Error getting client: " + e.getMessage())
                            .asRuntimeException()
            );
        }
    }

    @Override
    public void updateClient(UpdateClientRequest request, StreamObserver<ClientResponse> responseObserver) {
        log.info("Updating client with ID: {}", request.getId().getValue());

        try {
            // TODO: Implement actual client update logic
            // 1. Validate client exists
            // 2. Validate date_of_birth format if provided
            // 3. Update only provided fields (optional fields)
            // 4. Save updated client
            // 5. Return updated client with user info

            responseObserver.onError(
                    io.grpc.Status.UNIMPLEMENTED
                            .withDescription("Method not yet implemented")
                            .asRuntimeException()
            );
        } catch (Exception e) {
            log.error("Error updating client", e);
            responseObserver.onError(
                    io.grpc.Status.INTERNAL
                            .withDescription("Error updating client: " + e.getMessage())
                            .asRuntimeException()
            );
        }
    }

    @Override
    public void deleteClient(DeleteClientRequest request, StreamObserver<Empty> responseObserver) {
        log.info("Deleting client with ID: {}", request.getId().getValue());

        try {
            // TODO: Implement actual client deletion logic
            // 1. Validate client exists
            // 2. Check for dependencies (measurements, goals, plans, etc.)
            // 3. Handle cascade deletion or prevent deletion if dependencies exist
            // 4. Soft delete or hard delete based on business rules
            // 5. Return empty response

            responseObserver.onError(
                    io.grpc.Status.UNIMPLEMENTED
                            .withDescription("Method not yet implemented")
                            .asRuntimeException()
            );
        } catch (Exception e) {
            log.error("Error deleting client", e);
            responseObserver.onError(
                    io.grpc.Status.INTERNAL
                            .withDescription("Error deleting client: " + e.getMessage())
                            .asRuntimeException()
            );
        }
    }

    @Override
    public void listClients(ListClientsRequest request, StreamObserver<ListClientsResponse> responseObserver) {
        log.info("Listing clients with pagination - page: {}, size: {}",
                request.hasPageRequest() ? request.getPageRequest().getPage() : 0,
                request.hasPageRequest() ? request.getPageRequest().getSize() : 10);

        try {
            // TODO: Implement actual client listing logic
            // 1. Apply pagination from page_request
            // 2. Query database with pagination
            // 3. Join with user table to get user info for each client
            // 4. Return list with page response metadata

            ListClientsResponse response = ListClientsResponse.newBuilder()
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("Error listing clients", e);
            responseObserver.onError(
                    io.grpc.Status.INTERNAL
                            .withDescription("Error listing clients: " + e.getMessage())
                            .asRuntimeException()
            );
        }
    }

    @Override
    public void getClientByUserId(GetClientByUserIdRequest request, StreamObserver<ClientResponse> responseObserver) {
        log.info("Getting client by user ID: {}", request.getUserId().getValue());

        try {
            // TODO: Implement actual client retrieval by user ID
            // 1. Query database for client by user_id
            // 2. Join with user table to get user info
            // 3. Return client with nested user info

            responseObserver.onError(
                    io.grpc.Status.UNIMPLEMENTED
                            .withDescription("Method not yet implemented")
                            .asRuntimeException()
            );
        } catch (Exception e) {
            log.error("Error getting client by user ID", e);
            responseObserver.onError(
                    io.grpc.Status.INTERNAL
                            .withDescription("Error getting client by user ID: " + e.getMessage())
                            .asRuntimeException()
            );
        }
    }
}
