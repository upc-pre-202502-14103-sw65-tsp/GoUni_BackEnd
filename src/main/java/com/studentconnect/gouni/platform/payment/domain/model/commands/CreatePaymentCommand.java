package com.studentconnect.gouni.platform.payment.domain.model.commands;

import java.util.UUID;

public record CreatePaymentCommand(
        UUID passengerUserId,
        Long amount,
        String currency
) {
}
