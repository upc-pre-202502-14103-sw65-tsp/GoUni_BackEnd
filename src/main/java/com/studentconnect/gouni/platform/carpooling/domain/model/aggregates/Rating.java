package com.studentconnect.gouni.platform.carpooling.domain.model.aggregates;

import com.studentconnect.gouni.platform.iam.domain.model.aggregates.DriverUser;
import com.studentconnect.gouni.platform.iam.domain.model.aggregates.PassengerUser;
import com.studentconnect.gouni.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Rating extends AuditableAbstractAggregateRoot<Rating> {
    @ManyToOne
    @JoinColumn(name = "ride_id")
    @NotNull
    private Ride ride;

    @ManyToOne
    @JoinColumn(name = "rated_driver_id")
    @NotNull
    private DriverUser ratedDriver;

    @ManyToOne
    @JoinColumn(name = "rating_passenger_id")
    @NotNull
    private PassengerUser ratingPassenger;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer score;

    private String comment;

    public Rating() {}

    public Rating(Ride ride, DriverUser ratedDriver, PassengerUser ratingPassenger, Integer score, String comment) {
        this.ride = ride;
        this.ratedDriver = ratedDriver;
        this.ratingPassenger = ratingPassenger;
        this.score = score;
        this.comment = comment;
    }

    public Rating(Ride ride, DriverUser ratedDriver, PassengerUser ratingPassenger, Integer score) {
        this(ride, ratedDriver, ratingPassenger, score, null);
    }
}
