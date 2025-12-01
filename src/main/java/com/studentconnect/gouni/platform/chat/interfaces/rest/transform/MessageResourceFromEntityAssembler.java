package com.studentconnect.gouni.platform.chat.interfaces.rest.transform;

import com.studentconnect.gouni.platform.chat.domain.model.aggregates.Message;
import com.studentconnect.gouni.platform.chat.interfaces.rest.resources.MessageResource;

public class MessageResourceFromEntityAssembler {
    
    public static MessageResource toResourceFromEntity(Message entity) {
        return new MessageResource(
            entity.getId(),
            entity.getChatRoom().getId(),
            entity.getSenderId(),
            entity.getReceiverId(),
            entity.getContent(),
            entity.getMessageType().toString(),
            entity.getStatus().toString(),
            entity.getSentAt() != null ? entity.getSentAt().toString() : null,
            entity.getDeliveredAt() != null ? entity.getDeliveredAt().toString() : null,
            entity.getReadAt() != null ? entity.getReadAt().toString() : null
        );
    }
}
