package com.studentconnect.gouni.platform.payment.application.internal.commandservices;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.param.PaymentIntentCreateParams;
import com.studentconnect.gouni.platform.notifications.application.internal.outboundservices.acl.ExternalIamService;
import com.studentconnect.gouni.platform.payment.domain.model.aggregates.Payment;
import com.studentconnect.gouni.platform.payment.domain.model.commands.CreatePaymentCommand;
import com.studentconnect.gouni.platform.payment.domain.services.PaymentCommandService;
import com.studentconnect.gouni.platform.payment.infrastructure.persistence.jpa.repositories.PaymentRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentCommandServiceImpl implements PaymentCommandService {
    private final ExternalIamService externalIamService;
    private final PaymentRepository paymentRepository;

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    public PaymentCommandServiceImpl(ExternalIamService externalIamService, PaymentRepository paymentRepository) {
        this.externalIamService = externalIamService;
        this.paymentRepository = paymentRepository;
    }

    @PostConstruct
    public void initialize() {
        Stripe.apiKey = stripeApiKey;
    }

    @Override
    public Optional<Payment> handle(CreatePaymentCommand command) {
        var passengerUserResult = externalIamService.fetchPassengerUserById(command.passengerUserId());

        if (passengerUserResult.isEmpty()) {
            throw new RuntimeException("Passenger user not found");
        }

        var passengerUser = passengerUserResult.get();

        var newPayment = new Payment(
                passengerUser,
                command.amount(),
                command.currency()
        );

        Session session = null;

        try {
            PaymentIntentCreateParams intentParams = PaymentIntentCreateParams.builder()
                    .setAmount((long) (command.amount().doubleValue() * 100))
                    .setCurrency(command.currency())
                    .setCustomer(String.valueOf(command.passengerUserId()))
                    .setConfirmationMethod(PaymentIntentCreateParams.ConfirmationMethod.MANUAL)
                    .build();

            PaymentIntent paymentIntent = PaymentIntent.create(intentParams);
            paymentRepository.save(newPayment);
            return Optional.of(newPayment);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
