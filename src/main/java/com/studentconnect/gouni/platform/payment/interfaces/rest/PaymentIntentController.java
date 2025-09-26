package com.studentconnect.gouni.platform.payment.interfaces.rest;

import com.studentconnect.gouni.platform.payment.domain.services.PaymentIntentCommandService;
import com.studentconnect.gouni.platform.payment.interfaces.rest.resources.ConfirmPaymentIntentResource;
import com.studentconnect.gouni.platform.payment.interfaces.rest.resources.CreatePaymentIntentResource;
import com.studentconnect.gouni.platform.payment.interfaces.rest.resources.PaymentConfirmationResource;
import com.studentconnect.gouni.platform.payment.interfaces.rest.resources.PaymentIntentResource;
import com.studentconnect.gouni.platform.payment.interfaces.rest.transform.ConfirmPaymentIntentCommandFromResourceAssembler;
import com.studentconnect.gouni.platform.payment.interfaces.rest.transform.CreatePaymentIntentCommandFromResourceAssembler;
import com.studentconnect.gouni.platform.payment.interfaces.rest.transform.PaymentConfirmationResourceFromEntityAssembler;
import com.studentconnect.gouni.platform.payment.interfaces.rest.transform.PaymentIntentResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", methods = { RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE })
@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/v1/payment-intents", produces = "application/json")
@Tag(name = "Payment Intents", description = "Payment Intent Management Endpoints")
public class PaymentIntentController {
    private final PaymentIntentCommandService commandService;

    @PostMapping("/create-payment-intent")
    public ResponseEntity<PaymentIntentResource> createPaymentIntent(@RequestBody CreatePaymentIntentResource resource) {
        var createPaymentIntentCommand = CreatePaymentIntentCommandFromResourceAssembler.toCommandFromResource(resource);
        var paymentIntent = commandService.handle(createPaymentIntentCommand);
        var paymentIntentResource = PaymentIntentResourceFromEntityAssembler.toResourceFromEntity(paymentIntent);
        return new ResponseEntity<>(paymentIntentResource, HttpStatus.CREATED);
    }

    @PostMapping("/confirm-payment")
    public ResponseEntity<PaymentConfirmationResource> confirmPayment(@RequestBody ConfirmPaymentIntentResource resource) {
        var confirmPaymentIntentCommand = ConfirmPaymentIntentCommandFromResourceAssembler.toCommandFromResource(resource);
        var paymentConfirmation = commandService.handle(confirmPaymentIntentCommand);
        var paymentConfirmationResource = PaymentConfirmationResourceFromEntityAssembler.toResourceFromEntity(paymentConfirmation);

        HttpStatus status = paymentConfirmation.isSuccessful() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(paymentConfirmationResource, status);
    }
}
