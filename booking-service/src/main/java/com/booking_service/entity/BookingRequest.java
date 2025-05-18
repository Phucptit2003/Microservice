package com.booking_service.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class BookingRequest {
    @JsonProperty("user")
    private String customerEmail;

    @JsonProperty("showtimeId")
    private long showtimeId;

    @JsonProperty("seats")
    private List<String> seats;

    @JsonProperty("movie")
    private Movie movie;

    @JsonProperty("showtimeTime")
    private String showtimeTime;

    private double amount;
    private String currency;
    private String successUrl;
    private String cancelUrl;

    // Constructor mặc định
    public BookingRequest() {}

    // Constructor đầy đủ
    public BookingRequest(String customerEmail, long showtimeId, List<String> seats, Movie movie, String showtimeTime) {
        this.customerEmail = customerEmail;
        this.showtimeId = showtimeId;
        this.seats = seats;
        this.movie = movie;
        this.showtimeTime = showtimeTime;
    }

    // Getters và Setters
    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setUserEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public long getShowtimeId() {
        return showtimeId;
    }

    public void setShowtimeId(long showtimeId) {
        this.showtimeId = showtimeId;
    }

    public List<String> getSeats() {
        return seats;
    }

    public void setSeats(List<String> seats) {
        this.seats = seats;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public String getShowtimeTime() {
        return showtimeTime;
    }

    public void setShowtimeTime(String showtimeTime) {
        this.showtimeTime = showtimeTime;
    }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getSuccessUrl() { return successUrl; }
    public void setSuccessUrl(String successUrl) { this.successUrl = successUrl; }
    public String getCancelUrl() { return cancelUrl; }
    public void setCancelUrl(String cancelUrl) { this.cancelUrl = cancelUrl; }

    public static class User {
        @JsonProperty("id")
        private String id;

        @JsonProperty("username")
        private String username;

        @JsonProperty("role")
        private String role;

        @JsonProperty("email")
        private String email;

        // Constructor mặc định
        public User() {}

        // Constructor đầy đủ
        public User(String id, String username, String role, String email) {
            this.id = id;
            this.username = username;
            this.role = role;
            this.email = email;
        }

        // Getters và Setters
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    public static class Movie {
        @JsonProperty("id")
        private String id;

        @JsonProperty("title")
        private String title;

        // Constructor mặc định
        public Movie() {}

        // Constructor đầy đủ
        public Movie(String id, String title) {
            this.id = id;
            this.title = title;
        }

        // Getters và Setters
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}