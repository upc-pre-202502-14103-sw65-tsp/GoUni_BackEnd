package com.studentconnect.gouni.platform.payment.domain.model.commands;

import java.util.UUID;

public record CreatePaymentIntentCommand(
        Long amount,
        String currency,
        UUID passengerUserId
) {
}
