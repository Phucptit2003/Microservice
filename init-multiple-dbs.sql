CREATE DATABASE IF NOT EXISTS movie_db;
CREATE DATABASE IF NOT EXISTS showtime_db;
CREATE DATABASE IF NOT EXISTS seat_db;
CREATE DATABASE IF NOT EXISTS booking_db;
CREATE DATABASE IF NOT EXISTS payment_db;
CREATE DATABASE IF NOT EXISTS notification_db;
CREATE DATABASE IF NOT EXISTS user_db;

-- movie_db
USE movie_db;
CREATE TABLE IF NOT EXISTS movie (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  description TEXT NOT NULL,
  duration INT NOT NULL,
  rating DOUBLE NOT NULL
);
INSERT INTO movie (title, description, duration, rating) VALUES
  ('Inception', 'A mind-bending thriller', 148, 8.8),
  ('The Matrix', 'A hacker discovers reality', 136, 8.7);

-- showtime_db
USE showtime_db;
CREATE TABLE IF NOT EXISTS cinema (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255),
  location VARCHAR(255)
);
CREATE TABLE IF NOT EXISTS showtime (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  movie_id BIGINT NOT NULL,
  show_time DATETIME NOT NULL,
  available_seats INT NOT NULL,
  cinema_id BIGINT,
  FOREIGN KEY (cinema_id) REFERENCES cinema(id)
);
INSERT INTO cinema (name, location) VALUES
  ('Cinema 1', 'Downtown'),
  ('Cinema 2', 'Uptown');
INSERT INTO showtime (movie_id, show_time, available_seats, cinema_id) VALUES
  (1, '2024-07-01 19:00:00', 100, 1),
  (2, '2024-07-01 21:00:00', 80, 2);

-- seat_db
USE seat_db;
CREATE TABLE IF NOT EXISTS seats (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  showtime_id BIGINT NOT NULL,
  seat_number VARCHAR(10) NOT NULL,
  status VARCHAR(20) NOT NULL,
  locked BOOLEAN DEFAULT FALSE,
  locked_until DATETIME NULL,
  booking_id VARCHAR(50) NULL
);
INSERT INTO seats (showtime_id, seat_number, status, locked, booking_id) VALUES
  (1, 'A1', 'AVAILABLE', false, NULL),
  (1, 'A2', 'BOOKED', true, 'BKG123'),
  (1, 'A3', 'AVAILABLE', false, NULL),
  (1, 'A4', 'BOOKED', true, 'BKG124'),
  (1, 'A5', 'AVAILABLE', false, NULL),
  (1, 'A6', 'AVAILABLE', false, NULL),
  (1, 'A7', 'BOOKED', true, 'BKG125'),
  (1, 'A8', 'AVAILABLE', false, NULL),
  (1, 'A9', 'AVAILABLE', false, NULL),
  (1, 'A10', 'BOOKED', true, 'BKG126'),
  (1, 'B1', 'AVAILABLE', false, NULL),
  (1, 'B2', 'BOOKED', true, 'BKG127'),
  (1, 'B3', 'AVAILABLE', false, NULL),
  (1, 'B4', 'AVAILABLE', false, NULL),
  (1, 'B5', 'BOOKED', true, 'BKG128'),
  (1, 'B6', 'AVAILABLE', false, NULL),
  (1, 'B7', 'AVAILABLE', false, NULL),
  (1, 'B8', 'BOOKED', true, 'BKG129'),
  (1, 'B9', 'AVAILABLE', false, NULL),
  (1, 'B10', 'AVAILABLE', false, NULL),
  (1, 'C1', 'AVAILABLE', false, NULL),
  (1, 'C2', 'BOOKED', true, 'BKG130'),
  (1, 'C3', 'AVAILABLE', false, NULL),
  (1, 'C4', 'BOOKED', true, 'BKG131'),
  (1, 'C5', 'AVAILABLE', false, NULL);

-- booking_db
USE booking_db;
CREATE TABLE IF NOT EXISTS booking (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT,
  showtime_id BIGINT,
  seats VARCHAR(255),
  status VARCHAR(20),
  movie_name VARCHAR(255),
  showtime VARCHAR(255),
  user_email VARCHAR(255),
  created_at DATETIME
);
INSERT INTO booking (user_id, showtime_id, seats, status, movie_name, showtime, user_email, created_at) VALUES
  (1, 1, 'A1', 'CONFIRMED', 'Inception', '2024-07-01 19:00:00', 'user@example.com', NOW()),
  (2, 2, 'A2', 'PENDING', 'The Matrix', '2024-07-01 21:00:00', 'user2@example.com', NOW());

-- payment_db
USE payment_db;
CREATE TABLE IF NOT EXISTS payments (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  payment_id VARCHAR(225),
  order_id VARCHAR(50) NOT NULL,
  amount DECIMAL(10,2) NOT NULL,
  currency VARCHAR(10) NOT NULL,
  status VARCHAR(20),
  payment_method VARCHAR(50),
  description VARCHAR(255),
  created_at DATETIME,
  updated_at DATETIME
);
INSERT INTO payments (payment_id, order_id, amount, currency, status, payment_method, description, created_at, updated_at) VALUES
  ('PMT123', 'ORD123', 12.50, 'USD', 'COMPLETED', 'CARD', 'Movie ticket', NOW(), NOW()),
  ('PMT124', 'ORD124', 15.00, 'USD', 'PENDING', 'CARD', 'Movie ticket', NOW(), NOW());

-- notification_db
USE notification_db;
CREATE TABLE IF NOT EXISTS payment_notifications (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  customer_email VARCHAR(255) NOT NULL,
  order_id VARCHAR(50) NOT NULL,
  payment_id VARCHAR(225) NOT NULL,
  amount DECIMAL(10,2) NOT NULL,
  currency VARCHAR(10) NOT NULL,
  description VARCHAR(255) NOT NULL,
  payment_date DATETIME NOT NULL,
  payment_method VARCHAR(50),
  sent_at DATETIME NOT NULL
);
INSERT INTO payment_notifications (customer_email, order_id, payment_id, amount, currency, description, payment_date, payment_method, sent_at) VALUES
  ('user@example.com', 'ORD123', 'PMT123', 12.50, 'USD', 'Movie ticket', NOW(), 'CARD', NOW()),
  ('user2@example.com', 'ORD124', 'PMT124', 15.00, 'USD', 'Movie ticket', NOW(), 'CARD', NOW());

-- user_db
USE user_db;
CREATE TABLE IF NOT EXISTS client (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(255) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  role VARCHAR(50) NOT NULL,
  email VARCHAR(255) NOT NULL
);
INSERT INTO client (username, password, role, email) VALUES
  ('admin', 'adminpass', 'ADMIN', 'admin@example.com'),
  ('user', 'userpass', 'USER', 'user@example.com'); 