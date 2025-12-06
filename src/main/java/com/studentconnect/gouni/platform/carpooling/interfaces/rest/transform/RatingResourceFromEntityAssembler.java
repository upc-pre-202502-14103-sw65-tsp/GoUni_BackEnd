package com.studentconnect.gouni.platform.carpooling.interfaces.rest.transform;

import com.studentconnect.gouni.platform.carpooling.domain.model.aggregates.Rating;
import com.studentconnect.gouni.platform.carpooling.interfaces.rest.resources.RatingResource;

public class RatingResourceFromEntityAssembler {
    public static RatingResource toResourceFromEntity(Rating entity) {
        return new RatingResource(
                entity.getId(),
                entity.getRide().getId(),
                entity.getRatedDriver().getId(),
                entity.getRatedDriver().getFirstName() + " " + entity.getRatedDriver().getLastName(),
                entity.getRatingPassenger().getId(),
                entity.getRatingPassenger().getFirstName() + " " + entity.getRatingPassenger().getLastName(),
                entity.getScore(),
                entity.getComment(),
                entity.getCreatedAt().toString()
        );
    }
}
