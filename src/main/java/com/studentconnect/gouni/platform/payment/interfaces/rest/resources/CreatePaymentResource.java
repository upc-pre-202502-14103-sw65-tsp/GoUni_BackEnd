package com.studentconnect.gouni.platform.payment.interfaces.rest.resources;

import java.util.UUID;

public record CreatePaymentResource(
        UUID passengerUserId,
        Long amount,
        String currency
) {
}
