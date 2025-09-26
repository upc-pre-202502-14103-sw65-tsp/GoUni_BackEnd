package com.studentconnect.gouni.platform.payment.application.internal.commandservices;

import com.studentconnect.gouni.platform.payment.domain.model.commands.ConfirmPaymentIntentCommand;
import com.studentconnect.gouni.platform.payment.domain.model.commands.CreatePaymentIntentCommand;
import com.studentconnect.gouni.platform.payment.domain.model.entities.PaymentConfirmation;
import com.studentconnect.gouni.platform.payment.domain.model.entities.PaymentIntent;
import com.studentconnect.gouni.platform.payment.domain.services.PaymentIntentCommandService;
import com.studentconnect.gouni.platform.payment.infrastructure.stripe.services.StripePaymentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PaymentIntentCommandServiceImpl implements PaymentIntentCommandService {
    private final StripePaymentService stripePaymentService;

    @Override
    public PaymentIntent handle(CreatePaymentIntentCommand command) {
        try {
            return stripePaymentService.createPaymentIntent(command.amount(), command.currency());
        } catch (Exception e) {
            throw new RuntimeException("Failed to create payment intent: " + e.getMessage(), e);
        }
    }

    @Override
    public PaymentConfirmation handle(ConfirmPaymentIntentCommand command) {
        try {
            return stripePaymentService.confirmPaymentIntent(command.clientSecret(), command.paymentMethodId());
        } catch (Exception e) {
            throw new RuntimeException("Failed to confirm payment intent: " + e.getMessage(), e);
        }
    }
}
