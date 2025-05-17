package com.example.notification.controller;

import com.example.notification.dto.PaymentNotificationDto;
import com.example.notification.service.EmailNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final EmailNotificationService emailNotificationService;

    @PostMapping("/payment-success")
    public ResponseEntity<String> sendPaymentSuccessNotification(@RequestBody PaymentNotificationDto notificationRequest) {
        emailNotificationService.sendPaymentSuccessEmail(notificationRequest);
        return ResponseEntity.ok("Payment notification sent successfully");
    }
} 