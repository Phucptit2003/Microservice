package com.example.notification.service;

import com.example.notification.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {
    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void receiveMessage(String message) {
        // Xử lý gửi email/thông báo ở đây
        System.out.println("Received payment notification: " + message);
    }
} 