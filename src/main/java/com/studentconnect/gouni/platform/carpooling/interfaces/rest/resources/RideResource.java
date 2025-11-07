package com.studentconnect.gouni.platform.carpooling.interfaces.rest.resources;

import java.util.UUID;

public record RideResource(
        UUID id,
        UUID driverUserId,
        UUID passengerUserId,
        String pickUpGeoLocation,
        String dropOffGeoLocation
) {
}
