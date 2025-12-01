package com.studentconnect.gouni.platform.chat.domain.services;

import com.studentconnect.gouni.platform.chat.domain.model.aggregates.ChatRoom;
import com.studentconnect.gouni.platform.chat.domain.model.aggregates.Message;
import com.studentconnect.gouni.platform.chat.domain.model.commands.CreateChatRoomCommand;
import com.studentconnect.gouni.platform.chat.domain.model.commands.MarkMessageAsReadCommand;
import com.studentconnect.gouni.platform.chat.domain.model.commands.SendMessageCommand;

import java.util.Optional;
import java.util.UUID;

public interface ChatCommandService {
    
    Optional<ChatRoom> handle(CreateChatRoomCommand command);
    
    Optional<Message> handle(SendMessageCommand command);
    
    Optional<Message> handle(MarkMessageAsReadCommand command);
    
    void markAsRead(UUID messageId);
}
