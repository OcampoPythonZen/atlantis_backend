# Local Testing Guide

Quick guide to test the application locally with PostgreSQL in Docker.

## Prerequisites

- Docker Desktop installed and running
- Java 25 installed
- Port 5432 available (PostgreSQL)
- Port 8080 available (Application)

## Quick Start

### Option 1: Using Helper Scripts (Recommended)

```bash
# Start PostgreSQL
./start-local.sh

# In another terminal, run the application
./gradlew bootRun

# When done, stop PostgreSQL
./stop-local.sh
```

### Option 2: Manual Steps

```bash
# 1. Start PostgreSQL
docker-compose up -d postgres

# 2. Verify PostgreSQL is running
docker-compose ps

# 3. Check PostgreSQL logs (optional)
docker-compose logs -f postgres

# 4. Run the application
./gradlew bootRun

# 5. Stop when done
docker-compose down
```

## Verification Steps

### 1. Check PostgreSQL is Running

```bash
docker-compose ps
```

Expected output:
```
NAME                    IMAGE                 STATUS
nutritionist-postgres   postgres:16-alpine    Up (healthy)
```

### 2. Test Database Connection

```bash
docker-compose exec postgres psql -U postgres -d nutritionist_db -c "\dt"
```

Expected: List of all tables created by Flyway migration

### 3. Check Application Health

Once the app is running, open:
```
http://localhost:8080/actuator/health
```

Expected response:
```json
{
  "status": "UP"
}
```

### 4. Verify Flyway Migrations

Check application logs for:
```
Successfully applied 1 migration to schema "public"
```

## Testing Database

### Connect via psql

```bash
docker-compose exec postgres psql -U postgres -d nutritionist_db
```

### Useful SQL Commands

```sql
-- List all tables
\dt

-- Describe a table
\d users

-- Check roles
SELECT * FROM roles;

-- Exit
\q
```

### Sample Data Insert

```sql
-- Insert a test user
INSERT INTO users (email, password, first_name, last_name, is_active)
VALUES ('test@example.com', '$2a$10$encrypted_password', 'Test', 'User', true);

-- Check the insert
SELECT * FROM users;
```

## Common Issues

### Port 5432 Already in Use

```bash
# Check what's using the port
lsof -i :5432

# If local PostgreSQL is running, stop it
brew services stop postgresql@16
```

### Docker Not Running

```bash
# Check Docker status
docker info

# Start Docker Desktop manually if needed
```

### PostgreSQL Not Starting

```bash
# View logs
docker-compose logs postgres

# Remove volumes and restart
docker-compose down -v
docker-compose up -d postgres
```

### Application Can't Connect to Database

1. Verify PostgreSQL is healthy:
   ```bash
   docker-compose ps
   ```

2. Check application.properties has correct settings:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/nutritionist_db
   spring.datasource.username=postgres
   spring.datasource.password=postgres
   ```

## Logs and Debugging

### View Application Logs

Application logs appear in the terminal where you run `./gradlew bootRun`

Look for:
- ✅ Flyway migration messages
- ✅ "Started NutritionistApplication in X seconds"
- ❌ Any error messages

### View PostgreSQL Logs

```bash
docker-compose logs -f postgres
```

### Enable Debug Logging

Add to `application.properties`:
```properties
logging.level.org.flywaydb=DEBUG
logging.level.org.hibernate.SQL=DEBUG
logging.level.com.atlantis.nutritionist=DEBUG
```

## Clean Restart

If you need to start fresh:

```bash
# Stop and remove everything including data
docker-compose down -v

# Start fresh
./start-local.sh
./gradlew bootRun
```

## Next Steps After Successful Test

1. ✅ PostgreSQL running in Docker
2. ✅ Application connects successfully
3. ✅ Flyway migrations applied
4. ✅ Health check returns UP

Now you can:
- Implement REST endpoints
- Create JPA entities
- Write integration tests
- Build Docker image for deployment

## Useful Commands

```bash
# View running containers
docker ps

# View container stats
docker stats nutritionist-postgres

# Connect to PostgreSQL
psql -h localhost -U postgres -d nutritionist_db

# Backup database
docker-compose exec postgres pg_dump -U postgres nutritionist_db > backup.sql

# Restore database
docker-compose exec -T postgres psql -U postgres nutritionist_db < backup.sql

# Clean build
./gradlew clean build

# Build without tests
./gradlew clean build -x test
```
