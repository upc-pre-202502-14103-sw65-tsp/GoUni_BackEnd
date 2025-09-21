package com.studentconnect.gouni.platform.notifications.interfaces.rest.transform;

import com.studentconnect.gouni.platform.notifications.domain.model.aggregates.Notification;
import com.studentconnect.gouni.platform.notifications.interfaces.rest.resources.NotificationResource;

public class NotificationResourceFromEntityAssembler {
    public static NotificationResource toResourceFromEntity(Notification entity) {
        return new NotificationResource(
                entity.getId(),
                entity.getUser().getId(),
                entity.getTitle(),
                entity.getMessage()
        );
    }
}
