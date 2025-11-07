package com.studentconnect.gouni.platform.carpooling.interfaces.rest.transform;

import com.studentconnect.gouni.platform.carpooling.domain.model.aggregates.Ride;
import com.studentconnect.gouni.platform.carpooling.interfaces.rest.resources.RideResource;

public class RideResourceFromEntityAssembler {
    public static RideResource toResourceFromEntity(Ride entity) {
        return new RideResource(
                entity.getId(),
                entity.getDriverUser().getId(),
                entity.getPassengerUser().getId(),
                entity.getPickUpGeoLocation(),
                entity.getDropOffGeoLocation()
        );
    }
}
