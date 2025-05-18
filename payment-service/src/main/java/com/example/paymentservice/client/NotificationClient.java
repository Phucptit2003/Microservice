package com.example.paymentservice.client;

import com.example.paymentservice.dto.PaymentNotificationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "NOTIFICATION-SERVICE")
public interface NotificationClient {
    @PostMapping("/api/notifications/payment-success")
    void sendPaymentSuccessNotification(@RequestBody PaymentNotificationDto notificationRequest);
} 