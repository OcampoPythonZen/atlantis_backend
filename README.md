# Nutritionist Application

Professional nutritional management Spring Boot application with PostgreSQL and Flyway.

## 🚀 Quick Start

### Option 1: Docker (Recommended)

```bash
# Start PostgreSQL in Docker
docker-compose up -d postgres

# Run the application locally
./gradlew bootRun
```

**Access:**
- Application: http://localhost:8080
- Health Check: http://localhost:8080/actuator/health
- Database: localhost:5432

### Option 2: Local PostgreSQL

```bash
# Create database
psql postgres
CREATE DATABASE nutritionist_db;
\q

# Run the application
./gradlew bootRun
```

## 📚 Documentation

- **[DOCKER.md](DOCKER.md)** - Complete Docker guide (development and deployment)
- **[DATABASE_SETUP.md](DATABASE_SETUP.md)** - PostgreSQL and Flyway configuration

## 🏗️ Architecture

### Technology Stack

- **Framework:** Spring Boot 4.0.0
- **Language:** Java 25
- **Database:** PostgreSQL 16
- **Migrations:** Flyway
- **Build:** Gradle
- **Containers:** Docker

### Main Dependencies

- Spring Data JPA
- Spring Security
- Spring Web MVC
- Spring Boot Actuator
- PostgreSQL Driver
- Flyway Core

## 📊 Database Schema

The schema includes 17 tables organized into:

### Authentication and Users
- `users`, `roles`, `user_roles`

### Professional Profiles
- `nutriologists` - Professional nutritionists
- `clients` - Patients/Clients

### Medical Information
- `medical_history` - Complete medical history
- `measurements` - Biometric measurements and labs
- `food_diary_entries` - Daily dietary tracking
- `symptom_logs` - Symptom tracking

### Treatment Management
- `client_goals` - Nutritional goals
- `nutrition_plans` - Personalized nutrition plans
- `supplement_tracking` - Supplement tracking
- `clinical_notes` - Clinical notes

### Audit
- `audit_logs` - Audit logs

## 🔧 Useful Commands

### Development

```bash
# Build
./gradlew build

# Run tests
./gradlew test

# Run application
./gradlew bootRun

# Clean build
./gradlew clean
```

### Docker

```bash
# Database only
docker-compose up -d postgres

# View logs
docker-compose logs -f

# Stop
docker-compose down

# App + DB in Docker
docker-compose up --build
```

### Database

```bash
# Connect to PostgreSQL (Docker)
docker-compose exec postgres psql -U postgres -d nutritionist_db

# Migration status
./gradlew flywayInfo

# Run migrations
./gradlew flywayMigrate
```

## 🌐 Endpoints

### Actuator (Monitoring)

- `GET /actuator/health` - Health status
- `GET /actuator/info` - Application information
- `GET /actuator/metrics` - Metrics

## 📁 Project Structure

```
nutritionist/
├── src/
│   ├── main/
│   │   ├── java/com/atlantis/nutritionist/
│   │   │   └── NutritionistApplication.java
│   │   └── resources/
│   │       ├── db/migration/
│   │       │   └── V1__initial_schema.sql
│   │       ├── application.properties
│   │       └── application-docker.properties
│   └── test/
│       └── java/com/atlantis/nutritionist/
│           └── NutritionistApplicationTests.java
├── Dockerfile
├── docker-compose.yml
├── .dockerignore
├── .env.example
├── build.gradle.kts
├── DOCKER.md
└── DATABASE_SETUP.md
```

## ⚙️ Configuration

### Environment Variables

```bash
# Copy example
cp .env.example .env

# Edit configuration
nano .env
```

### Spring Profiles

- **default** - Local development (application.properties)
- **docker** - Docker execution (application-docker.properties)
- **prod** - Production (configure as needed)

## 🐳 Deploy

### Build Docker Image

```bash
docker build -t nutritionist-app:latest .
```

### Cloud Deployment

Compatible with:
- AWS ECS/EKS
- Google Cloud Run
- Azure Container Instances
- DigitalOcean App Platform
- Heroku Container Registry

See [DOCKER.md](DOCKER.md) for detailed instructions.

## 🧪 Testing

```bash
# Run all tests
./gradlew test

# With PostgreSQL in Docker
docker-compose up -d postgres
./gradlew test
```

## 📝 Important Notes

- Application uses **PostgreSQL only** (no H2)
- Flyway runs migrations automatically on startup
- Make sure PostgreSQL is running before executing
- Health check is available at `/actuator/health`

## 🔒 Security

- Spring Security enabled
- Non-root user in Docker container
- Credentials via environment variables
- Never commit `.env` files with secrets

## 📖 Resources

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Flyway Documentation](https://flywaydb.org/documentation/)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [Docker Documentation](https://docs.docker.com/)

## 👥 System Roles

1. **NUTRIOLOGIST** - Professional nutritionists
2. **PATIENT** - Patients/Clients

## 🚦 Project Status

- ✅ Database configured
- ✅ Flyway migrations
- ✅ Docker configured
- ✅ Health checks
- 🔄 Development in progress

---

**Version:** 0.0.1-SNAPSHOT
**Java:** 25
**Spring Boot:** 4.0.0
