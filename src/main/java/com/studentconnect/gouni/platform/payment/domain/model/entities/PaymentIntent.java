package com.studentconnect.gouni.platform.payment.domain.model.entities;

import lombok.Getter;

@Getter
public class PaymentIntent {
    private final String paymentIntentId;
    private final String clientSecret;
    private final String status;
    private final Long amount;
    private final String currency;

    public PaymentIntent(String paymentIntentId, String clientSecret, String status, Long amount, String currency) {
        this.paymentIntentId = paymentIntentId;
        this.clientSecret = clientSecret;
        this.status = status;
        this.amount = amount;
        this.currency = currency;
    }
}
