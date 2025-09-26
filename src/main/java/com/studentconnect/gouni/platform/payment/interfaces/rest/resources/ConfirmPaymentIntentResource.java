package com.studentconnect.gouni.platform.payment.interfaces.rest.resources;

public record ConfirmPaymentIntentResource(
        String clientSecret,
        String paymentMethodId
) {
}
