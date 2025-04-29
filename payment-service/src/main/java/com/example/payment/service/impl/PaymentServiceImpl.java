package com.example.payment.service.impl;

import com.example.payment.dto.response.PaymentResponse;
import com.example.payment.model.Booking;
import com.example.payment.service.PaymentService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @Override
    public PaymentResponse createPaymentUrL(Booking booking) throws StripeException{

        Stripe.apiKey = stripeSecretKey;

        SessionCreateParams params = SessionCreateParams.builder().addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:3000/payment/success?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl("http://localhost:3000/payment/fail")
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L).setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("usd")
                                .setUnitAmount((long) booking.getTotalPrice() * 100)
                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName("Movie Ticket")
                                        .build())
                                .build()
                        )
                        .build()
                )
                .build();

        Session session = Session.create(params);

        PaymentResponse response = PaymentResponse.builder()
                .payment_url(session.getUrl())
                .build();

        return null;
    }
}
