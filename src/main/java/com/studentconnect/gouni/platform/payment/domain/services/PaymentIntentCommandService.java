package com.studentconnect.gouni.platform.payment.domain.services;

import com.studentconnect.gouni.platform.payment.domain.model.commands.ConfirmPaymentIntentCommand;
import com.studentconnect.gouni.platform.payment.domain.model.commands.CreatePaymentIntentCommand;
import com.studentconnect.gouni.platform.payment.domain.model.entities.PaymentConfirmation;
import com.studentconnect.gouni.platform.payment.domain.model.entities.PaymentIntent;

public interface PaymentIntentCommandService {
    PaymentIntent handle(CreatePaymentIntentCommand command);
    PaymentConfirmation handle(ConfirmPaymentIntentCommand command);
}
