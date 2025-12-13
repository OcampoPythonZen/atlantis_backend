#!/bin/bash

echo "========================================="
echo "Stopping Nutritionist App - Local Setup"
echo "========================================="
echo ""

echo "🛑 Stopping PostgreSQL container..."
docker-compose down

echo ""
echo "✅ PostgreSQL stopped"
echo ""
echo "To remove data volumes as well, run:"
echo "  docker-compose down -v"
echo ""
