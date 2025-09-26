package com.studentconnect.gouni.platform.payment.interfaces.rest.resources;

public record PaymentIntentResource(
        String clientSecret,
        String paymentIntentId,
        String status
) {
}
