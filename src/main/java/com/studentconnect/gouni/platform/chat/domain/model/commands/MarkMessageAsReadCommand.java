package com.studentconnect.gouni.platform.chat.domain.model.commands;

import java.util.UUID;

/**
 * Command to mark a message as read
 * @param messageId the message ID to mark as read
 * @param userId the user ID marking the message as read
 */
public record MarkMessageAsReadCommand(
        UUID messageId,
        UUID userId
) {
}
