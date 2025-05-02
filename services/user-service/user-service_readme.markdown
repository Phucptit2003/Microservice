# User Service

## Overview
The `user-service` manages user authentication and profile information in the `movie_ticket_booking` system. It provides APIs for user registration, login, and profile management, using JWT for secure authentication.

## Features
- User registration and login with JWT token generation.
- Retrieve and update user profiles.
- Secure endpoints with Spring Security and JWT.

## Technologies
- **Language**: Java (Spring Boot)
- **Database**: PostgreSQL (assumed)
- **Communication**: REST APIs
- **Security**: JWT with Spring Security
- **Build Tool**: Maven

## Setup
1. Ensure the service is included in the `docker-compose.yml` file in the project root.
2. Configure environment variables in the project's `.env` file:
   ```plaintext
   USER_DB_URL=jdbc:postgresql://db:5432/user_db
   EUREKA_URL=http://eureka-server:8761/eureka
   JWT_SECRET=your_jwt_secret_key_here
   ```
3. Build and run the service using Docker Compose:
   ```bash
   docker-compose up -d user-service
   ```

## API
The API specification is available in [../../docs/api-specs/user-service.yaml](../../docs/api-specs/user-service.yaml). Key endpoints include:
- `POST /api/user/register`: Register a new user.
- `POST /api/user/login`: Authenticate a user and return a JWT token.
- `GET /api/user/profile`: Get the authenticated user's profile.

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
- Ensure the database is initialized with user data.

## Notes
- Ensure the JWT secret is consistent across services (e.g., `api-gateway`).
- The service is critical for authentication across the system.
- Add database migration scripts for user table initialization.