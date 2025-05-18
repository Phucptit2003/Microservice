# Payment Service

## Overview
The `payment-service` processes payments for bookings in the `movie_ticket_booking` system. It integrates with an external payment provider (e.g., Stripe, PayPal) to handle transactions.

## Features
- Process payments for bookings.
- Check payment status.
- Support multiple payment methods (credit card, debit card, PayPal).

## Technologies
- **Language**: Java (Spring Boot)
- **Database**: PostgreSQL (assumed)
- **Communication**: REST APIs
- **External Integration**: Payment provider (e.g., Stripe)
- **Build Tool**: Maven

## Setup
1. Ensure the service is included in the `docker-compose.yml` file in the project root.
2. Configure environment variables in the project's `.env` file:
   ```plaintext
   PAYMENT_DB_URL=jdbc:postgresql://db:5432/payment_db
   EUREKA_URL=http://eureka-server:8761/eureka
   PAYMENT_PROVIDER_API_KEY=your_payment_provider_key
   ```
3. Build and run the service using Docker Compose:
   ```bash
   docker-compose up -d payment-service
   ```

## API
The API specification is available in [../../docs/api-specs/payment-service.yaml](../../docs/api-specs/payment-service.yaml). Key endpoints include:
- `POST /api/payment/pay`: Process a payment.
- `GET /api/payment/status/{paymentId}`: Check payment status.

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
- Ensure the database and payment provider are configured.

## Notes
- Configure the payment provider (e.g., Stripe) with the appropriate API key.
- Ensure the database is initialized for storing payment records.
- Implement retry logic for failed payment attempts.