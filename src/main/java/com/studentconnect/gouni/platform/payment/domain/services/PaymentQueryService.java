package com.studentconnect.gouni.platform.payment.domain.services;

import com.studentconnect.gouni.platform.payment.domain.model.aggregates.Payment;
import com.studentconnect.gouni.platform.payment.domain.model.queries.GetPaymentByIdQuery;
import com.studentconnect.gouni.platform.payment.domain.model.queries.GetPaymentsByPassengerIdQuery;

import java.util.List;
import java.util.Optional;

public interface PaymentQueryService {
    Optional<Payment> handle(GetPaymentByIdQuery query);
    List<Payment> handle(GetPaymentsByPassengerIdQuery query);
}
