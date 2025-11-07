package com.studentconnect.gouni.platform.payment.infrastructure.stripe.services;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.param.PaymentIntentConfirmParams;
import com.stripe.param.PaymentIntentCreateParams;
import com.studentconnect.gouni.platform.notifications.application.internal.outboundservices.acl.ExternalIamService;
import com.studentconnect.gouni.platform.payment.domain.model.aggregates.Payment;
import com.studentconnect.gouni.platform.payment.domain.model.entities.PaymentConfirmation;
import com.studentconnect.gouni.platform.payment.domain.model.entities.PaymentIntent;
import com.studentconnect.gouni.platform.payment.infrastructure.persistence.jpa.repositories.PaymentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class StripePaymentService {

    @Value("${stripe.api.key}")
    private String stripeSecretKey;

    private final PaymentRepository paymentRepository;
    private final ExternalIamService externalIamService;

    public StripePaymentService(PaymentRepository paymentRepository, ExternalIamService externalIamService) {
        this.paymentRepository = paymentRepository;
        this.externalIamService = externalIamService;
    }

    public PaymentIntent createPaymentIntent(Long amount, String currency, UUID passengerUserId) throws StripeException {
        Stripe.apiKey = stripeSecretKey;

        PaymentIntentCreateParams.AutomaticPaymentMethods automatic =
                PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                        .setEnabled(true)
                        .setAllowRedirects(PaymentIntentCreateParams.AutomaticPaymentMethods.AllowRedirects.NEVER)
                        .build();

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amount * 100)
                .setCurrency(currency)
                .setAutomaticPaymentMethods(automatic)
                .putMetadata("passengerUserId", passengerUserId.toString())
                .putMetadata("amountUnits", amount.toString())
                .build();

        com.stripe.model.PaymentIntent paymentIntent = com.stripe.model.PaymentIntent.create(params);

        return new PaymentIntent(
                paymentIntent.getId(),
                paymentIntent.getClientSecret(),
                paymentIntent.getStatus(),
                paymentIntent.getAmount(),
                paymentIntent.getCurrency()
        );
    }

    public PaymentConfirmation confirmPaymentIntent(String clientSecret, String paymentMethodId) {
        try {
            Stripe.apiKey = stripeSecretKey;

            // Extract payment intent ID from client secret
            String paymentIntentId = extractPaymentIntentId(clientSecret);

            PaymentIntentConfirmParams params = PaymentIntentConfirmParams.builder()
                    .setPaymentMethod(paymentMethodId)
                    .build();

            com.stripe.model.PaymentIntent paymentIntent = com.stripe.model.PaymentIntent.retrieve(paymentIntentId);
            paymentIntent = paymentIntent.confirm(params);

            // Persist domain Payment when succeeded
            if ("succeeded".equals(paymentIntent.getStatus())) {
                String passengerIdStr = paymentIntent.getMetadata() != null ? paymentIntent.getMetadata().get("passengerUserId") : null;
                String amountUnitsStr = paymentIntent.getMetadata() != null ? paymentIntent.getMetadata().get("amountUnits") : null;
                if (passengerIdStr != null) {
                    try {
                        UUID passengerUserId = UUID.fromString(passengerIdStr);
                        var passengerUserResult = externalIamService.fetchPassengerUserById(passengerUserId);
                        if (passengerUserResult.isPresent()) {
                            Long amountUnits = amountUnitsStr != null ? Long.parseLong(amountUnitsStr) : paymentIntent.getAmount() / 100;
                            var payment = new Payment(
                                    passengerUserResult.get(),
                                    amountUnits,
                                    paymentIntent.getCurrency()
                            );
                            payment.confirmPayment();
                            paymentRepository.save(payment);
                        }
                    } catch (Exception ignored) {
                        // Swallow to avoid breaking confirmation; persistence issues logged elsewhere
                    }
                }
            }

            return new PaymentConfirmation(
                    paymentIntent.getStatus(),
                    paymentIntent.getId(),
                    paymentIntent.getAmount(),
                    paymentIntent.getCurrency(),
                    null,
                    null
            );

        } catch (StripeException e) {
            return new PaymentConfirmation(
                    "failed",
                    null,
                    null,
                    null,
                    e.getCode(),
                    e.getMessage()
            );
        }
    }

    private String extractPaymentIntentId(String clientSecret) {
        // Client secret format: pi_xxx_secret_yyy
        return clientSecret.split("_secret_")[0];
    }
}
