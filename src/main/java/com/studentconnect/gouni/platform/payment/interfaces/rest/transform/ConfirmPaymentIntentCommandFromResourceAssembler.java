package com.studentconnect.gouni.platform.payment.interfaces.rest.transform;

import com.studentconnect.gouni.platform.payment.domain.model.commands.ConfirmPaymentIntentCommand;
import com.studentconnect.gouni.platform.payment.interfaces.rest.resources.ConfirmPaymentIntentResource;

public class ConfirmPaymentIntentCommandFromResourceAssembler {
    public static ConfirmPaymentIntentCommand toCommandFromResource(ConfirmPaymentIntentResource resource) {
        return new ConfirmPaymentIntentCommand(
                resource.clientSecret(),
                resource.paymentMethodId()
        );
    }
}
