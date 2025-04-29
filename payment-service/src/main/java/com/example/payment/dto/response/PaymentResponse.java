package com.example.payment.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentResponse {

    private String payment_url;

}
