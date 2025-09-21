package com.studentconnect.gouni.platform.notifications.application.acl;

import com.studentconnect.gouni.platform.notifications.domain.model.aggregates.Notification;
import com.studentconnect.gouni.platform.notifications.domain.model.commands.CreateNotificationCommand;
import com.studentconnect.gouni.platform.notifications.domain.model.commands.DeleteNotificationCommand;
import com.studentconnect.gouni.platform.notifications.domain.model.queries.GetAllNotificationsByUserIdQuery;
import com.studentconnect.gouni.platform.notifications.domain.services.NotificationCommandService;
import com.studentconnect.gouni.platform.notifications.domain.services.NotificationQueryService;
import com.studentconnect.gouni.platform.notifications.interfaces.acl.NotificationContextFacade;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class NotificationContextFacadeImpl implements NotificationContextFacade {
    private final NotificationCommandService commandService;
    private final NotificationQueryService queryService;

    @Override
    public Optional<Notification> createNotification(UUID userId, String title, String message) {
        var notificationResult = commandService.handle(new CreateNotificationCommand(userId, title, message));

        if (notificationResult.isEmpty()) {
            throw new IllegalArgumentException("Failed to create notification for user ID: " + userId);
        }

        return notificationResult;
    }

    public List<Notification> fetchAllNotificationsByProfileId(UUID userId) {
        return queryService.handle(new GetAllNotificationsByUserIdQuery(userId));
    }

    @Override
    public void deleteNotification(UUID notificationId) {
        commandService.handle(new DeleteNotificationCommand(notificationId));
    }
}
