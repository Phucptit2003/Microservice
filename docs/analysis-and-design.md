# System Analysis and Design

## 1. System Analysis

### 1.1. Requirements
The `movie_ticket_booking` system is designed to provide an online platform for users to browse movies, select showtimes, book seats, and complete payments. Key requirements include:

- **Functional Requirements**:
  - Users can register, log in, and manage their profiles.
  - Users can browse movies and showtimes by cinema.
  - Users can select and book seats for a showtime.
  - The system processes payments securely.
  - Users receive booking confirmations via notifications.
  - Admins can manage movies, showtimes, and cinemas.
- **Non-Functional Requirements**:
  - High availability: The system should handle multiple concurrent users.
  - Security: User data and transactions must be protected (using JWT).
  - Scalability: The system should scale to support growing user demand.
  - Performance: Booking process should complete within seconds.

### 1.2. Stakeholders
- **End Users**: Customers booking movie tickets.
- **Admins**: Manage movies, showtimes, and cinemas.
- **Developers**: Maintain and extend the system.

### 1.3. Use Cases
- **User Use Cases**:
  - Register and log in.
  - Browse movies and showtimes.
  - Select seats and book tickets.
  - Make payments and receive confirmations.
- **Admin Use Cases**:
  - Add, update, or delete movies.
  - Schedule showtimes for cinemas.
- **System Use Cases**:
  - Validate user authentication.
  - Lock seats to prevent double-booking.
  - Process payments securely.

## 2. System Design

### 2.1. High-Level Design
The system uses a microservice architecture with the following services:
- **User Service**: Handles user authentication and profile management.
- **Movie Service**: Manages movie data and admin operations.
- **Showtime Service**: Manages showtimes and cinema information.
- **Seat Service**: Handles seat selection and locking.
- **Booking Service**: Orchestrates the booking process.
- **Payment Service**: Processes payments.
- **Notification Service**: Sends notifications.
- **API Gateway**: Routes requests and enforces security.
- **Eureka Server**: Manages service discovery.

### 2.2. Data Model
Each service has its own data model (stored in PostgreSQL):
- **User Service**:
  - `Client`: Stores user information (ID, name, email, password hash).
- **Movie Service**:
  - `Movie`: Stores movie details (ID, title, genre, duration).
- **Showtime Service**:
  - `Showtime`: Stores showtime details (ID, movie ID, cinema ID, time).
  - `Cinema`: Stores cinema details (ID, name, location).
- **Seat Service**:
  - `Seat`: Stores seat details (ID, showtime ID, seat number, status).
- **Booking Service**:
  - `Booking`: Stores booking details (ID, user ID, showtime ID, seat IDs, payment status).
- **Payment Service**:
  - `Payment`: Stores payment details (ID, booking ID, amount, status).
- **Notification Service**:
  - `Notification`: Stores notification details (ID, user ID, message, status).

### 2.3. Design Considerations
- **Database per Service**: Each microservice uses its own PostgreSQL database to ensure loose coupling.
- **Synchronous Communication**: Services use REST APIs with Feign Client for quick interactions.
- **Security**: JWT tokens are validated at the API Gateway and individual services.
- **Concurrency**: Seat locking prevents double-booking during the booking process.
- **Error Handling**: Global exception handlers manage errors gracefully.

### 2.4. Assumptions
- Each service uses PostgreSQL as its database.
- The system is deployed locally using Docker Compose, with potential for cloud deployment.
- Payment processing integrates with an external provider (e.g., Stripe).

## 3. Future Enhancements
- Add asynchronous communication using RabbitMQ or Kafka.
- Implement caching (e.g., Redis) for frequently accessed data like movie listings.
- Add comprehensive API documentation using Swagger.
- Integrate monitoring tools (e.g., Prometheus, Grafana).