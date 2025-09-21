package com.studentconnect.gouni.platform.notifications.interfaces.rest;

import com.studentconnect.gouni.platform.notifications.application.internal.outboundservices.acl.ExternalIamService;
import com.studentconnect.gouni.platform.notifications.domain.model.queries.GetAllNotificationsByUserIdQuery;
import com.studentconnect.gouni.platform.notifications.domain.services.NotificationCommandService;
import com.studentconnect.gouni.platform.notifications.domain.services.NotificationQueryService;
import com.studentconnect.gouni.platform.notifications.interfaces.rest.resources.NotificationResource;
import com.studentconnect.gouni.platform.notifications.interfaces.rest.transform.NotificationResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@CrossOrigin(origins = "*", methods = { RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE })
@RestController
@RequestMapping(value = "/api/v1/notifications", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Notifications", description = "Notifications Management Endpoint")
public class NotificationsController {
    private final NotificationCommandService commandService;
    private final NotificationQueryService queryService;
    private final ExternalIamService externalIamService;

    @GetMapping()
    public ResponseEntity<List<NotificationResource>> getAllNotificationsByProfileId(@AuthenticationPrincipal UserDetails userDetails) {
        var email = userDetails.getUsername();
        var userId = externalIamService.fetchUserIdByEmail(email);

        if (userId == null) {
            throw new IllegalArgumentException("Profile ID not found for the provided email");
        }

        var getAllNotificationsQuery = new GetAllNotificationsByUserIdQuery(userId);
        var notifications = queryService.handle(getAllNotificationsQuery);
        if (notifications.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        var notificationResources = notifications.stream()
                .map(NotificationResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(notificationResources);
    }
}
