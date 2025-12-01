package com.studentconnect.gouni.platform.carpooling.interfaces.rest.resources;

import java.util.UUID;

public record RatingResource(
        UUID id,
        UUID rideId,
        UUID driverId,
        String driverName,
        UUID passengerId,
        String passengerName,
        Integer score,
        String comment,
        String createdAt
) {
}
