package com.studentconnect.gouni.platform.payment.interfaces.rest.resources;

public record PaymentConfirmationResource(
        String paymentIntentId,
        Long amount,
        String currency,
        String status,
        String error,
        String message
) {
}
