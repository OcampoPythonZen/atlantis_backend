# Development Guide - Nutritionist Application

## Project Overview
Professional nutritional management application with PostgreSQL database and gRPC APIs.

**Version:** 0.0.1-SNAPSHOT
**Java:** 25
**Spring Boot:** 4.0.0
**Database:** PostgreSQL 16

---

## Development Progress Tracker

### ✅ Phase 1: Project Setup & Infrastructure
- [x] Spring Boot 4.0.0 project initialization
- [x] Gradle build configuration
- [x] PostgreSQL 16 Docker setup
- [x] Docker Compose configuration
- [x] Environment configuration (.env.example)
- [x] Basic project structure

### ✅ Phase 2: Database Schema Design
- [x] Complete database schema (17 tables)
- [x] Flyway migration setup
- [x] Initial schema migration (V1__initial_schema.sql)
- [x] Seed data migration (V2__seed_data.sql)
- [x] Database indexes and constraints
- [x] Foreign key relationships

#### Database Tables (14 Active Tables)

**Authentication & Users:**
- `users` - Base user accounts
- `roles` - User roles (NUTRIOLOGIST, PATIENT)
- `user_roles` - User-role associations

**Professional Profiles:**
- `nutriologists` - Nutritionist profiles with licenses and specializations
- `clients` - Patient/client profiles with demographics

**Medical Information:**
- `medical_history` - Comprehensive medical history and conditions
- `measurements` - Biometric measurements and lab results (50+ fields)
- `symptom_logs` - Symptom tracking over time

**Dietary Tracking:**
- `food_diary_entries` - Daily food intake with macros/micros

**Treatment Management:**
- `client_goals` - Patient goals (weight loss, muscle gain, health improvement)
- `nutrition_plans` - Personalized nutrition plans
- `supplement_tracking` - Supplement prescriptions and tracking
- `clinical_notes` - Clinical consultation notes

**Audit:**
- `audit_logs` - System audit trail

#### Seed Data Summary
- **Users:** 5 (2 nutriologists, 3 patients)
- **Nutriologists:** 2 with complete professional profiles
- **Clients:** 3 with diverse medical scenarios:
  - Pre-diabetic patient (weight management)
  - Athletic patient (muscle gain)
  - Type 2 diabetic patient (disease management)
- **Medical Records:** Complete histories, measurements, goals, plans
- **Clinical Data:** Consultation notes, supplement prescriptions

### ✅ Phase 3: Core Dependencies
- [x] Spring Data JPA
- [x] Spring Security
- [x] PostgreSQL Driver
- [x] Flyway Core
- [x] Flyway PostgreSQL
- [x] Spring Boot Actuator

### ✅ Phase 4: API Layer - gRPC Implementation (COMPLETED)
- [x] gRPC dependencies and plugins (grpc-spring-boot-starter, protobuf)
- [x] Protocol Buffer definitions (.proto files) - 6 service definitions
- [x] gRPC service generation and build configuration
- [x] Sample gRPC service implementation (UserService)
- [x] gRPC server configuration (port 9090)
- [x] Complete all gRPC service implementations (NutriologistService, ClientService, MeasurementService, NutritionPlanService, AuthService)
- [x] gRPC interceptors and security (JWT authentication, authorization)
- [x] Error handling and validation (custom exceptions, field validation)

#### Planned gRPC Services
1. **AuthenticationService** - Login, registration, token management
2. **UserService** - User CRUD operations
3. **NutriologistService** - Nutritionist profile management
4. **ClientService** - Client/patient management
5. **MedicalHistoryService** - Medical history operations
6. **MeasurementService** - Biometric measurements
7. **FoodDiaryService** - Food intake tracking
8. **SymptomLogService** - Symptom logging
9. **ClientGoalService** - Goal management
10. **NutritionPlanService** - Nutrition plan CRUD
11. **SupplementService** - Supplement tracking
12. **ClinicalNoteService** - Clinical notes management

### 🔄 Phase 5: Business Logic (IN PROGRESS)
- [x] JPA Entity classes (14 entities created)
- [x] Repository interfaces (9 repositories created with custom queries)
- [ ] Service layer implementation
- [ ] Business validation logic
- [ ] Transaction management

