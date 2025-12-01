package com.studentconnect.gouni.platform.carpooling.interfaces.rest.resources;

import java.util.List;
import java.util.UUID;

public record DriverRatingStatsResource(
        UUID driverId,
        String driverName,
        Double averageRating,
        Long totalRatings,
        List<RatingResource> recentRatings
) {
}
