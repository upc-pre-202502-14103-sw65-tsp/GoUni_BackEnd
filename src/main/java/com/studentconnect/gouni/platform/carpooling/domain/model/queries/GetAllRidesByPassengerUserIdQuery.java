package com.studentconnect.gouni.platform.carpooling.domain.model.queries;

import java.util.UUID;

public record GetAllRidesByPassengerUserIdQuery(UUID passengerUserId) {
}
