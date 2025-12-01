package com.studentconnect.gouni.platform.chat.interfaces.rest.resources;

import java.util.UUID;

public record SendMessageResource(
        UUID senderId,
        UUID receiverId,
        String content,
        String messageType
) {
}
