package com.studentconnect.gouni.platform.iam.application.internal.outboundservices.acl;

import com.studentconnect.gouni.platform.notifications.interfaces.acl.NotificationContextFacade;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class ExternalNotificationService {
    private final NotificationContextFacade notificationContextFacade;

    public void createNotification(UUID userId, String title, String message) {
        notificationContextFacade.createNotification(userId, title, message);
    }
}
