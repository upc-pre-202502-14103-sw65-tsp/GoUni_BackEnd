package com.studentconnect.gouni.platform.notifications.application.internal.commandservices;

import com.studentconnect.gouni.platform.notifications.application.internal.outboundservices.acl.ExternalIamService;
import com.studentconnect.gouni.platform.notifications.domain.model.aggregates.Notification;
import com.studentconnect.gouni.platform.notifications.domain.model.commands.CreateNotificationCommand;
import com.studentconnect.gouni.platform.notifications.domain.model.commands.DeleteNotificationCommand;
import com.studentconnect.gouni.platform.notifications.domain.services.NotificationCommandService;
import com.studentconnect.gouni.platform.notifications.infrastructure.persistence.jpa.repositories.NotificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class NotificationCommandServiceImpl implements NotificationCommandService {
    private final NotificationRepository repository;
    private final ExternalIamService externalIamService;

    @Override
    public Optional<Notification> handle(CreateNotificationCommand command) {
        var userResult = externalIamService.fetchUserById(command.userId());

        if (userResult.isEmpty()) {
            throw new IllegalArgumentException("User not found with ID: " + command.userId());
        }

        var newNotification = new Notification(
                userResult.get(),
                command.title(),
                command.message()
        );

        try {
            var savedNotification = repository.save(newNotification);
            return Optional.of(savedNotification);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error creating notification: " + e.getMessage(), e);
        }
    }

    @Override
    public void handle(DeleteNotificationCommand command) {
        try {
            var notificationResult = repository.findById(command.id());

            if (notificationResult.isEmpty()) {
                throw new IllegalArgumentException("Notification not found with ID: " + command.id());
            }

            repository.deleteById(command.id());
        } catch (Exception e) {
            throw new IllegalArgumentException("Error deleting notification: " + e.getMessage(), e);
        }
    }
}