#### Completed JPA Entities
**Authentication & Users:**
- `User` - Base user entity with roles and profiles
- `Role` - User roles (NUTRIOLOGIST, PATIENT)
- `UserRole` - User-role composite key association
- `Nutriologist` - Nutritionist profiles
- `Client` - Patient/client profiles

**Medical Data:**
- `MedicalHistory` - Comprehensive medical history (50+ fields)
- `Measurement` - Biometric measurements and lab results (65+ fields)
- `SymptomLog` - Symptom tracking over time

**Dietary Tracking:**
- `FoodDiaryEntry` - Daily food intake with macros/micros

**Treatment Management:**
- `ClientGoal` - Patient goals and objectives
- `NutritionPlan` - Personalized nutrition plans
- `SupplementTracking` - Supplement prescriptions
- `ClinicalNote` - Clinical consultation notes

**Audit:**
- `AuditLog` - System audit trail with JSONB support

#### Completed Repository Interfaces
All repositories extend `JpaRepository` with custom query methods:
- `UserRepository`, `RoleRepository`, `UserRoleRepository`
- `MedicalHistoryRepository` - Find by client, check existence
- `MeasurementRepository` - Latest measurements, date ranges, client history
- `SymptomLogRepository` - Date ranges, symptom search
- `FoodDiaryEntryRepository` - Date ranges, meal types, calorie calculations
- `ClientGoalRepository` - Active goals, goal types
- `NutritionPlanRepository` - Active plans, plan types
- `SupplementTrackingRepository` - Active supplements
- `ClinicalNoteRepository` - Consultation history, date ranges
- `AuditLogRepository` - User actions, entity tracking

### ⏳ Phase 6: Security (PENDING)
- [ ] JWT authentication implementation
- [ ] Role-based access control (RBAC)
- [ ] gRPC authentication interceptors
- [ ] Password encryption (BCrypt)
- [ ] Security context management

### ⏳ Phase 7: Testing (PENDING)
- [ ] Unit tests for services
- [ ] Integration tests for gRPC services
- [ ] Database integration tests
- [ ] Security tests
- [ ] Performance tests

### ⏳ Phase 8: Observability (PENDING)
- [ ] Structured logging
- [ ] Health checks
- [ ] Metrics collection
- [ ] Distributed tracing
- [ ] Monitoring dashboard

### ⏳ Phase 9: Documentation (PENDING)
- [ ] API documentation (gRPC reflection)
- [ ] Architecture documentation
- [ ] Deployment guides
- [ ] User guides

### ⏳ Phase 10: Deployment (PENDING)
- [ ] Production Docker configuration
- [ ] CI/CD pipeline
- [ ] Environment configurations
- [ ] Cloud deployment setup

---

## Development Environment Setup

### Prerequisites
- Java 25
- Docker & Docker Compose
- PostgreSQL client (optional)
- gRPC tools (grpcurl, BloomRPC)

### Local Development Setup

1. **Start PostgreSQL:**
```bash
docker-compose up -d postgres
```

2. **Run Application:**
```bash
./gradlew bootRun
```

3. **Access Points:**
- gRPC Server: `localhost:9090` (to be configured)
- Actuator Health: `http://localhost:8080/actuator/health`
- Database: `localhost:5432`

### Database Connection
```bash
# Via Docker
docker-compose exec postgres psql -U postgres -d nutritionist_db

# Via psql
psql -h localhost -p 5432 -U postgres -d nutritionist_db
```

**Credentials:** `postgres/postgres`

### Test Users
All users have password: `password123`

**Nutriologists:**
- maria.rodriguez@nutrition.com
- john.smith@nutrition.com

**Patients:**
- sarah.johnson@email.com
- michael.chen@email.com
- emma.davis@email.com

---

## Current Architecture

### Technology Stack
- **Framework:** Spring Boot 4.0.0
- **Language:** Java 25
- **API Protocol:** gRPC (in progress)
- **Database:** PostgreSQL 16
- **Migrations:** Flyway
- **Build Tool:** Gradle 9.2.1
- **Containerization:** Docker

