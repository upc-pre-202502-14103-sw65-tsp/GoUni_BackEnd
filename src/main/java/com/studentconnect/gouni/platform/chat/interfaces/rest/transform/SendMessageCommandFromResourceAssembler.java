package com.studentconnect.gouni.platform.chat.interfaces.rest.transform;

import com.studentconnect.gouni.platform.chat.domain.model.commands.SendMessageCommand;
import com.studentconnect.gouni.platform.chat.domain.model.valueobjects.MessageType;
import com.studentconnect.gouni.platform.chat.interfaces.rest.resources.SendMessageResource;

import java.util.UUID;

public class SendMessageCommandFromResourceAssembler {
    
    public static SendMessageCommand toCommandFromResource(UUID chatRoomId, SendMessageResource resource) {
        MessageType messageType = MessageType.TEXT;
        
        if (resource.messageType() != null) {
            try {
                messageType = MessageType.valueOf(resource.messageType().toUpperCase());
            } catch (IllegalArgumentException e) {
                // Default to TEXT if invalid type
                messageType = MessageType.TEXT;
            }
        }
        
        return new SendMessageCommand(
            chatRoomId,
            resource.senderId(),
            resource.receiverId(),
            resource.content(),
            messageType
        );
    }
}
