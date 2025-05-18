package com.example.notification.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment_notifications")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String customerEmail;

    @Column(nullable = false)
    private String orderId;

    @Column(nullable = false)
    private String paymentId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDateTime paymentDate;

    private String paymentMethod;

    @Column(nullable = false)
    private LocalDateTime sentAt;
} 