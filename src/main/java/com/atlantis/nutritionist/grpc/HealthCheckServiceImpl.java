package com.atlantis.nutritionist.grpc;

import com.atlantis.nutritionist.grpc.health.HealthCheckRequest;
import com.atlantis.nutritionist.grpc.health.HealthCheckResponse;
import com.atlantis.nutritionist.grpc.health.HealthCheckServiceGrpc;
import com.atlantis.nutritionist.grpc.health.SystemStatusResponse;
import com.atlantis.nutritionist.grpc.common.Empty;
import com.atlantis.nutritionist.security.PublicEndpoint;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;

import javax.sql.DataSource;
import java.lang.management.ManagementFactory;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@GrpcService
public class HealthCheckServiceImpl extends HealthCheckServiceGrpc.HealthCheckServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(HealthCheckServiceImpl.class);

    @Autowired
    private DataSource dataSource;

    @Autowired(required = false)
    private BuildProperties buildProperties;

    @Value("${spring.application.name:nutritionist-app}")
    private String applicationName;

    private final long startTime = System.currentTimeMillis();

    @PublicEndpoint("Health checks are public")
    @Override
    public void check(HealthCheckRequest request, StreamObserver<HealthCheckResponse> responseObserver) {
        log.info("Health check request received for service: {}",
                request.hasService() ? request.getService() : "all");

        HealthCheckResponse.Status status = HealthCheckResponse.Status.SERVING;
        String message = "Service is healthy";

        try {
            // Check database connectivity
            if (!isDatabaseConnected()) {
                status = HealthCheckResponse.Status.NOT_SERVING;
                message = "Database connection failed";
            }
        } catch (Exception e) {
            log.error("Health check failed", e);
            status = HealthCheckResponse.Status.NOT_SERVING;
            message = "Health check failed: " + e.getMessage();
        }

        HealthCheckResponse response = HealthCheckResponse.newBuilder()
                .setStatus(status)
                .setMessage(message)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @PublicEndpoint("System status is public")
    @Override
    public void getSystemStatus(Empty request, StreamObserver<SystemStatusResponse> responseObserver) {
        log.info("System status request received");

        try {
            boolean databaseConnected = isDatabaseConnected();
            boolean grpcServerRunning = true; // If we're here, gRPC is running
            String version = buildProperties != null ? buildProperties.getVersion() : "0.0.1-SNAPSHOT";
            long uptimeSeconds = (System.currentTimeMillis() - startTime) / 1000;

            Map<String, String> systemInfo = new HashMap<>();
            systemInfo.put("application", applicationName);
            systemInfo.put("java_version", System.getProperty("java.version"));
            systemInfo.put("os_name", System.getProperty("os.name"));
            systemInfo.put("os_version", System.getProperty("os.version"));
            systemInfo.put("total_memory_mb", String.valueOf(Runtime.getRuntime().totalMemory() / (1024 * 1024)));
            systemInfo.put("free_memory_mb", String.valueOf(Runtime.getRuntime().freeMemory() / (1024 * 1024)));
            systemInfo.put("available_processors", String.valueOf(Runtime.getRuntime().availableProcessors()));
            systemInfo.put("jvm_uptime_seconds", String.valueOf(ManagementFactory.getRuntimeMXBean().getUptime() / 1000));

            SystemStatusResponse response = SystemStatusResponse.newBuilder()
                    .setDatabaseConnected(databaseConnected)
                    .setGrpcServerRunning(grpcServerRunning)
                    .setApplicationVersion(version)
                    .setUptimeSeconds(uptimeSeconds)
                    .putAllSystemInfo(systemInfo)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("Failed to get system status", e);
            responseObserver.onError(io.grpc.Status.INTERNAL
                    .withDescription("Failed to get system status: " + e.getMessage())
                    .asRuntimeException());
        }
    }

    private boolean isDatabaseConnected() {
        try (Connection connection = dataSource.getConnection()) {
            return connection.isValid(5);
        } catch (Exception e) {
            log.error("Database connection check failed", e);
            return false;
        }
    }
}
