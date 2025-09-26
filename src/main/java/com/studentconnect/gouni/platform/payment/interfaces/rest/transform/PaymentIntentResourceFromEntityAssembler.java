package com.studentconnect.gouni.platform.payment.interfaces.rest.transform;

import com.studentconnect.gouni.platform.payment.domain.model.entities.PaymentIntent;
import com.studentconnect.gouni.platform.payment.interfaces.rest.resources.PaymentIntentResource;

public class PaymentIntentResourceFromEntityAssembler {
    public static PaymentIntentResource toResourceFromEntity(PaymentIntent entity) {
        return new PaymentIntentResource(
                entity.getClientSecret(),
                entity.getPaymentIntentId(),
                entity.getStatus()
        );
    }
}
