package com.example.paymentservice.service.impl;

import com.example.paymentservice.dto.PaymentRequestDto;
import com.example.paymentservice.dto.PaymentResponseDto;
import com.example.paymentservice.exception.PaymentException;
import com.example.paymentservice.model.Payment;
import com.example.paymentservice.model.Payment.PaymentStatus;
import com.example.paymentservice.repository.PaymentRepository;
import com.example.paymentservice.service.PaymentService;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    
    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    @Override
    public PaymentResponseDto createCheckoutSession(PaymentRequestDto paymentRequestDto) {
        try {
            // Convert amount to cents (Stripe uses the smallest currency unit)
            long amountInCents = paymentRequestDto.getAmount().multiply(new BigDecimal(100)).longValue();
            
            // Build line items for the checkout session
            SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                    .setPriceData(
                            SessionCreateParams.LineItem.PriceData.builder()
                                    .setCurrency(paymentRequestDto.getCurrency())
                                    .setUnitAmount(amountInCents)
                                    .setProductData(
                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                    .setName("Order #" + paymentRequestDto.getOrderId())
                                                    .setDescription(paymentRequestDto.getDescription())
                                                    .build()
                                    )
                                    .build()
                    )
                    .setQuantity(1L)
                    .build();

            // Create metadata for the session
            Map<String, String> metadata = new HashMap<>();
            metadata.put("orderId", paymentRequestDto.getOrderId());
            metadata.put("userId", paymentRequestDto.getUserId());
            
            // Create the checkout session
            SessionCreateParams params = SessionCreateParams.builder()
                    .addLineItem(lineItem)
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(paymentRequestDto.getSuccessUrl())
                    .setCancelUrl(paymentRequestDto.getCancelUrl())
                    .setCustomerEmail(paymentRequestDto.getCustomerEmail())
                    .putAllMetadata(metadata)
                    .build();

            Session session = Session.create(params);
            
            // Save payment information to database
            Payment payment = Payment.builder()
                    .paymentId(session.getId())
                    .orderId(paymentRequestDto.getOrderId())
                    .userId(paymentRequestDto.getUserId())
                    .amount(paymentRequestDto.getAmount())
                    .currency(paymentRequestDto.getCurrency())
                    .description(paymentRequestDto.getDescription())
                    .status(PaymentStatus.PENDING)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            
            paymentRepository.save(payment);
            
            // Return response with checkout URL
            return PaymentResponseDto.builder()
                    .paymentId(payment.getPaymentId())
                    .orderId(payment.getOrderId())
                    .amount(payment.getAmount())
                    .currency(payment.getCurrency())
                    .status(payment.getStatus())
                    .checkoutUrl(session.getUrl())
                    .sessionId(session.getId())
                    .createdAt(payment.getCreatedAt())
                    .message("Payment session created successfully")
                    .build();
            
        } catch (StripeException e) {
            log.error("Error creating checkout session: {}", e.getMessage());
            throw new PaymentException("Failed to create payment session: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public String handleWebhookEvent(String payload, String sigHeader) {
        try {
            // Verify the webhook signature
            Event event = Webhook.constructEvent(payload, sigHeader, webhookSecret);

            // Handle the event
            if ("checkout.session.completed".equals(event.getType())) {
                Session session = (Session) event.getData().getObject();
                
                // Update payment status in database
                Payment payment = paymentRepository.findByPaymentId(session.getId())
                        .orElseThrow(() -> new PaymentException("Payment not found with ID: " + session.getId()));

                // Only update if payment is actually paid and session is complete
                if ("paid".equals(session.getPaymentStatus()) && "complete".equals(session.getStatus())) {
                    payment.setStatus(PaymentStatus.COMPLETED);
                    payment.setUpdatedAt(LocalDateTime.now());
                    
                    // Get payment method types if available
                    if (session.getPaymentMethodTypes() != null && !session.getPaymentMethodTypes().isEmpty()) {
                        payment.setPaymentMethod(session.getPaymentMethodTypes().get(0));
                    }
                    
                    paymentRepository.save(payment);
                    log.info("Payment completed for session: {}", session.getId());
                } else {
                    log.warn("Payment not marked as paid or session not complete for session: {}", session.getId());
                }
            }

            return "Webhook processed successfully";
        } catch (SignatureVerificationException e) {
            log.error("Invalid signature: {}", e.getMessage());
            throw new PaymentException("Invalid webhook signature: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Error processing webhook: {}", e.getMessage());
            throw new PaymentException("Failed to process webhook: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public PaymentResponseDto getPaymentBySessionId(String sessionId) {
        Payment payment = paymentRepository.findByPaymentId(sessionId)
                .orElseThrow(() -> new PaymentException("Payment not found with session ID: " + sessionId));
        
        return PaymentResponseDto.builder()
                .paymentId(payment.getPaymentId())
                .orderId(payment.getOrderId())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .status(payment.getStatus())
                .sessionId(sessionId)
                .createdAt(payment.getCreatedAt())
                .message("Payment retrieved successfully")
                .build();
    }

    @Override
    @Transactional
    public PaymentResponseDto updatePaymentStatus(String paymentId, PaymentStatus status) {
        log.info("Updating payment status for ID: {} to {}", paymentId, status);
        
        Payment payment = paymentRepository.findByPaymentId(paymentId)
                .orElseThrow(() -> new PaymentException("Payment not found with ID: " + paymentId));
        
        payment.setStatus(status);
        payment.setUpdatedAt(LocalDateTime.now());
        paymentRepository.save(payment);
        
        return PaymentResponseDto.builder()
                .paymentId(payment.getPaymentId())
                .orderId(payment.getOrderId())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .status(payment.getStatus())
                .sessionId(paymentId)
                .createdAt(payment.getCreatedAt())
                .message("Payment status updated successfully")
                .build();
    }
}
