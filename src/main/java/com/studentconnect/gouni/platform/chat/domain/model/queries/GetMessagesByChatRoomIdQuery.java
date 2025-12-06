package com.studentconnect.gouni.platform.chat.domain.model.queries;

import java.util.UUID;

/**
 * Query to get messages by chat room ID with pagination
 * @param chatRoomId the chat room ID
 * @param page the page number (0-indexed)
 * @param size the page size
 */
public record GetMessagesByChatRoomIdQuery(
        UUID chatRoomId,
        int page,
        int size
) {
    /**
     * Constructor with default pagination
     */
    public GetMessagesByChatRoomIdQuery(UUID chatRoomId) {
        this(chatRoomId, 0, 50);
    }
}
