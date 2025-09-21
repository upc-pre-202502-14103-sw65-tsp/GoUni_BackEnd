package com.studentconnect.gouni.platform.notifications.domain.model.commands;

import java.util.UUID;

public record CreateNotificationCommand(UUID userId, String title, String message) {
}
