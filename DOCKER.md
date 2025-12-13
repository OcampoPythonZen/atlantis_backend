# Docker Guide - Nutritionist Application

This guide will help you run the application and PostgreSQL using Docker.

## Prerequisites

- Docker installed ([Download Docker](https://www.docker.com/products/docker-desktop))
- Docker Compose (included in Docker Desktop)

```bash
# Verify installation
docker --version
docker-compose --version
```

## Option 1: Database Only (Local Development)

**Use case:** You develop on your local machine and only need PostgreSQL in Docker.

### 1. Start PostgreSQL

```bash
# Start PostgreSQL only
docker-compose up -d postgres

# View logs
docker-compose logs -f postgres

# Verify it's running
docker-compose ps
```

### 2. Connect from Local Application

The application is already configured in `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/nutritionist_db
```

### 3. Run Application Locally

```bash
./gradlew bootRun
```

### 4. Stop PostgreSQL

```bash
docker-compose down

# To also remove data
docker-compose down -v
```

## Option 2: Application and Database in Docker

**Use case:** You want to run everything in Docker (local production/testing).

### 1. Edit docker-compose.yml

Uncomment the `app` section in `docker-compose.yml` (lines 21-37).

### 2. Start All Services

```bash
# Build and start everything
docker-compose up --build

# Or in detached mode (background)
docker-compose up --build -d

# View logs
docker-compose logs -f

# App logs only
docker-compose logs -f app
```

### 3. Access the Application

- **Application:** http://localhost:8080
- **Health Check:** http://localhost:8080/actuator/health
- **Database:** localhost:5432

### 4. Manage Services

```bash
# View status
docker-compose ps

# Stop services
docker-compose stop

# Restart services
docker-compose restart

# Stop and remove containers
docker-compose down

# Remove containers and volumes (WARNING! Deletes data)
docker-compose down -v
```

## Option 3: Build Image for Deployment

**Use case:** Create Docker image for production deployment.

### 1. Build the Image

```bash
# Build with tag
docker build -t nutritionist-app:latest .

# Build with specific version
docker build -t nutritionist-app:1.0.0 .
```

### 2. Run Image Only

```bash
# Run with environment variables
docker run -d \
  --name nutritionist-app \
  -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/nutritionist_db \
  -e SPRING_DATASOURCE_USERNAME=postgres \
  -e SPRING_DATASOURCE_PASSWORD=postgres \
  -e SPRING_PROFILES_ACTIVE=docker \
  nutritionist-app:latest

# View logs
docker logs -f nutritionist-app

# Stop
docker stop nutritionist-app
docker rm nutritionist-app
```

### 3. Publish to Docker Hub

```bash
# Login
docker login

# Tag
docker tag nutritionist-app:latest your-username/nutritionist-app:latest

# Push
docker push your-username/nutritionist-app:latest
```

## Environment Variables

### Database Configuration

| Variable | Default | Description |
|----------|---------|-------------|
| `POSTGRES_DB` | `nutritionist_db` | Database name |
| `POSTGRES_USER` | `postgres` | PostgreSQL user |
| `POSTGRES_PASSWORD` | `postgres` | PostgreSQL password |
| `POSTGRES_PORT` | `5432` | PostgreSQL port |

### Application Configuration

| Variable | Default | Description |
|----------|---------|-------------|
| `SPRING_PROFILES_ACTIVE` | `docker` | Spring Boot profile |
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://postgres:5432/nutritionist_db` | Connection URL |
| `APP_PORT` | `8080` | Application port |
| `JAVA_OPTS` | `-Xmx512m -Xms256m` | JVM options |

### .env File

You can create a `.env` file based on `.env.example`:

```bash
# Copy example
cp .env.example .env

# Edit values
nano .env
```

Docker Compose will automatically read variables from the `.env` file.

## Database Access

### From Command Line

```bash
# Connect to PostgreSQL in Docker
docker-compose exec postgres psql -U postgres -d nutritionist_db

# Or using local psql
psql -h localhost -U postgres -d nutritionist_db
```

### Using pgAdmin or DBeaver

- **Host:** localhost
- **Port:** 5432
- **Database:** nutritionist_db
- **Username:** postgres
- **Password:** postgres

## Flyway Migrations

Migrations run automatically when starting the application.

### Check Migration Status

```bash
# Inside app container
docker-compose exec app ./gradlew flywayInfo

# From your local machine (with PostgreSQL in Docker)
./gradlew flywayInfo
```

## Health Checks

The application includes configured health checks:

### Actuator Endpoints

- **Health:** http://localhost:8080/actuator/health
- **Info:** http://localhost:8080/actuator/info
- **Metrics:** http://localhost:8080/actuator/metrics

### Health Check in Docker

```bash
# View container health status
docker inspect --format='{{json .State.Health}}' nutritionist-app | jq
```

## Troubleshooting

### PostgreSQL Doesn't Start

```bash
# View detailed logs
docker-compose logs postgres

# Check if port is in use
lsof -i :5432

# Remove volumes and restart
docker-compose down -v
docker-compose up -d postgres
```

### Application Can't Connect to PostgreSQL

```bash
# Verify PostgreSQL is healthy
docker-compose ps

# Check app logs
docker-compose logs app

# Ensure app waits for PostgreSQL to be ready
# (already configured with depends_on and healthcheck)
```

### Error: "Port already in use"

```bash
# Change port in docker-compose.yml
ports:
  - "8081:8080"  # Use port 8081 externally
```

### Clean Everything and Start Fresh

```bash
# Stop and remove everything
docker-compose down -v

# Remove images (optional)
docker rmi nutritionist-app

# Rebuild
docker-compose up --build
```

## Useful Commands

```bash
# View all containers
docker ps -a

# View logs in real-time
docker-compose logs -f

# Execute command in container
docker-compose exec postgres psql -U postgres

# View resource usage
docker stats

# Clean Docker system
docker system prune -a

# Database backup
docker-compose exec postgres pg_dump -U postgres nutritionist_db > backup.sql

# Restore backup
docker-compose exec -T postgres psql -U postgres nutritionist_db < backup.sql
```

## Docker File Structure

```
nutritionist/
├── Dockerfile                              # Application image
├── docker-compose.yml                       # Service orchestration
├── .dockerignore                           # Files to ignore in build
├── .env.example                            # Environment variables example
└── src/main/resources/
    ├── application.properties              # Default config (local)
    └── application-docker.properties       # Docker config
```

## Best Practices

1. **Never commit `.env`** with real credentials
2. **Use volumes** to persist PostgreSQL data
3. **Health checks** for production monitoring
4. **Multi-stage builds** to optimize image size
5. **Non-root user** in container for security
6. **Environment variables** for flexible configuration

## Production Deployment

For cloud service deployment:

- **AWS ECS/EKS:** Use built images
- **Google Cloud Run:** Compatible with Dockerfile
- **Azure Container Instances:** Supports Docker Compose
- **DigitalOcean App Platform:** Detects Dockerfile automatically
- **Heroku:** Use Heroku Container Registry

See platform-specific documentation for deployment details.
