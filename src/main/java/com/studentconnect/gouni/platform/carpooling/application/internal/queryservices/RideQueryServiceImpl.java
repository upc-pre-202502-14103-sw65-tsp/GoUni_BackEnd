package com.studentconnect.gouni.platform.carpooling.application.internal.queryservices;

import com.studentconnect.gouni.platform.carpooling.domain.model.aggregates.Ride;
import com.studentconnect.gouni.platform.carpooling.domain.model.queries.*;
import com.studentconnect.gouni.platform.carpooling.domain.services.RideQueryService;
import com.studentconnect.gouni.platform.carpooling.infrastructure.persistence.jpa.repositories.RideRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RideQueryServiceImpl implements RideQueryService {
    private final RideRepository rideRepository;

    @Override
    public Optional<Ride> handle(GetRideByIdQuery query) {
        return rideRepository.findById(query.rideId());
    }

    @Override
    public List<Ride> handle(GetAllRidesByPassengerUserIdQuery query) {
        return rideRepository.findAllByPassengerUser_Id(query.passengerUserId());
    }

    @Override
    public List<Ride> handle(GetAllRidesByDriverUserIdQuery query) {
        return rideRepository.findAllByDriverUser_Id(query.driveUserId());
    }

    @Override
    public List<Ride> handle(GetAllRidesQuery query) {
        return rideRepository.findAll();
    }

    @Override
    public List<Ride> handle(GetAllRidesByStatusQuery query) {
        return rideRepository.findAllByRideStatus(query.rideStatus());
    }

    @Override
    public List<Ride> handle(GetAllRidesByLocationQuery query) {
        return rideRepository.findAllByDropOffGeoLocationOrPickUpGeoLocation(query.location(), query.location());
    }
}
