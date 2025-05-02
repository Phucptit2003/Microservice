# Eureka Server

## Overview
The `eureka-server` is a service discovery component in the `movie_ticket_booking` system. It allows microservices to register and discover each other dynamically, enabling communication in a distributed environment.

## Features
- Register microservices for discovery.
- Provide service discovery for the API Gateway and other services.
- Support dynamic scaling of microservices.

## Technologies
- **Language**: Java (Spring Boot, Spring Cloud Netflix Eureka)
- **Build Tool**: Maven

## Setup
1. Ensure the service is included in the `docker-compose.yml` file in the project root.
2. Configure environment variables in the project's `.env` file:
   ```plaintext
   EUREKA_URL=http://eureka-server:8761/eureka
   ```
3. Build and run the service using Docker Compose:
   ```bash
   docker-compose up -d eureka-server
   ```

## API
The Eureka Server does not expose public APIs for clients. It provides a dashboard at `http://localhost:8761` for monitoring registered services.

## Development
- **Source Code**: Located in `src/`
- **Build**:
  ```bash
  ./mvnw clean package
  ```
- **Run Locally**:
  ```bash
  ./mvnw spring-boot:run
  ```

## Notes
- The Eureka Server must be running before other microservices and the API Gateway.
- Monitor the Eureka dashboard to ensure all services are registered.
- Consider enabling high availability by running multiple Eureka instances in production.