### Project Structure
```
nutritionist/
├── src/
│   ├── main/
│   │   ├── java/com/atlantis/nutritionist/
│   │   │   ├── NutritionistApplication.java
│   │   │   ├── config/          # Configuration classes
│   │   │   ├── entity/          # JPA entities (to be created)
│   │   │   ├── repository/      # Data access layer (to be created)
│   │   │   ├── service/         # Business logic (to be created)
│   │   │   ├── grpc/            # gRPC services (to be created)
│   │   │   └── security/        # Security configuration (to be created)
│   │   ├── proto/               # Protocol Buffer definitions (to be created)
│   │   └── resources/
│   │       ├── db/migration/
│   │       │   ├── V1__initial_schema.sql
│   │       │   └── V2__seed_data.sql
│   │       ├── application.properties
│   │       └── application-docker.properties
│   └── test/                    # Test files (to be created)
├── build.gradle.kts
├── docker-compose.yml
├── Dockerfile
├── DEVELOPMENT.md              # This file
├── README.md
├── DATABASE_SETUP.md
└── DOCKER.md
```

---

## Database Schema Details

### Authentication Flow
1. User authenticates via `users` table
2. Role checked via `user_roles` → `roles`
3. JWT token issued with role claims
4. Role-based access to endpoints

### Entity Relationships

**User → Nutriologist (1:1)**
- Foreign key: `nutriologists.user_id → users.id`

**User → Client (1:1)**
- Foreign key: `clients.user_id → users.id`

**Client → Medical History (1:1)**
- Foreign key: `medical_history.client_id → clients.id`

**Client → Measurements (1:N)**
- Foreign key: `measurements.client_id → clients.id`
- Tracks progress over time

**Client → Goals (1:N)**
- Foreign key: `client_goals.client_id → clients.id`
- Multiple active goals possible

**Client → Nutrition Plans (1:N)**
- Foreign key: `nutrition_plans.client_id → clients.id`
- Historical plans preserved

**Nutritionist → Client (M:N)**
- Via `measurements`, `client_goals`, `nutrition_plans`
- Nutritionist can have multiple clients
- Client can work with multiple nutritionists over time

---

## Development Commands

### Build
```bash
./gradlew clean build
./gradlew build --no-daemon
```

### Run
```bash
./gradlew bootRun
```

### Test
```bash
./gradlew test
./gradlew test --tests "ClassName"
```

### Database Operations
```bash
# Tables list
docker-compose exec postgres psql -U postgres -d nutritionist_db -c "\dt"

# Table structure
docker-compose exec postgres psql -U postgres -d nutritionist_db -c "\d table_name"

# Query data
docker-compose exec postgres psql -U postgres -d nutritionist_db -c "SELECT * FROM users;"
```

### Docker
```bash
# Start services
docker-compose up -d

# View logs
docker-compose logs -f

# Stop services
docker-compose down

# Rebuild
docker-compose up --build
```

---

## Known Issues & Solutions

### Issue 1: Flyway Not Running Automatically
**Problem:** Flyway migrations don't execute on Spring Boot 4.0.0 startup
**Cause:** Possible autoconfiguration issue with Spring Boot 4.0.0
**Solution:** Run migrations manually via SQL:
```bash
docker-compose exec -T postgres psql -U postgres -d nutritionist_db < src/main/resources/db/migration/V1__initial_schema.sql
docker-compose exec -T postgres psql -U postgres -d nutritionist_db < src/main/resources/db/migration/V2__seed_data.sql
```

### Issue 2: Flyway Gradle Plugin Compatibility
**Problem:** Flyway Gradle plugin incompatible with Gradle 9.2.1
**Workaround:** Use manual SQL execution instead of `./gradlew flywayMigrate`

---

## Next Steps

### Immediate (Phase 4 - gRPC)
1. Add gRPC dependencies (grpc-spring-boot-starter, protobuf)
2. Create Protocol Buffer definitions for all entities
3. Generate Java classes from .proto files
4. Implement gRPC service endpoints
5. Configure gRPC server port and settings

### Short Term (Phases 5-6)
1. Create JPA entities matching database schema
2. Implement repository layer
3. Build service layer with business logic
4. Implement JWT authentication
5. Configure role-based security

### Medium Term (Phases 7-8)
1. Comprehensive test coverage
2. Monitoring and observability
3. Performance optimization
4. Error handling and validation

