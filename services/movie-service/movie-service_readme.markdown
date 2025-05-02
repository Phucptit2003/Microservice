# Movie Service

## Overview
The `movie-service` manages movie information in the `movie_ticket_booking` system. It provides APIs for users to browse movies and for admins to add, update, or delete movie data.

## Features
- List all available movies.
- Retrieve detailed information for a specific movie.
- Admin operations to manage movie data (add, update, delete).

## Technologies
- **Language**: Java (Spring Boot)
- **Database**: PostgreSQL (assumed)
- **Communication**: REST APIs
- **Security**: JWT authentication
- **Build Tool**: Maven

## Setup
1. Ensure the service is included in the `docker-compose.yml` file in the project root.
2. Configure environment variables in the project's `.env` file:
   ```plaintext
   MOVIE_DB_URL=jdbc:postgresql://db:5432/movie_db
   EUREKA_URL=http://eureka-server:8761/eureka
   JWT_SECRET=your_jwt_secret_key_here
   ```
3. Build and run the service using Docker Compose:
   ```bash
   docker-compose up -d movie-service
   ```

## API
The API specification is available in [../../docs/api-specs/movie-service.yaml](../../docs/api-specs/movie-service.yaml). Key endpoints include:
- `GET /api/movie/movies`: List all movies.
- `POST /api/movie/movies`: Add a new movie (admin-only).
- `GET /api/movie/movies/{id}`: Get movie details.

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
- Ensure the database is initialized with movie data.

## Notes
- Admin endpoints require appropriate JWT roles (e.g., `ROLE_ADMIN`).
- Ensure the database schema is set up before running the service.