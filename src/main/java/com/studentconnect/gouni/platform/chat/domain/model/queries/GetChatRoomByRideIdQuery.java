package com.studentconnect.gouni.platform.chat.domain.model.queries;

import java.util.UUID;

/**
 * Query to get a chat room by ride ID
 * @param rideId the ride ID
 */
public record GetChatRoomByRideIdQuery(UUID rideId) {
}
