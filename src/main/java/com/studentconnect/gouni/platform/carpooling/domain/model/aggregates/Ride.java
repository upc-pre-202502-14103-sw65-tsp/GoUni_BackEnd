package com.studentconnect.gouni.platform.carpooling.domain.model.aggregates;

import com.studentconnect.gouni.platform.carpooling.domain.model.valueobjects.RideStatus;
import com.studentconnect.gouni.platform.iam.domain.model.aggregates.DriverUser;
import com.studentconnect.gouni.platform.iam.domain.model.aggregates.PassengerUser;
import com.studentconnect.gouni.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Ride extends AuditableAbstractAggregateRoot<Ride> {
    @OneToOne
    @JoinColumn(name = "driver_user_id")
    private DriverUser driverUser;

    @OneToOne
    @JoinColumn(name = "passenger_user_id")
    private PassengerUser passengerUser;

    private String pickUpGeoLocation;

    private String dropOffGeoLocation;

    private RideStatus rideStatus;

    private Integer rating;

    public Ride() { }

    public Ride(
            DriverUser driverUser,
            PassengerUser passengerUser,
            String pickUpGeoLocation,
            String dropOffGeoLocation
    ) {
        this.driverUser = driverUser;
        this.passengerUser = passengerUser;
        this.pickUpGeoLocation = pickUpGeoLocation;
        this.dropOffGeoLocation = dropOffGeoLocation;
        this.rating = null;
        this.rideStatus = RideStatus.REQUESTED;
    }

    public void updateRideStatus(RideStatus newStatus) {
        this.rideStatus = newStatus;
    }

    public void updateRating(Integer rating) {
        this.rating = rating;
    }
}
