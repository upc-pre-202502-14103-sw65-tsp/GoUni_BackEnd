package com.studentconnect.gouni.platform.chat.interfaces.rest.resources;

import java.util.UUID;

public record MessageResource(
        UUID id,
        UUID chatRoomId,
        UUID senderId,
        UUID receiverId,
        String content,
        String messageType,
        String status,
        String sentAt,
        String deliveredAt,
        String readAt
) {
}
