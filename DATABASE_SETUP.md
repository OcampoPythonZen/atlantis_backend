# Database Setup - Nutritionist Application

## Database Schema

This application uses PostgreSQL as the primary database and Flyway for versioning and migrations.

## PostgreSQL Configuration

### 1. Install PostgreSQL

If you don't have PostgreSQL installed yet:

```bash
# macOS with Homebrew
brew install postgresql@16
brew services start postgresql@16

# Ubuntu/Debian
sudo apt-get install postgresql postgresql-contrib

# Verify installation
psql --version
```

### 2. Create the Database

```bash
# Connect to PostgreSQL
psql postgres

# Create the database
CREATE DATABASE nutritionist_db;

# Create user (optional, if not using postgres user)
CREATE USER nutritionist_user WITH PASSWORD 'your_password';
GRANT ALL PRIVILEGES ON DATABASE nutritionist_db TO nutritionist_user;

# Exit
\q
```

### 3. Configure Credentials

Edit the `src/main/resources/application.properties` file and update credentials:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/nutritionist_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

## Flyway Migrations

### Schema Structure

The database schema includes:

#### Authentication and Users
- `users` - System users
- `roles` - Roles (NUTRIOLOGIST, PATIENT)
- `user_roles` - User role assignments

#### Nutritionists
- `nutriologists` - Professional nutritionist profile

#### Clients/Patients
- `clients` - Basic client information
- `medical_history` - Complete medical history
- `measurements` - Biometric measurements and lab analyses
- `food_diary_entries` - Daily dietary tracking
- `symptom_logs` - Symptom tracking

#### Treatment and Follow-up
- `client_goals` - Nutritional goals
- `nutrition_plans` - Personalized nutrition plans
- `supplement_tracking` - Supplement tracking
- `clinical_notes` - Clinical consultation notes

#### Audit
- `audit_logs` - System audit logs

### Run Migrations

Migrations run automatically when starting the application.

To run migrations manually:

```bash
./gradlew flywayMigrate
```

### Useful Flyway Commands

```bash
# View migration status
./gradlew flywayInfo

# Clean database (WARNING! Deletes everything)
./gradlew flywayClean

# Repair migration history
./gradlew flywayRepair

# Validate migrations
./gradlew flywayValidate
```

### Create New Migration

1. Create a new file in `src/main/resources/db/migration/`
2. Name the file following the pattern: `V{version}__{description}.sql`
   - Example: `V2__add_appointments_table.sql`
3. Write your SQL
4. The migration will run automatically when starting the application

## Testing

### Test Configuration

Tests use the same PostgreSQL database configured in `application.properties`.

**Important:** Make sure PostgreSQL is running and the database is created before running tests.

### Run Tests

```bash
./gradlew test
```

## PostgreSQL Connection

### From Command Line

```bash
psql -h localhost -U postgres -d nutritionist_db
```

### Useful psql Commands

```sql
-- List all tables
\dt

-- Describe a table
\d users

-- View table data
SELECT * FROM users;

-- Exit
\q
```

## Troubleshooting

### Error: "database 'nutritionist_db' does not exist"

```bash
createdb nutritionist_db
```

### Error: "role 'postgres' does not exist"

```bash
createuser -s postgres
```

### Connection Error

Verify PostgreSQL is running:

```bash
# macOS
brew services list

# Linux
sudo systemctl status postgresql
```

### View Flyway Logs

Add to `application.properties`:

```properties
logging.level.org.flywaydb=DEBUG
```

## File Structure

```
src/main/resources/
├── db/
│   └── migration/
│       └── V1__initial_schema.sql
└── application.properties
```
