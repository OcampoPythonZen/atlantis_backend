#!/bin/bash

echo "========================================="
echo "Starting Nutritionist App - Local Setup"
echo "========================================="
echo ""

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo "❌ Error: Docker is not running"
    echo "Please start Docker Desktop and try again"
    exit 1
fi

echo "✅ Docker is running"
echo ""

# Start PostgreSQL
echo "📦 Starting PostgreSQL container..."
docker-compose up -d postgres

# Wait for PostgreSQL to be ready
echo "⏳ Waiting for PostgreSQL to be ready..."
for i in {1..30}; do
    if docker-compose exec -T postgres pg_isready -U postgres > /dev/null 2>&1; then
        echo "✅ PostgreSQL is ready!"
        break
    fi
    if [ $i -eq 30 ]; then
        echo "❌ PostgreSQL failed to start within 30 seconds"
        exit 1
    fi
    sleep 1
done

echo ""
echo "📊 Database Status:"
docker-compose ps

echo ""
echo "========================================="
echo "PostgreSQL is ready!"
echo "========================================="
echo ""
echo "Connection details:"
echo "  Host:     localhost"
echo "  Port:     5432"
echo "  Database: nutritionist_db"
echo "  Username: postgres"
echo "  Password: postgres"
echo ""
echo "Next steps:"
echo "  1. Run the application: ./gradlew bootRun"
echo "  2. Access health check: http://localhost:8080/actuator/health"
echo ""
echo "To stop PostgreSQL:"
echo "  docker-compose down"
echo ""
