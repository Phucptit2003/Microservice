package com.example.notification.repository;

import com.example.notification.model.PaymentNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentNotificationRepository extends JpaRepository<PaymentNotification, Long> {
} 