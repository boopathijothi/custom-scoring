# Makefile for building Gradle project and managing Docker Compose

# Variables (customize if needed)
GRADLE_CMD=./gradlew
DOCKER_COMPOSE_CMD=docker-compose

.PHONY: all build docker-up docker-down clean

# Default command
all: build docker-up

# Build the Gradle project
build:
	@echo "🔨 Building with Gradle..."
	$(GRADLE_CMD) clean build --no-daemon

# Start Docker Compose and build images
docker-up:
	@echo "🚀 Starting Docker containers..."
	$(DOCKER_COMPOSE_CMD) up --build -d

# Stop and remove Docker containers, networks, and images created by up
docker-down:
	@echo "🧹 Stopping and removing Docker containers..."
	$(DOCKER_COMPOSE_CMD) down --volumes --remove-orphans

# Clean everything
clean: docker-down
	@echo "🧽 Running Gradle clean..."
	$(GRADLE_CMD) clean
