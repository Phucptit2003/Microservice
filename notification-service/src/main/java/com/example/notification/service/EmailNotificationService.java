package com.example.notification.service;

import com.example.notification.dto.PaymentNotificationDto;
import com.example.notification.model.PaymentNotification;
import com.example.notification.repository.PaymentNotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailNotificationService {

    private final JavaMailSender mailSender;
    private final PaymentNotificationRepository paymentNotificationRepository;

    public void sendPaymentSuccessEmail(PaymentNotificationDto dto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(dto.getCustomerEmail());
        message.setSubject("Payment Success - Order #" + dto.getOrderId());
        message.setText(buildEmailBody(dto));
        mailSender.send(message);
        log.info("Payment success email sent to {}", dto.getCustomerEmail());

        // Save notification to database
        PaymentNotification notification = PaymentNotification.builder()
                .customerEmail(dto.getCustomerEmail())
                .orderId(dto.getOrderId())
                .paymentId(dto.getPaymentId())
                .amount(dto.getAmount())
                .currency(dto.getCurrency())
                .description(dto.getDescription())
                .paymentDate(dto.getPaymentDate())
                .paymentMethod(dto.getPaymentMethod())
                .sentAt(LocalDateTime.now())
                .build();
        paymentNotificationRepository.save(notification);
    }

    private String buildEmailBody(PaymentNotificationDto dto) {
        return String.format("""
                Dear Customer,

                Your payment was successful!

                Order ID: %s
                Payment ID: %s
                Amount: %s %s
                Description: %s
                Payment Date: %s
                Payment Method: %s

                Thank you for your purchase!
                """,
                dto.getOrderId(),
                dto.getPaymentId(),
                dto.getAmount(),
                dto.getCurrency(),
                dto.getDescription(),
                dto.getPaymentDate(),
                dto.getPaymentMethod() != null ? dto.getPaymentMethod() : "N/A"
        );
    }
} 