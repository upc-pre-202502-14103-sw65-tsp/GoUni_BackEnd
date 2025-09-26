package com.studentconnect.gouni.platform.payment.infrastructure.stripe.services;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.param.PaymentIntentConfirmParams;
import com.stripe.param.PaymentIntentCreateParams;
import com.studentconnect.gouni.platform.payment.domain.model.entities.PaymentConfirmation;
import com.studentconnect.gouni.platform.payment.domain.model.entities.PaymentIntent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripePaymentService {

    @Value("${stripe.api.key}")
    private String stripeSecretKey;

    public PaymentIntent createPaymentIntent(Long amount, String currency) throws StripeException {
        Stripe.apiKey = stripeSecretKey;

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amount)
                .setCurrency(currency)
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
