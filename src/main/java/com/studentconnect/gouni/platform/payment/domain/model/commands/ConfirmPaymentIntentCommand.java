package com.studentconnect.gouni.platform.payment.domain.model.commands;

public record ConfirmPaymentIntentCommand(
        String clientSecret,
        String paymentMethodId
) {}

