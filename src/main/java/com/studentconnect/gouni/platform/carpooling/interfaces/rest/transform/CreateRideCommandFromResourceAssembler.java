package com.studentconnect.gouni.platform.carpooling.interfaces.rest.transform;

import com.studentconnect.gouni.platform.carpooling.domain.model.commands.CreateRideCommand;
import com.studentconnect.gouni.platform.carpooling.interfaces.rest.resources.CreateRideResource;

public class CreateRideCommandFromResourceAssembler {
    public static CreateRideCommand toCommandFromResource(CreateRideResource resource) {
        return new CreateRideCommand(
                resource.driverUserId(),
                resource.passengerUserId(),
                resource.pickUpGeoLocation(),
                resource.dropOffGeoLocation()
        );
    }
}
