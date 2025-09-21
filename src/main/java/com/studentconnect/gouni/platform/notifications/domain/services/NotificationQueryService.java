package com.studentconnect.gouni.platform.notifications.domain.services;

import com.studentconnect.gouni.platform.notifications.domain.model.aggregates.Notification;
import com.studentconnect.gouni.platform.notifications.domain.model.queries.GetAllNotificationsByUserIdQuery;

import java.util.List;

public interface NotificationQueryService {
    List<Notification> handle(GetAllNotificationsByUserIdQuery query);
}
