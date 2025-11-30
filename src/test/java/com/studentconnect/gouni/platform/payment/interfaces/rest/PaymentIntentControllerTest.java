package com.studentconnect.gouni.platform.payment.interfaces.rest;

import com.studentconnect.gouni.platform.payment.domain.model.entities.PaymentIntent;
import com.studentconnect.gouni.platform.payment.domain.model.entities.PaymentConfirmation;
import com.studentconnect.gouni.platform.payment.domain.model.commands.ConfirmPaymentIntentCommand;
import com.studentconnect.gouni.platform.payment.domain.model.commands.CreatePaymentIntentCommand;
import com.studentconnect.gouni.platform.payment.domain.services.PaymentIntentCommandService;
import com.studentconnect.gouni.platform.payment.interfaces.rest.resources.ConfirmPaymentIntentResource;
import com.studentconnect.gouni.platform.payment.interfaces.rest.resources.CreatePaymentIntentResource;
import com.studentconnect.gouni.platform.payment.interfaces.rest.resources.PaymentConfirmationResource;
import com.studentconnect.gouni.platform.payment.interfaces.rest.resources.PaymentIntentResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("PaymentIntentController Tests")
class PaymentIntentControllerTest {

    @Mock
    private PaymentIntentCommandService commandService;

    @InjectMocks
    private PaymentIntentController paymentIntentController;

    private CreatePaymentIntentResource validCreateResource;
    private ConfirmPaymentIntentResource validConfirmResource;
    private PaymentIntent mockPaymentIntent;
    private PaymentConfirmation mockPaymentConfirmation;
    private UUID testPassengerUserId;

    @BeforeEach
    void setUp() {
        testPassengerUserId = UUID.randomUUID();
        
        validCreateResource = new CreatePaymentIntentResource(
                5000L,
                "usd",
                testPassengerUserId
        );

        validConfirmResource = new ConfirmPaymentIntentResource(
                "pi_test_client_secret",
                "pm_test_payment_method"
        );

        mockPaymentIntent = new PaymentIntent(
                "pi_test_123",
                "pi_test_client_secret",
                "requires_confirmation",
                5000L,
                "usd"
        );

        mockPaymentConfirmation = new PaymentConfirmation(
                "succeeded",
                "pi_test_123",
                5000L,
                "usd",
                null,
                "Payment successful"
        );
    }

    @Test
    @DisplayName("Should create payment intent successfully")
    void shouldCreatePaymentIntentSuccessfully() {
        when(commandService.handle(any(CreatePaymentIntentCommand.class)))
                .thenReturn(mockPaymentIntent);

        ResponseEntity<PaymentIntentResource> response = 
                paymentIntentController.createPaymentIntent(validCreateResource);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());

        PaymentIntentResource resource = response.getBody();
        assertEquals("pi_test_client_secret", resource.clientSecret());
        assertEquals("requires_confirmation", resource.status());

