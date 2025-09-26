package com.studentconnect.gouni.platform.payment.application.internal.queryservices;

import com.studentconnect.gouni.platform.payment.domain.model.aggregates.Payment;
import com.studentconnect.gouni.platform.payment.domain.model.queries.GetPaymentByIdQuery;
import com.studentconnect.gouni.platform.payment.domain.model.queries.GetPaymentsByPassengerIdQuery;
import com.studentconnect.gouni.platform.payment.domain.services.PaymentQueryService;
import com.studentconnect.gouni.platform.payment.infrastructure.persistence.jpa.repositories.PaymentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PaymentQueryServiceImpl implements PaymentQueryService {
    private final PaymentRepository paymentRepository;

    @Override
    public Optional<Payment> handle(GetPaymentByIdQuery query) {
        return paymentRepository.findById(query.paymentId());
    }

    @Override
    public List<Payment> handle(GetPaymentsByPassengerIdQuery query) {
        return paymentRepository.findAllByPassengerUser_Id(query.passengerId());
    }
}
