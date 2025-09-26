package com.studentconnect.gouni.platform.payment.infrastructure.persistence.jpa.repositories;

import com.studentconnect.gouni.platform.payment.domain.model.aggregates.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    List<Payment> findAllByPassengerUser_Id(UUID passengerUserId);
}
