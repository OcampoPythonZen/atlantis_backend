package com.atlantis.nutritionist.grpc;

import com.atlantis.nutritionist.grpc.common.Empty;
import com.atlantis.nutritionist.grpc.health.*;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

/**
 * gRPC service implementation for health checks.
 * Provides endpoints for monitoring system health and status.
 * All endpoints are public (no authentication required).
 */
@GrpcService
public class HealthCheckServiceImpl extends HealthCheckServiceGrpc.HealthCheckServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(HealthCheckServiceImpl.class);

    private final DataSource dataSource;
    private final long startTime;

    @Value("${spring.application.name:nutritionist}")
    private String applicationName;

    public HealthCheckServiceImpl(DataSource dataSource) {
        this.dataSource = dataSource;
        this.startTime = System.currentTimeMillis();
    }

    /**
     * Check the health status of the service.
     */
    @Override
    public void check(HealthCheckRequest request, StreamObserver<HealthCheckResponse> responseObserver) {
        log.debug("Health check request received");

        try {
            boolean isDatabaseHealthy = checkDatabaseConnection();

            HealthCheckResponse.Status status = isDatabaseHealthy
                    ? HealthCheckResponse.Status.SERVING
                    : HealthCheckResponse.Status.NOT_SERVING;

            String message = isDatabaseHealthy
                    ? "Service is healthy"
                    : "Database connection failed";

            HealthCheckResponse response = HealthCheckResponse.newBuilder()
                    .setStatus(status)
                    .setMessage(message)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            log.error("Health check failed", e);

            HealthCheckResponse response = HealthCheckResponse.newBuilder()
                    .setStatus(HealthCheckResponse.Status.NOT_SERVING)
                    .setMessage("Health check failed: " + e.getMessage())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }

    /**
     * Get detailed system status information.
     */
    @Override
    public void getSystemStatus(Empty request, StreamObserver<SystemStatusResponse> responseObserver) {
        log.debug("System status request received");

        try {
            boolean databaseConnected = checkDatabaseConnection();
            long uptimeSeconds = (System.currentTimeMillis() - startTime) / 1000;

            Map<String, String> systemInfo = new HashMap<>();
            systemInfo.put("java.version", System.getProperty("java.version"));
            systemInfo.put("os.name", System.getProperty("os.name"));
            systemInfo.put("available.processors", String.valueOf(Runtime.getRuntime().availableProcessors()));
            systemInfo.put("max.memory.mb", String.valueOf(Runtime.getRuntime().maxMemory() / (1024 * 1024)));
            systemInfo.put("free.memory.mb", String.valueOf(Runtime.getRuntime().freeMemory() / (1024 * 1024)));

            SystemStatusResponse response = SystemStatusResponse.newBuilder()
                    .setDatabaseConnected(databaseConnected)
                    .setGrpcServerRunning(true)
                    .setApplicationVersion("1.0.0")
                    .setUptimeSeconds(uptimeSeconds)
                    .putAllSystemInfo(systemInfo)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            log.error("System status check failed", e);

            SystemStatusResponse response = SystemStatusResponse.newBuilder()
                    .setDatabaseConnected(false)
                    .setGrpcServerRunning(true)
                    .setApplicationVersion("1.0.0")
                    .setUptimeSeconds(0)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }

    /**
     * Check if database connection is healthy.
     */
    private boolean checkDatabaseConnection() {
        try (Connection connection = dataSource.getConnection()) {
            return connection.isValid(5);
        } catch (Exception e) {
            log.warn("Database health check failed: {}", e.getMessage());
            return false;
        }
    }
}
