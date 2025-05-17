package com.example.seat.controller;

import com.example.seat.model.*;
import com.example.seat.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seats")
public class SeatController {

    @Autowired
    private SeatService seatService;

    @GetMapping("/test")
    public String test() {
        return "Seat Service is running!";
    }

    @GetMapping("/showtime/{showtimeId}")
    public ResponseEntity<List<SeatResponse>> getSeatsByShowtime(@PathVariable Long showtimeId) {
        List<SeatResponse> seats = seatService.getSeatsByShowtime(showtimeId);
        return ResponseEntity.ok(seats);
    }

    @PostMapping("/lock")
    public ResponseEntity<?> lockSeats(@RequestBody LockSeatRequest request) {
        try {
            List<String> unavailableSeats = seatService.getUnavailableSeats(request.getShowtimeId(), request.getSeatNumbers());
            if (!unavailableSeats.isEmpty()) {
                return ResponseEntity.badRequest().body(new ErrorResponse("Các ghế không khả dụng: " + unavailableSeats));
            }
            String bookingId = seatService.lockSeats(request.getShowtimeId(), request.getSeatNumbers());
            return ResponseEntity.ok(new LockSeatResponse(bookingId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Lỗi khi khóa ghế: " + e.getMessage()));
        }
    }

    @PostMapping("/book")
    public ResponseEntity<?> bookSeats(@RequestBody BookSeatRequest request) {
        try {
            seatService.bookSeats(request.getShowtimeId(), request.getSeatNumbers());
            return ResponseEntity.ok("Seats booked successfully");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}