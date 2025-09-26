package com.studentconnect.gouni.platform.payment.interfaces.rest.resources;

public record CreatePaymentIntentResource(
        Long amount,
        String currency
) {
}
