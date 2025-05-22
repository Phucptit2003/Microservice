package com.example.paymentservice.service;

import com.example.paymentservice.dto.PaymentNotificationDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentNotificationSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void sendPaymentNotification(PaymentNotificationDto dto) {
        try {
            String message = objectMapper.writeValueAsString(dto);
            rabbitTemplate.convertAndSend("payment-notification-queue", message);
        } catch (Exception e) {
            System.err.println("Failed to send payment notification: " + e.getMessage());
        }
    }
} 