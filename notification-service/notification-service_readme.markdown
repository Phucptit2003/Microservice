# Notification Service

## Overview
The `notification-service` handles sending notifications in the `movie_ticket_booking` system, such as booking confirmations, to users via email, SMS, or push notifications.

## Features
- Send notifications to users based on booking events.
- Support multiple notification types (email, SMS, push).

## Technologies
- **Language**: Java (Spring Boot)
- **Database**: PostgreSQL (assumed)
- **Communication**: REST APIs
- **External Integration**: Notification provider (e.g., SendGrid, Twilio)
- **Build Tool**: Maven

## Setup
1. Ensure the service is included in the `docker-compose.yml` file in the project root.
2. Configure environment variables in the project's `.env` file:
   ```plaintext
   NOTIFICATION_DB_URL=jdbc:postgresql://db:5432/notification_db
   EUREKA_URL=http://eureka-server:8761/eureka
   NOTIFICATION_PROVIDER_API_KEY=your_notification_provider_key
   ```
3. Build and run the service using Docker Compose:
   ```bash
   docker-compose up -d notification-service
   ```

## API
The API specification is available in [../../docs/api-specs/notification-service.yaml](../../docs/api-specs/notification-service.yaml). Key endpoints include:
- `POST /api/notification/notify`: Send a notification to a user.

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
- Ensure the database and notification provider are configured.

## Notes
- Configure the notification provider (e.g., SendGrid, Twilio) with the appropriate API key.
- Ensure the database is initialized for storing notification logs.
- Consider adding retry mechanisms for failed notifications.