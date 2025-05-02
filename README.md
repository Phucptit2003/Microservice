# Movie Ticket Booking System

## Overview
The `movie_ticket_booking` system is a microservice-based application for online movie ticket booking. It allows users to browse movies, select showtimes, book seats, make payments, and receive notifications. The system is built using **Spring Boot**, **Docker**, and **Spring Cloud**, with **Eureka Server** for service discovery and **API Gateway** for request routing. Security is enforced using **JWT**.

## Features
- User registration and authentication.
- Browse movies and showtimes by cinema.
- Select and book seats for a showtime.
- Process payments securely.
- Send booking confirmation notifications.
- Admin functionality to manage movies and showtimes.

## Architecture
The system follows a microservice architecture, with the following components:
- **API Gateway**: Routes requests and handles JWT authentication.
- **Eureka Server**: Manages service discovery.
- **Microservices**:
  - `user-service`: User authentication and profile management.
  - `movie-service`: Movie information and administration.
  - `showtime-service`: Showtime and cinema management.
  - `seat`: Seat selection and locking.
  - `booking-service`: Booking orchestration.
  - `payment-service`: Payment processing.
  - `notification-service`: Notification delivery.
- **Databases**: Each service uses its own database (assumed PostgreSQL).
- **Communication**: REST APIs with Feign Client for synchronous interactions.

See [docs/architecture.md](docs/architecture.md) for details.

## Getting Started

### Prerequisites
- Docker and Docker Compose
- Java 17
- Maven
- Git

### Setup
1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd movie_ticket_booking
   ```
2. Copy the example environment file:
   ```bash
   cp .env.example .env
   ```
3. Update `.env` with your configuration (e.g., database credentials).
4. Run the initialization script:
   ```bash
   chmod +x scripts/init.sh
   ./scripts/init.sh
   ```
5. Start all services:
   ```bash
   docker-compose up -d
   ```
6. Access the API Gateway at `http://localhost:8080`.

### Documentation
- System architecture: [docs/architecture.md](docs/architecture.md)
- Analysis and design: [docs/analysis-and-design.md](docs/analysis-and-design.md)
- API specifications: [docs/api-specs/](docs/api-specs/)
- Architecture diagram: [docs/asset/architecture-diagram.txt](docs/asset/architecture-diagram.txt)

### Services
Each microservice is located in the `services/` folder, with its own `Dockerfile`, source code (`src/`), and `readme.md` for specific instructions:
- `services/booking-service`
- `services/movie-service`
- `services/notification-service`
- `services/payment-service`
- `services/seat`
- `services/showtime-service`
- `services/user-service`
- `gateway`: API Gateway configuration.

## Contributing
1. Fork the repository.
2. Create a feature branch (`git checkout -b feature/your-feature`).
3. Commit your changes (`git commit -m 'Add your feature'`).
4. Push to the branch (`git push origin feature/your-feature`).
5. Create a Pull Request.

## License
MIT License