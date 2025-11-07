package com.studentconnect.gouni.platform.carpooling.domain.services;

import com.studentconnect.gouni.platform.carpooling.domain.model.aggregates.Ride;
import com.studentconnect.gouni.platform.carpooling.domain.model.commands.CreateRideCommand;

import java.util.Optional;

public interface RideCommandService {
    Optional<Ride> handle(CreateRideCommand command);
}