### Long Term (Phases 9-10)
1. Complete documentation
2. Production deployment
3. CI/CD pipeline
4. Scaling strategy

---

## Development Best Practices

### Code Standards
- Follow Spring Boot best practices
- Use constructor injection for dependencies
- Write self-documenting code
- Add JavaDoc for public APIs
- Keep methods focused and small

### Database
- Never modify migration files after deployment
- Always create new migrations for schema changes
- Use meaningful constraint names
- Include rollback considerations

### Security
- Never commit secrets or passwords
- Use environment variables for configuration
- Implement proper input validation
- Follow OWASP security guidelines

### Testing
- Write tests before fixing bugs (TDD)
- Aim for >80% code coverage
- Include integration tests
- Test security boundaries

---

## Contributing Guidelines

### Branch Strategy
- `main` - Production-ready code
- `develop` - Integration branch
- `feature/*` - Feature branches
- `hotfix/*` - Production fixes

### Commit Messages
```
type(scope): subject

body

footer
```

Types: feat, fix, docs, style, refactor, test, chore

### Pull Request Process
1. Create feature branch from `develop`
2. Implement changes with tests
3. Update documentation
4. Submit PR with description
5. Address review comments
6. Merge after approval

---

## Resources

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [gRPC Java Documentation](https://grpc.io/docs/languages/java/)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [Flyway Documentation](https://flywaydb.org/documentation/)
- [Protocol Buffers Guide](https://protobuf.dev/)

---

## gRPC Services Status

| Service | Proto Defined | Java Generated | Implemented | Security | Tested |
|---------|--------------|----------------|-------------|----------|--------|
| AuthService | ✅ | ✅ | ✅ | ✅ Public | ❌ |
| UserService | ✅ | ✅ | 🔄 Sample | ⏳ Pending | ❌ |
| ClientService | ✅ | ✅ | ✅ | ⏳ Pending | ❌ |
| NutriologistService | ✅ | ✅ | ✅ | ⏳ Pending | ❌ |
| MeasurementService | ✅ | ✅ | ✅ | ⏳ Pending | ❌ |
| NutritionPlanService | ✅ | ✅ | ✅ | ⏳ Pending | ❌ |
| HealthCheckService | ✅ | ✅ | ✅ | ✅ Public | ❌ |

See [GRPC_API.md](GRPC_API.md) for detailed gRPC API documentation.

---

**Last Updated:** 2024-12-12
**Status:** Phase 5 IN PROGRESS - JPA entities and repositories completed (14 entities, 9 repositories). Next: Service layer implementation and database integration with gRPC services.

## Phase 4 Summary - Security & Validation Implementation

### Security Infrastructure ✅
- **JWT Authentication**: Interceptor validates tokens on all protected endpoints
- **Authorization**: Role-based access control with `@RequireRole` support
- **Password Encryption**: BCrypt password encoder configured
- **Public Endpoints**: `@PublicEndpoint` annotation for auth/health endpoints

### Exception Handling ✅
- **Centralized Handler**: GrpcExceptionHandler maps all exceptions to gRPC Status codes
- **Custom Exceptions**: AuthenticationException, AuthorizationException, ValidationException, EntityNotFoundException, EntityAlreadyExistsException
- **Error Sanitization**: Production-safe error messages

### Validation Layer ✅
- **ValidationUtils**: Email, UUID, date, phone, password strength validation
- **RequestValidator**: Field-level validation with error accumulation
- **Input Validation**: All service methods validate required fields and formats

### Services Completed ✅
- **AuthServiceImpl**: Register, Login, RefreshToken, ValidateToken, Logout (with TODOs for DB)
- **HealthCheckServiceImpl**: Public health check endpoints
- **NutriologistServiceImpl**: Full CRUD with validation placeholders
- **ClientServiceImpl**: Full CRUD with validation placeholders
- **MeasurementServiceImpl**: Measurement tracking with IMC calculation
- **NutritionPlanServiceImpl**: Nutrition plan management

### Configuration ✅
- **GrpcSecurityConfig**: Registers authentication & authorization interceptors
- **SecurityConfig**: Spring Security with BCrypt password encoder
- **application.properties**: JWT, security, and validation settings
