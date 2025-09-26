package com.studentconnect.gouni.platform.payment.domain.model.commands;

public record CreatePaymentIntentCommand(
        Long amount,
        String currency
) {
}
