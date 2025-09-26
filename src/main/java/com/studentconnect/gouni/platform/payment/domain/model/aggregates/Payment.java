package com.studentconnect.gouni.platform.payment.domain.model.aggregates;

import com.studentconnect.gouni.platform.iam.domain.model.aggregates.PassengerUser;
import com.studentconnect.gouni.platform.payment.domain.model.valueobjects.PaymentStatus;
import com.studentconnect.gouni.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Entity
@Getter
public class Payment extends AuditableAbstractAggregateRoot<Payment> {
    @ManyToOne
    @JoinColumn(name = "passenger_user_id")
    @NotNull
    private PassengerUser passengerUser;

    @NotNull
    private Long amount;

    @NotNull
    private String currency;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    protected Payment() {}

    public Payment(PassengerUser passengerUser, Long amount, String currency) {
        this.passengerUser = passengerUser;
        this.amount = amount;
        this.currency = currency;
        this.status = PaymentStatus.REQUIRES_CONFIRMATION;
    }

    public void confirmPayment() {
        if (this.status != PaymentStatus.REQUIRES_CONFIRMATION) {
            throw new IllegalStateException("Payment can only be confirmed from REQUIRES_CONFIRMATION status");
        }
        this.status = PaymentStatus.CONFIRMED;
    }
}
