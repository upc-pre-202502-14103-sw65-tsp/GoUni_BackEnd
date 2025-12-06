package com.studentconnect.gouni.platform.carpooling.application.internal.queryservices;

import com.studentconnect.gouni.platform.carpooling.domain.model.aggregates.Rating;
import com.studentconnect.gouni.platform.carpooling.domain.model.queries.*;
import com.studentconnect.gouni.platform.carpooling.domain.services.RatingQueryService;
import com.studentconnect.gouni.platform.carpooling.infrastructure.persistence.jpa.repositories.RatingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RatingQueryServiceImpl implements RatingQueryService {
    private final RatingRepository ratingRepository;

    @Override
    public Optional<Rating> handle(GetRatingByIdQuery query) {
        return ratingRepository.findById(query.ratingId());
    }

    @Override
    public List<Rating> handle(GetRatingsByDriverUserIdQuery query) {
        return ratingRepository.findByRatedDriverId(query.driverUserId());
    }

    @Override
    public List<Rating> handle(GetRatingsByPassengerUserIdQuery query) {
        return ratingRepository.findByRatingPassengerId(query.passengerUserId());
    }

    @Override
    public Optional<Rating> handle(GetRatingByRideIdQuery query) {
        return ratingRepository.findByRideId(query.rideId());
    }

    @Override
    public Double handle(GetAverageRatingByDriverUserIdQuery query) {
        var average = ratingRepository.findAverageRatingByDriverId(query.driverUserId());
        return average != null ? average : 0.0;
    }
}
