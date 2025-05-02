#!/bin/bash

# Initialize the movie_ticket_booking project

echo "Starting project initialization..."

# Check if Docker is installed
if ! command -v docker &> /dev/null; then
    echo "Error: Docker is not installed. Please install Docker and try again."
    exit 1
fi

# Check if Docker Compose is installed
if ! command -v docker-compose &> /dev/null; then
    echo "Error: Docker Compose is not installed. Please install Docker Compose and try again."
    exit 1
fi

# Check if .env file exists
if [ ! -f .env ]; then
    echo "Creating .env from .env.example..."
    cp .env.example .env
    echo "Please update .env with your configuration."
else
    echo ".env file found."
fi

# Build all services
echo "Building Docker images..."
docker-compose build

# Initialize databases (assumed PostgreSQL)
echo "Setting up databases..."
docker-compose up -d db
sleep 5 # Wait for database to start

# Run database migrations (placeholder, replace with actual migration command)
# Example: docker-compose exec db psql -U admin -d movie_db -f /init.sql
echo "Database initialization placeholder. Add migration scripts as needed."

echo "Initialization complete. Run 'docker-compose up -d' to start the application."