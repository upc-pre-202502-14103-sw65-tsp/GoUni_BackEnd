package com.studentconnect.gouni.platform.payment.interfaces.rest.transform;

import com.studentconnect.gouni.platform.payment.domain.model.commands.CreatePaymentIntentCommand;
import com.studentconnect.gouni.platform.payment.interfaces.rest.resources.CreatePaymentIntentResource;

public class CreatePaymentIntentCommandFromResourceAssembler {
    public static CreatePaymentIntentCommand toCommandFromResource(CreatePaymentIntentResource resource) {
        return new CreatePaymentIntentCommand(
                resource.amount(),
                resource.currency()
        );
    }
}
