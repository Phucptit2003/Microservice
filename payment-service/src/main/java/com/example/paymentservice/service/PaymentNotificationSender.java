package com.example.paymentservice.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentNotificationSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendPaymentNotification(String message) {
        rabbitTemplate.convertAndSend("payment-notification-queue", message);
    }
} 