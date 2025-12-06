package com.studentconnect.gouni.platform.chat.domain.model.queries;

import java.util.UUID;

/**
 * Query to get unread messages for a specific user
 * @param userId the user ID
 */
public record GetUnreadMessagesByUserIdQuery(UUID userId) {
}
