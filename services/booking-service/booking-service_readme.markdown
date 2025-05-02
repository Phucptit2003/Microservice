# Booking Service

## Overview
The `booking-service` is a core microservice in the `movie_ticket_booking` system, responsible for orchestrating the movie ticket booking process. It integrates with `showtime-service`, `seat`, `payment-service`, and `notification-service` to validate showtimes, lock seats, process payments, and send booking confirmations.

## Features
- Create and confirm bookings.
- Validate showtimes and seat availability.
- Coordinate payment processing with the `payment-service`.
- Trigger notifications via the `notification-service` for booking confirmations.

## Technologies
- **Language**: Java (Spring Boot)
- **Database**: PostgreSQL (assumed)
- **Communication**: REST APIs with Feign Client for synchronous interactions
- **Security**: JWT authentication
- **Build Tool**: Maven

## Setup
1. Ensure the service is included in the `docker-compose.yml` file in the project root.
2. Configure environment variables in the project's `.env` file:
   ```plaintext
   BOOKING_DB_URL=jdbc:postgresql://db:5432/booking_db
   EUREKA_URL=http://eureka-server:8761/eureka
   JWT_SECRET=your_jwt_secret_key_here
   ```
3. Build and run the service using Docker Compose:
   ```bash
   docker-compose up -d booking-service
   ```

## API
The API specification is available in [../../docs/api-specs/booking-service.yaml](../../docs/api-specs/booking-service.yaml). Key endpoints include:
- `POST /api/booking/book`: Create a new booking.
- `POST /api/booking/confirm`: Confirm a booking after payment.

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
- Ensure the database and dependent services (`showtime-service`, `seat`, `payment-service`, `notification-service`) are running.

## Notes
- The service depends on `showtime-service`, `seat`, `payment-service`, and `notification-service` for full functionality.
- Add database migration scripts for production deployment.
- Ensure JWT secret is consistent across services for authentication.