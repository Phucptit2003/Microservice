package com.example.notification.controller;

import com.example.notification.model.NotificationRequest;
import com.example.notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/send")
    public ResponseEntity<?> sendConfirmationEmail(@RequestBody NotificationRequest request) {
        notificationService.sendConfirmationEmail(
                request.getEmail(),
                request.getBookingId(),
                request.getMovieName(),
                request.getShowtime(),
                request.getSeats()
        );
        return ResponseEntity.ok("Email sent successfully");
    }
}
