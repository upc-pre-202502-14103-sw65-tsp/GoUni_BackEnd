package com.studentconnect.gouni.platform.chat.interfaces.rest.resources;

import java.util.UUID;

public record ChatRoomResource(
        UUID id,
        UUID rideId,
        UUID driverUserId,
        UUID passengerUserId,
        Boolean isActive,
        String roomId,
        String createdAt
) {
}
