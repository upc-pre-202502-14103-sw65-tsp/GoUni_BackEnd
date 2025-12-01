package com.studentconnect.gouni.platform.carpooling.interfaces.rest.resources;

import java.util.UUID;

public record CreateRatingResource(
        UUID rideId,
        Integer score,
        String comment
) {
}
