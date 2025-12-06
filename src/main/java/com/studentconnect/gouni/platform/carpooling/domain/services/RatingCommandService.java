package com.studentconnect.gouni.platform.carpooling.domain.services;

import com.studentconnect.gouni.platform.carpooling.domain.model.aggregates.Rating;
import com.studentconnect.gouni.platform.carpooling.domain.model.commands.CreateRatingCommand;

import java.util.Optional;

public interface RatingCommandService {
    /**
     * Creates a new rating for a completed ride.
     * @param command the command containing rating details
     * @return the created rating, or empty if validation fails
     */
    Optional<Rating> handle(CreateRatingCommand command);
}
