package com.studentconnect.gouni.platform.carpooling.domain.model.queries;

import com.studentconnect.gouni.platform.carpooling.domain.model.valueobjects.RideStatus;

public record GetAllRidesByStatusQuery(RideStatus rideStatus) {
}
