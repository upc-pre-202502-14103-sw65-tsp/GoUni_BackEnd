package com.studentconnect.gouni.platform.chat.domain.model.commands;

import com.studentconnect.gouni.platform.chat.domain.model.valueobjects.MessageType;

import java.util.UUID;

/**
 * Command to send a message in a chat room
 * @param chatRoomId the chat room ID
 * @param senderId the sender user ID
 * @param receiverId the receiver user ID
 * @param content the message content
 * @param messageType the type of message
 */
public record SendMessageCommand(
        UUID chatRoomId,
        UUID senderId,
        UUID receiverId,
        String content,
        MessageType messageType
) {
    /**
     * Constructor with default TEXT message type
     */
    public SendMessageCommand(UUID chatRoomId, UUID senderId, UUID receiverId, String content) {
        this(chatRoomId, senderId, receiverId, content, MessageType.TEXT);
    }
}
