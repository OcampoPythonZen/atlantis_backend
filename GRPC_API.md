# gRPC API Documentation

## Overview
The Nutritionist application uses gRPC for all API communications instead of REST. gRPC provides:
- High performance with Protocol Buffers
- Strong typing and code generation
- Bi-directional streaming support
- Better error handling

## Server Configuration

**gRPC Server:** `localhost:9090`
**HTTP/Actuator:** `localhost:8080` (health checks only)

## Available gRPC Services

### 1. UserService
**Package:** `com.atlantis.nutritionist.grpc.user`

Operations:
- `CreateUser` - Register new user
- `GetUser` - Get user by ID
- `UpdateUser` - Update user information
- `DeleteUser` - Delete user account
- `ListUsers` - List all users with pagination
- `GetUserByEmail` - Find user by email

**Status:** Sample implementation created

### 2. AuthService
**Package:** `com.atlantis.nutritionist.grpc.auth`

Operations:
- `Register` - New user registration
- `Login` - User authentication
- `RefreshToken` - Token refresh
- `Logout` - User logout
- `ChangePassword` - Password update
- `ValidateToken` - Token validation

**Status:** Proto defined, implementation pending

### 3. ClientService
**Package:** `com.atlantis.nutritionist.grpc.client`

Operations:
- `CreateClient` - Create patient profile
- `GetClient` - Get client details
- `UpdateClient` - Update client information
- `DeleteClient` - Delete client profile
- `ListClients` - List all clients
- `GetClientByUserId` - Get client by user ID

**Status:** Proto defined, implementation pending

### 4. NutriologistService
**Package:** `com.atlantis.nutritionist.grpc.nutriologist`

Operations:
- `CreateNutriologist` - Create nutritionist profile
- `GetNutriologist` - Get nutritionist details
- `UpdateNutriologist` - Update profile
- `DeleteNutriologist` - Delete profile
- `ListNutriologists` - List all nutriologists
- `GetNutriologistByUserId` - Get by user ID
- `SearchNutriologists` - Search with filters

**Status:** Proto defined, implementation pending

### 5. MeasurementService
**Package:** `com.atlantis.nutritionist.grpc.measurement`

Operations:
- `CreateMeasurement` - Record new measurement
- `GetMeasurement` - Get measurement by ID
- `GetClientMeasurements` - Get all measurements for client
- `GetLatestMeasurement` - Get most recent measurement
- `DeleteMeasurement` - Delete measurement

**Status:** Proto defined, implementation pending

### 6. NutritionPlanService
**Package:** `com.atlantis.nutritionist.grpc.nutritionplan`

Operations:
- `CreateNutritionPlan` - Create new plan
- `GetNutritionPlan` - Get plan by ID
- `UpdateNutritionPlan` - Update plan
- `DeactivatePlan` - Deactivate plan
- `GetClientPlans` - Get all plans for client
- `GetActivePlan` - Get currently active plan

**Status:** Proto defined, implementation pending

## Testing gRPC APIs

### Using grpcurl

1. **Install grpcurl:**
```bash
brew install grpcurl
```

2. **List available services:**
```bash
grpcurl -plaintext localhost:9090 list
```

3. **Describe a service:**
```bash
grpcurl -plaintext localhost:9090 describe com.atlantis.nutritionist.grpc.UserService
```

4. **Call CreateUser:**
```bash
grpcurl -plaintext -d '{
  "email": "test@example.com",
  "password": "password123",
  "first_name": "John",
  "last_name": "Doe",
  "phone": "+1-555-0100",
  "roles": ["ROLE_PATIENT"]
}' localhost:9090 com.atlantis.nutritionist.grpc.UserService/CreateUser
```

5. **Health check:**
```bash
grpcurl -plaintext localhost:9090 grpc.health.v1.Health/Check
```

### Using BloomRPC (GUI Client)

