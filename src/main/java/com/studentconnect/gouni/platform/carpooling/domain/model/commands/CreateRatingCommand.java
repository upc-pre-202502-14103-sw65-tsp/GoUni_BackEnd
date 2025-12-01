package com.studentconnect.gouni.platform.carpooling.domain.model.commands;

import java.util.UUID;

public record CreateRatingCommand(
        UUID rideId,
        Integer score,
        String comment
) {
    public CreateRatingCommand {
        if (score == null || score < 1 || score > 5) {
            throw new IllegalArgumentException("Score must be between 1 and 5");
        }
    }
}
