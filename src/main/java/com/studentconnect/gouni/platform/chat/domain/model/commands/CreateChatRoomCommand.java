package com.studentconnect.gouni.platform.chat.domain.model.commands;

import java.util.UUID;

/**
 * Command to create a new chat room for a ride
 * @param rideId the ride ID
 * @param driverUserId the driver user ID
 * @param passengerUserId the passenger user ID
 */
public record CreateChatRoomCommand(
        UUID rideId,
        UUID driverUserId,
        UUID passengerUserId
) {
}