1. Download BloomRPC: https://github.com/bloomrpc/bloomrpc/releases
2. Import proto files from `src/main/proto/`
3. Set server address: `localhost:9090`
4. Test endpoints interactively

### Using Python Client

```python
import grpc
from generated import user_service_pb2, user_service_pb2_grpc

channel = grpc.insecure_channel('localhost:9090')
stub = user_service_pb2_grpc.UserServiceStub(channel)

request = user_service_pb2.CreateUserRequest(
    email="test@example.com",
    password="password123",
    first_name="John",
    last_name="Doe"
)

response = stub.CreateUser(request)
print(response)
```

## Protocol Buffer Definitions

All `.proto` files are located in: `src/main/proto/`

- `common.proto` - Common types and enums
- `user_service.proto` - User management
- `auth_service.proto` - Authentication
- `client_service.proto` - Client/patient management
- `nutriologist_service.proto` - Nutritionist management
- `measurement_service.proto` - Biometric measurements
- `nutrition_plan_service.proto` - Nutrition plans

## Generated Code

Java classes are generated in:
```
build/generated/source/proto/main/
├── grpc/     # gRPC service stubs
└── java/     # Protocol Buffer messages
```

To regenerate:
```bash
./gradlew generateProto
```

## Common Types

### UUID
```protobuf
message UUID {
  string value = 1;
}
```

### Timestamp
```protobuf
message Timestamp {
  int64 seconds = 1;
  int32 nanos = 2;
}
```

### PageRequest
```protobuf
message PageRequest {
  int32 page = 1;
  int32 size = 2;
  string sort_by = 3;
  bool ascending = 4;
}
```

### UserRole Enum
```protobuf
enum UserRole {
  ROLE_UNKNOWN = 0;
  ROLE_NUTRIOLOGIST = 1;
  ROLE_PATIENT = 2;
}
```

## Error Handling

gRPC uses status codes instead of HTTP status codes:

- `OK` (0) - Success
- `INVALID_ARGUMENT` (3) - Invalid request parameters
- `NOT_FOUND` (5) - Resource not found
- `ALREADY_EXISTS` (6) - Resource already exists
- `PERMISSION_DENIED` (7) - Insufficient permissions
- `UNAUTHENTICATED` (16) - Authentication required
- `UNIMPLEMENTED` (12) - Method not implemented yet

Example error response:
```
code: INVALID_ARGUMENT
message: "Email is required"
```

## Next Steps for Implementation

1. **Create JPA Entity Classes**
   - Map database tables to Java entities
   - Define relationships

2. **Implement Repository Layer**
   - Spring Data JPA repositories
   - Custom queries if needed

3. **Implement Service Layer**
   - Business logic
   - Transaction management
   - Validation

4. **Complete gRPC Service Implementations**
   - Map between protobuf messages and entities
   - Call service layer methods
   - Handle errors appropriately

5. **Add Authentication & Authorization**
   - JWT token validation
   - Role-based access control
   - gRPC interceptors for security

6. **Add Validation**
   - Input validation
   - Business rule validation
   - Data integrity checks

7. **Write Tests**
   - Unit tests for services
   - Integration tests for gRPC endpoints
   - End-to-end tests

## Development Workflow

1. **Define API in .proto file**
2. **Generate Java code:**
   ```bash
   ./gradlew generateProto
   ```
3. **Implement gRPC service**
4. **Test with grpcurl or BloomRPC**
5. **Write unit tests**
6. **Deploy**

## References

- [gRPC Java Documentation](https://grpc.io/docs/languages/java/)
- [Protocol Buffers Guide](https://protobuf.dev/)
- [grpc-spring-boot-starter](https://github.com/grpc-ecosystem/grpc-spring)
- [grpcurl Documentation](https://github.com/fullstorydev/grpcurl)

---

**Last Updated:** 2024-12-02
**gRPC Server Port:** 9090
**Status:** Core infrastructure complete, service implementations in progress
