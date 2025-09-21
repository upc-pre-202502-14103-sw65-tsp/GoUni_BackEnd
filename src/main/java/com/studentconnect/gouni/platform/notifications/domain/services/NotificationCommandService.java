package com.studentconnect.gouni.platform.notifications.domain.services;

import com.studentconnect.gouni.platform.notifications.domain.model.aggregates.Notification;
import com.studentconnect.gouni.platform.notifications.domain.model.commands.CreateNotificationCommand;
import com.studentconnect.gouni.platform.notifications.domain.model.commands.DeleteNotificationCommand;

import java.util.Optional;

public interface NotificationCommandService {
    Optional<Notification> handle(CreateNotificationCommand command);
    void handle(DeleteNotificationCommand command);
}
