package com.studentconnect.gouni.platform.carpooling.interfaces.rest.transform;

import com.studentconnect.gouni.platform.carpooling.domain.model.commands.CreateRatingCommand;
import com.studentconnect.gouni.platform.carpooling.interfaces.rest.resources.CreateRatingResource;

public class CreateRatingCommandFromResourceAssembler {
    public static CreateRatingCommand toCommandFromResource(CreateRatingResource resource) {
        return new CreateRatingCommand(
                resource.rideId(),
                resource.score(),
                resource.comment()
        );
    }
}
