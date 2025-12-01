package com.studentconnect.gouni.platform.carpooling.domain.services;

import com.studentconnect.gouni.platform.carpooling.domain.model.aggregates.Rating;
import com.studentconnect.gouni.platform.carpooling.domain.model.queries.*;

import java.util.List;
import java.util.Optional;

public interface RatingQueryService {
    /**
     * Retrieves a rating by its ID.
     * @param query the query containing the rating ID
     * @return the rating if found, empty otherwise
     */
    Optional<Rating> handle(GetRatingByIdQuery query);

    /**
     * Retrieves all ratings for a specific driver.
     * @param query the query containing the driver user ID
     * @return list of ratings for the driver
     */
    List<Rating> handle(GetRatingsByDriverUserIdQuery query);

    /**
     * Retrieves all ratings given by a specific passenger.
     * @param query the query containing the passenger user ID
     * @return list of ratings by the passenger
     */
    List<Rating> handle(GetRatingsByPassengerUserIdQuery query);

    /**
     * Retrieves the rating for a specific ride.
     * @param query the query containing the ride ID
     * @return the rating if found, empty otherwise
     */
    Optional<Rating> handle(GetRatingByRideIdQuery query);

    /**
     * Calculates the average rating for a specific driver.
     * @param query the query containing the driver user ID
     * @return the average rating, or 0.0 if no ratings exist
     */
    Double handle(GetAverageRatingByDriverUserIdQuery query);
}
