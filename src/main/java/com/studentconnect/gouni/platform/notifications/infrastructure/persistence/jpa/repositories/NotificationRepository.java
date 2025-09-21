package com.studentconnect.gouni.platform.notifications.infrastructure.persistence.jpa.repositories;

import com.studentconnect.gouni.platform.notifications.domain.model.aggregates.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    List<Notification> findAllByUser_Id(UUID userId);
}
