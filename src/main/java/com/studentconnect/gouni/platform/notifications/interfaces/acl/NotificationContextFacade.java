package com.studentconnect.gouni.platform.notifications.interfaces.acl;

import com.studentconnect.gouni.platform.notifications.domain.model.aggregates.Notification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NotificationContextFacade {
    Optional<Notification> createNotification(UUID userId, String title, String message);

    List<Notification> fetchAllNotificationsByProfileId(UUID userId);

    void deleteNotification(UUID notificationId);
}
