package com.studentconnect.gouni.platform.chat.interfaces.rest.transform;

import com.studentconnect.gouni.platform.chat.domain.model.aggregates.ChatRoom;
import com.studentconnect.gouni.platform.chat.interfaces.rest.resources.ChatRoomResource;

public class ChatRoomResourceFromEntityAssembler {
    
    public static ChatRoomResource toResourceFromEntity(ChatRoom entity) {
        return new ChatRoomResource(
            entity.getId(),
            entity.getRideId(),
            entity.getDriverUserId(),
            entity.getPassengerUserId(),
            entity.getIsActive(),
            entity.getRoomId(),
            entity.getCreatedAt() != null ? entity.getCreatedAt().toString() : null
        );
    }
}
