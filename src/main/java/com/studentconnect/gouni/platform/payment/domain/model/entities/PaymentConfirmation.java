package com.studentconnect.gouni.platform.payment.domain.model.entities;

import lombok.Getter;

@Getter
public class PaymentConfirmation {
    private final String status;
    private final String paymentIntentId;
    private final Long amount;
    private final String currency;
    private final String error;
    private final String message;

    public PaymentConfirmation(String status, String paymentIntentId, Long amount, String currency, String error, String message) {
        this.status = status;
        this.paymentIntentId = paymentIntentId;
        this.amount = amount;
        this.currency = currency;
        this.error = error;
        this.message = message;
    }

    public boolean isSuccessful() {
        return "succeeded".equals(status);
    }
}
