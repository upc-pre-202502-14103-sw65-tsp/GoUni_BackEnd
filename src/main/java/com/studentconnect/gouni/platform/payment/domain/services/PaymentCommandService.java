package com.studentconnect.gouni.platform.payment.domain.services;

import com.studentconnect.gouni.platform.payment.domain.model.aggregates.Payment;
import com.studentconnect.gouni.platform.payment.domain.model.commands.CreatePaymentCommand;

import java.util.Optional;

public interface PaymentCommandService {
    Optional<Payment> handle(CreatePaymentCommand command);
}
