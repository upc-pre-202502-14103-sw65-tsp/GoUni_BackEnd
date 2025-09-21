package com.studentconnect.gouni.platform.notifications.application.internal.queryservices;

import com.studentconnect.gouni.platform.notifications.domain.model.aggregates.Notification;
import com.studentconnect.gouni.platform.notifications.domain.model.queries.GetAllNotificationsByUserIdQuery;
import com.studentconnect.gouni.platform.notifications.domain.services.NotificationQueryService;
import com.studentconnect.gouni.platform.notifications.infrastructure.persistence.jpa.repositories.NotificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class NotificationQueryServiceImpl implements NotificationQueryService {
    private final NotificationRepository repository;

    @Override
    public List<Notification> handle(GetAllNotificationsByUserIdQuery query) {
        return repository.findAllByUser_Id(query.userId());
    }
}
