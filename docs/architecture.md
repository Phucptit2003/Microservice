# System Architecture

## Overview
The `movie_ticket_booking` system is a microservice-based application designed for online movie ticket booking. It comprises multiple independent services, each handling a specific business function, orchestrated using Docker Compose and managed via Spring Cloud Netflix Eureka for service discovery. An API Gateway routes client requests, and JWT-based authentication ensures security.

## Architecture Diagram
The system follows a microservice architecture with the following components:
- **API Gateway**: Routes requests and handles JWT authentication.
- **Eureka Server**: Manages service discovery.
- **Microservices**:
  - **User Service**: Manages user authentication and profiles.
  - **Movie Service**: Handles movie information and administration.
  - **Showtime Service**: Manages movie showtimes and cinema details.
  - **Seat Service**: Manages seat locking and booking.
  - **Booking Service**: Coordinates ticket booking, integrating with other services.
  - **Payment Service**: Processes payments.
  - **Notification Service**: Sends notifications (e.g., booking confirmations).
- **Database**: Each service uses its own PostgreSQL database.
- **Communication**: Services communicate synchronously via REST APIs using Feign Client.

![Architecture Diagram](./asset/architecture-diagram.png)

## Component Interactions
1. **Client Requests**: Users interact with the system via the API Gateway, which authenticates requests using JWT.
2. **Service Discovery**: The Eureka Server registers all microservices, allowing dynamic discovery.
3. **Booking Flow**:
   - The `booking-service` receives a booking request, validates it with the `showtime-service`, locks seats via the `seat`, processes payment through the `payment-service`, and sends a confirmation via the `notification-service`.
   - The `user-service` verifies user identity during the process.
4. **Data Management**: Each service maintains its own database, adhering to the *database-per-service* pattern.

## Technologies
- **Language**: Java (Spring Boot).
- **Service Discovery**: Spring Cloud Netflix Eureka.
- **API Gateway**: Spring Cloud Gateway.
- **Security**: JWT with Spring Security.
- **Containerization**: Docker and Docker Compose.
- **Database**: PostgreSQL (assumed).
- **Build Tool**: Maven.

## Scalability and Resilience
- **Scalability**: Each microservice can be scaled independently using Docker Compose or Kubernetes.
- **Resilience**: The system uses exception handling (e.g., `GlobalExceptionHandler`) to manage errors gracefully.
- **Future Improvements**:
  - Add a message queue (e.g., RabbitMQ) for asynchronous communication.
  - Implement monitoring with Prometheus and Grafana.