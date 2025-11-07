package com.studentconnect.gouni.platform.payment.interfaces.rest.resources;

import java.util.UUID;

public record CreatePaymentIntentResource(
        Long amount,
        String currency,
        UUID passengerUserId
) {
}
