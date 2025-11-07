package com.studentconnect.gouni.platform.carpooling.application.internal.commandservices;

import com.studentconnect.gouni.platform.carpooling.domain.model.aggregates.Ride;
import com.studentconnect.gouni.platform.carpooling.domain.model.commands.CreateRideCommand;
import com.studentconnect.gouni.platform.carpooling.domain.services.RideCommandService;
import com.studentconnect.gouni.platform.carpooling.infrastructure.persistence.jpa.repositories.RideRepository;
import com.studentconnect.gouni.platform.notifications.application.internal.outboundservices.acl.ExternalIamService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class RideCommandServiceImpl implements RideCommandService {
    private final RideRepository rideRepository;
    private final ExternalIamService externalIamService;

    @Override
    public Optional<Ride> handle(CreateRideCommand command) {
        var driverUserResult = externalIamService.fetchDriverUserById(command.driverUserId());
        var passengerUserResult = externalIamService.fetchPassengerUserById(command.passengerUserId());

        if (driverUserResult.isEmpty()) {
            throw new  RuntimeException("Driver user id not found");
        }

        if (passengerUserResult.isEmpty()) {
            throw new  RuntimeException("Passenger user id not found");
        }

        var newRide = new Ride(
                driverUserResult.get(),
                passengerUserResult.get(),
                command.pickUpGeoLocation(),
                command.dropOffGeoLocation()
        );

        try {
            var savedRide = rideRepository.save(newRide);
            return Optional.of(savedRide);
        } catch (Exception e){
            throw new IllegalArgumentException("Error creating ride: " + e.getMessage());
        }
    }
}
