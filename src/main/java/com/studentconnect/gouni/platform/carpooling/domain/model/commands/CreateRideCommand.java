package com.studentconnect.gouni.platform.carpooling.domain.model.commands;

import com.studentconnect.gouni.platform.carpooling.domain.model.valueobjects.RideStatus;

import java.util.UUID;

public record CreateRideCommand(UUID driverUserId, UUID passengerUserId, String pickUpGeoLocation, String dropOffGeoLocation) {
}
