# Seat Service

## Overview
The `seat` service manages seat selection and locking in the `movie_ticket_booking` system. It ensures seats are locked to prevent double-booking and confirms bookings once payments are processed.

## Features
- Lock seats for a specific showtime.
- Book locked seats for a confirmed booking.
- Check seat availability.

## Technologies
- **Language**: Java (Spring Boot)
- **Database**: PostgreSQL (assumed)
- **Communication**: REST APIs
- **Build Tool**: Maven

## Setup
1. Ensure the service is included in the `docker-compose.yml` file in the project root.
2. Configure environment variables in the project's `.env` file:
   ```plaintext
   SEAT_DB_URL=jdbc:postgresql://db:5432/seat_db
   EUREKA_URL=http://eureka-server:8761/eureka
   ```
3. Build and run the service using Docker Compose:
   ```bash
   docker-compose up -d seat
   ```

## API
The API specification is available in [../../docs/api-specs/seat.yaml](../../docs/api-specs/seat.yaml). Key endpoints include:
- `POST /api/seat/lock`: Lock seats for a showtime.
- `POST /api/seat/book`: Book locked seats.

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
- Ensure the database is initialized with seat data.

## Notes
- Ensure concurrency control (e.g., optimistic locking) to prevent double-booking.
- The service depends on `showtime-service` for showtime validation.
- Add database migration scripts for seat initialization.