package com.studentconnect.gouni.platform.payment.interfaces.rest.transform;

import com.studentconnect.gouni.platform.payment.domain.model.entities.PaymentConfirmation;
import com.studentconnect.gouni.platform.payment.interfaces.rest.resources.PaymentConfirmationResource;

public class PaymentConfirmationResourceFromEntityAssembler {
    public static PaymentConfirmationResource toResourceFromEntity(PaymentConfirmation entity) {
        return new PaymentConfirmationResource(
                entity.getPaymentIntentId(),
                entity.getAmount(),
                entity.getCurrency(),
                entity.getStatus(),
                entity.getError(),
                entity.getMessage()
        );
    }
}
