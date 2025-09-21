package com.studentconnect.gouni.platform.notifications.interfaces.rest.transform;

import com.studentconnect.gouni.platform.notifications.domain.model.commands.CreateNotificationCommand;
import com.studentconnect.gouni.platform.notifications.interfaces.rest.resources.CreateNotificationResource;

public class CreateNotificationCommandFromResourceAssembler {
    public static CreateNotificationCommand toCommandFromResource(CreateNotificationResource resource) {
        return new CreateNotificationCommand(
                resource.userId(),
                resource.title(),
                resource.message()
        );
    }
}
