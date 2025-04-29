package com.example.payment.service;

import com.example.payment.dto.response.PaymentResponse;
import com.example.payment.model.Booking;
import com.stripe.exception.StripeException;

public interface PaymentService {

    PaymentResponse createPaymentUrL(Booking booking) throws StripeException;

//    @Value("${stripe.api.key}")
//    private String stripeApiKey;
//
//    public String createPaymentSession(String bookingId, double amount) throws StripeException {
//        Stripe.apiKey = stripeApiKey;
//
//        SessionCreateParams params = SessionCreateParams.builder()
//                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
//                .setMode(SessionCreateParams.Mode.PAYMENT)
//                .setSuccessUrl("http://localhost:3000/success?session_id={CHECKOUT_SESSION_ID}")
//                .setCancelUrl("http://localhost:3000/cancel")
//                .addLineItem(
//                        SessionCreateParams.LineItem.builder()
//                                .setPriceData(
//                                        SessionCreateParams.LineItem.PriceData.builder()
//                                                .setCurrency("usd")
//                                                .setUnitAmount((long) (amount * 100)) // Amount in cents
//                                                .setProductData(
//                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
//                                                                .setName("Movie Ticket")
//                                                                .build()
//                                                )
//                                                .build()
//                                )
//                                .setQuantity(1L)
//                                .build()
//                )
//                .putMetadata("bookingId", bookingId)
//                .build();
//
//        Session session = Session.create(params);
//        return session.getId();
//    }
//
//    public String getPaymentStatus(String sessionId) throws StripeException {
//        Stripe.apiKey = stripeApiKey;
//        Session session = Session.retrieve(sessionId);
//        return session.getStatus();
//    }



}