        verify(commandService, times(1)).handle(any(CreatePaymentIntentCommand.class));
    }

    @Test
    @DisplayName("Should handle payment intent creation failure")
    void shouldHandlePaymentIntentCreationFailure() {
        when(commandService.handle(any(CreatePaymentIntentCommand.class)))
                .thenThrow(new RuntimeException("Stripe API error"));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> paymentIntentController.createPaymentIntent(validCreateResource)
        );

        assertEquals("Stripe API error", exception.getMessage());
        verify(commandService, times(1)).handle(any(CreatePaymentIntentCommand.class));
    }

    @Test
    @DisplayName("Should create payment intent with different amounts")
    void shouldCreatePaymentIntentWithDifferentAmounts() {
        CreatePaymentIntentResource highAmountResource = new CreatePaymentIntentResource(
                100000L,
                "usd",
                testPassengerUserId
        );

        when(commandService.handle(any(CreatePaymentIntentCommand.class)))
                .thenReturn(mockPaymentIntent);

        ResponseEntity<PaymentIntentResource> response = 
                paymentIntentController.createPaymentIntent(highAmountResource);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());

        verify(commandService, times(1)).handle(any(CreatePaymentIntentCommand.class));
    }

    @Test
    @DisplayName("Should create payment intent with different currencies")
    void shouldCreatePaymentIntentWithDifferentCurrencies() {
        CreatePaymentIntentResource eurResource = new CreatePaymentIntentResource(
                5000L,
                "eur",
                testPassengerUserId
        );

        when(commandService.handle(any(CreatePaymentIntentCommand.class)))
                .thenReturn(mockPaymentIntent);

        ResponseEntity<PaymentIntentResource> response = 
                paymentIntentController.createPaymentIntent(eurResource);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());

        verify(commandService, times(1)).handle(any(CreatePaymentIntentCommand.class));
    }

    @Test
    @DisplayName("Should confirm payment successfully")
    void shouldConfirmPaymentSuccessfully() {
        when(commandService.handle(any(ConfirmPaymentIntentCommand.class)))
                .thenReturn(mockPaymentConfirmation);

        ResponseEntity<PaymentConfirmationResource> response = 
                paymentIntentController.confirmPayment(validConfirmResource);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        PaymentConfirmationResource resource = response.getBody();
        assertEquals("pi_test_123", resource.paymentIntentId());
        assertEquals(5000L, resource.amount());
        assertEquals("usd", resource.currency());
        assertEquals("succeeded", resource.status());

        verify(commandService, times(1)).handle(any(ConfirmPaymentIntentCommand.class));
    }

    @Test
    @DisplayName("Should handle payment confirmation failure")
    void shouldHandlePaymentConfirmationFailure() {
        PaymentConfirmation failedConfirmation = new PaymentConfirmation(
                "failed",
                "pi_test_123",
                5000L,
                "usd",
                "card_declined",
                "Payment failed"
        );

        when(commandService.handle(any(ConfirmPaymentIntentCommand.class)))
                .thenReturn(failedConfirmation);

        ResponseEntity<PaymentConfirmationResource> response = 
                paymentIntentController.confirmPayment(validConfirmResource);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());

        PaymentConfirmationResource resource = response.getBody();
        assertEquals("pi_test_123", resource.paymentIntentId());
        assertEquals("failed", resource.status());
        assertEquals("card_declined", resource.error());

        verify(commandService, times(1)).handle(any(ConfirmPaymentIntentCommand.class));
    }

    @Test
    @DisplayName("Should handle payment confirmation with invalid client secret")
    void shouldHandlePaymentConfirmationWithInvalidClientSecret() {
        ConfirmPaymentIntentResource invalidResource = new ConfirmPaymentIntentResource(
                "invalid_client_secret",
                "pm_test_payment_method"
        );

        when(commandService.handle(any(ConfirmPaymentIntentCommand.class)))
                .thenThrow(new RuntimeException("Invalid client secret"));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> paymentIntentController.confirmPayment(invalidResource)
        );

        assertEquals("Invalid client secret", exception.getMessage());
        verify(commandService, times(1)).handle(any(ConfirmPaymentIntentCommand.class));
    }

    @Test
    @DisplayName("Should handle payment confirmation with missing payment method")
    void shouldHandlePaymentConfirmationWithMissingPaymentMethod() {
        ConfirmPaymentIntentResource missingPaymentMethodResource = new ConfirmPaymentIntentResource(
                "pi_test_client_secret",
                null
        );

        when(commandService.handle(any(ConfirmPaymentIntentCommand.class)))
                .thenThrow(new RuntimeException("Payment method is required"));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> paymentIntentController.confirmPayment(missingPaymentMethodResource)
        );

        assertEquals("Payment method is required", exception.getMessage());
        verify(commandService, times(1)).handle(any(ConfirmPaymentIntentCommand.class));
    }

    @Test
    @DisplayName("Should handle zero amount payment intent")
    void shouldHandleZeroAmountPaymentIntent() {
        CreatePaymentIntentResource zeroAmountResource = new CreatePaymentIntentResource(
                0L,
                "usd",
                testPassengerUserId
        );

        when(commandService.handle(any(CreatePaymentIntentCommand.class)))
                .thenThrow(new RuntimeException("Amount must be greater than zero"));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> paymentIntentController.createPaymentIntent(zeroAmountResource)
        );

        assertEquals("Amount must be greater than zero", exception.getMessage());
        verify(commandService, times(1)).handle(any(CreatePaymentIntentCommand.class));
    }
}
