package com.studentconnect.gouni.platform.payment.infrastructure.stripe.configuration;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripeConfiguration {
    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @PostConstruct
    public void initialize() {
        com.stripe.Stripe.apiKey = stripeApiKey;
    }
}
