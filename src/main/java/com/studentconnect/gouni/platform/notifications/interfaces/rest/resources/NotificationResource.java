package com.studentconnect.gouni.platform.notifications.interfaces.rest.resources;

import java.util.UUID;

public record NotificationResource(
        UUID id,
        UUID userId,
        String title,
        String message
) {
}
