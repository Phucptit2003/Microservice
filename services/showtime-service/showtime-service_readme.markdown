# Showtime Service

## Overview
The `showtime-service` manages movie showtimes and cinema information in the `movie_ticket_booking` system. It provides APIs for users to browse showtimes and for admins to schedule new showtimes.

## Features
- List all available showtimes.
- Retrieve showtime details by ID.
- Admin operations to add, update, or delete showtimes.
- Validate showtime availability for bookings.

## Technologies
- **Language**: Java (Spring Boot)
- **Database**: PostgreSQL (assumed)
- **Communication**: REST APIs with Feign Client
- **Security**: JWT authentication
- **Build Tool**: Maven

## Setup
1. Ensure the service is included in the `docker-compose.yml` file in the project root.
2. Configure environment variables in the project's `.env` file:
   ```plaintext
   SHOWTIME_DB_URL=jdbc:postgresql://db:5432/showtime_db
   EUREKA_URL=http://eureka-server:8761/eureka
   JWT_SECRET=your_jwt_secret_key_here
   ```
3. Build and run the service using Docker Compose:
   ```bash
   docker-compose up -d showtime-service
   ```

## API
The API specification is available in [../../docs/api-specs/showtime-service.yaml](../../docs/api-specs/showtime-service.yaml). Key endpoints include:
- `GET /api/showtime/showtimes`: List all showtimes.
- `POST /api/showtime/showtimes`: Add a new showtime (admin-only).
- `GET /api/showtime/showtimes/{id}`: Get showtime details.

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
- Ensure the database and `movie-service` are running for full functionality.

## Notes
- The service depends on `movie-service` for movie validation.
- Admin endpoints require appropriate JWT roles.
- Ensure the database schema is set up for showtimes and cinemas.