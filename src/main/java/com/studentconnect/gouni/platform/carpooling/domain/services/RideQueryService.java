package com.studentconnect.gouni.platform.carpooling.domain.services;

import com.studentconnect.gouni.platform.carpooling.domain.model.aggregates.Ride;
import com.studentconnect.gouni.platform.carpooling.domain.model.queries.GetAllRidesByDriverUserIdQuery;
import com.studentconnect.gouni.platform.carpooling.domain.model.queries.GetAllRidesByPassengerUserIdQuery;
import com.studentconnect.gouni.platform.carpooling.domain.model.queries.GetAllRidesQuery;
import com.studentconnect.gouni.platform.carpooling.domain.model.queries.GetRideByIdQuery;

import java.util.List;
import java.util.Optional;

public interface RideQueryService {
    Optional<Ride> handle(GetRideByIdQuery query);
    List<Ride> handle(GetAllRidesByPassengerUserIdQuery query);
    List<Ride> handle(GetAllRidesByDriverUserIdQuery query);
    List<Ride> handle(GetAllRidesQuery query);
}
