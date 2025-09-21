package com.studentconnect.gouni.platform.iam.application.internal.eventhandlers;

import com.studentconnect.gouni.platform.iam.application.internal.outboundservices.acl.ExternalNotificationService;
import com.studentconnect.gouni.platform.iam.domain.model.events.UserCreationEvent;
import com.studentconnect.gouni.platform.iam.domain.model.queries.GetUserByIdQuery;
import com.studentconnect.gouni.platform.iam.domain.model.valueobjects.Roles;
import com.studentconnect.gouni.platform.iam.domain.services.UserQueryService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserCreationEventHandler {
    private final UserQueryService userQueryService;
    private final ExternalNotificationService externalNotificationService;
    private static final Logger logger = LoggerFactory.getLogger(UserCreationEventHandler.class);

    @EventListener(UserCreationEvent.class)
    public void on(UserCreationEvent event) {
        try {
            var user = userQueryService.handle(new GetUserByIdQuery(event.getUserId()));

            if (user.isEmpty()) {
                throw new RuntimeException("User not found with ID: " + event.getUserId());
            }

            var userEntity = user.get();

            if (userEntity.getRoles().stream().anyMatch(role -> role.getName() == Roles.PASSENGER_ROLE)) {
                externalNotificationService.createNotification(
                        userEntity.getId(),
                        "Welcome to GoUni!",
                        "Welcome aboard! You can now start using the platform."
                );
            } else if (userEntity.getRoles().stream().anyMatch(role -> role.getName() == Roles.DRIVER_ROLE)) {
                externalNotificationService.createNotification(
                        userEntity.getId(),
                        "Welcome Driver to GoUni!",
                        "Welcome aboard Driver! You can now start managing the platform."
                );
            }
        } catch (Exception e) {
            logger.error("Error handling user creation event for user ID {}: {}", event.getUserId(), e.getMessage(), e);
        }
    }
}
