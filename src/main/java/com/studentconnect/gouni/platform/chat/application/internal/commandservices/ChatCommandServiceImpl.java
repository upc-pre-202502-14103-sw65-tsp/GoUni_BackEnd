package com.studentconnect.gouni.platform.chat.application.internal.commandservices;

import com.studentconnect.gouni.platform.chat.domain.model.aggregates.ChatRoom;
import com.studentconnect.gouni.platform.chat.domain.model.aggregates.Message;
import com.studentconnect.gouni.platform.chat.domain.model.commands.CreateChatRoomCommand;
import com.studentconnect.gouni.platform.chat.domain.model.commands.MarkMessageAsReadCommand;
import com.studentconnect.gouni.platform.chat.domain.model.commands.SendMessageCommand;
import com.studentconnect.gouni.platform.chat.domain.services.ChatCommandService;
import com.studentconnect.gouni.platform.chat.infrastructure.persistence.jpa.repositories.ChatRoomRepository;
import com.studentconnect.gouni.platform.chat.infrastructure.persistence.jpa.repositories.MessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ChatCommandServiceImpl implements ChatCommandService {
    
    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;
    
    @Override
    @Transactional
    public Optional<ChatRoom> handle(CreateChatRoomCommand command) {
        var existingChatRoom = chatRoomRepository.findByRideId(command.rideId());
        if (existingChatRoom.isPresent()) {
            return existingChatRoom;
        }
        
        var chatRoom = new ChatRoom(
            command.rideId(),
            command.driverUserId(),
            command.passengerUserId()
        );
        
        var savedChatRoom = chatRoomRepository.save(chatRoom);
        return Optional.of(savedChatRoom);
    }
    
    @Override
    @Transactional
    public Optional<Message> handle(SendMessageCommand command) {
        var chatRoom = chatRoomRepository.findById(command.chatRoomId());
        if (chatRoom.isEmpty()) {
            throw new IllegalArgumentException("Chat room with ID " + command.chatRoomId() + " not found");
        }
        
        var chatRoomEntity = chatRoom.get();
        
        if (!chatRoomEntity.isParticipant(command.senderId())) {
            throw new IllegalArgumentException("Sender is not a participant in this chat room");
        }
        
        if (!chatRoomEntity.isParticipant(command.receiverId())) {
            throw new IllegalArgumentException("Receiver is not a participant in this chat room");
        }
        
        var message = new Message(
            chatRoomEntity,
            command.senderId(),
            command.receiverId(),
            command.content(),
            command.messageType()
        );
        
        var savedMessage = messageRepository.save(message);
        return Optional.of(savedMessage);
    }
    
    @Override
    @Transactional
    public Optional<Message> handle(MarkMessageAsReadCommand command) {
        var message = messageRepository.findById(command.messageId());
        if (message.isEmpty()) {
            return Optional.empty();
        }
        
        var messageEntity = message.get();
        
        if (!messageEntity.getReceiverId().equals(command.userId())) {
            throw new IllegalArgumentException("Only the receiver can mark a message as read");
        }
        
        messageEntity.markAsRead();
        var updatedMessage = messageRepository.save(messageEntity);
        return Optional.of(updatedMessage);
    }
    
    @Override
    @Transactional
    public void markAsRead(UUID messageId) {
        var message = messageRepository.findById(messageId);
        if (message.isPresent()) {
            var messageEntity = message.get();
            messageEntity.markAsRead();
            messageRepository.save(messageEntity);
        }
    }
}
