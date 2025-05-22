package com.example.notification.service;

import com.example.notification.config.RabbitMQConfig;
import com.example.notification.dto.PaymentNotificationDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {
    @Autowired
    private EmailNotificationService emailNotificationService;

    @Autowired
    private ObjectMapper objectMapper;

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void receiveMessage(String message) {
        System.out.println("Received payment notification: " + message);
        try {
            PaymentNotificationDto dto = objectMapper.readValue(message, PaymentNotificationDto.class);
            emailNotificationService.sendPaymentSuccessEmail(dto);
        } catch (Exception e) {
            System.err.println("Failed to parse or send email: " + e.getMessage());
        }
    }
} 