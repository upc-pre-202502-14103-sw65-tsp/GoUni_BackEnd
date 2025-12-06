package com.studentconnect.gouni.platform.chat.application.internal.queryservices;

import com.studentconnect.gouni.platform.chat.domain.model.aggregates.ChatRoom;
import com.studentconnect.gouni.platform.chat.domain.model.aggregates.Message;
import com.studentconnect.gouni.platform.chat.domain.model.queries.GetChatRoomByRideIdQuery;
import com.studentconnect.gouni.platform.chat.domain.model.queries.GetMessagesByChatRoomIdQuery;
import com.studentconnect.gouni.platform.chat.domain.model.queries.GetUnreadMessagesByUserIdQuery;
import com.studentconnect.gouni.platform.chat.domain.model.valueobjects.MessageStatus;
import com.studentconnect.gouni.platform.chat.domain.services.ChatQueryService;
import com.studentconnect.gouni.platform.chat.infrastructure.persistence.jpa.repositories.ChatRoomRepository;
import com.studentconnect.gouni.platform.chat.infrastructure.persistence.jpa.repositories.MessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ChatQueryServiceImpl implements ChatQueryService {
    
    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;
    
    @Override
    public Optional<ChatRoom> handle(GetChatRoomByRideIdQuery query) {
        return chatRoomRepository.findByRideId(query.rideId());
    }
    
    @Override
    public List<Message> handle(GetMessagesByChatRoomIdQuery query) {
        var pageable = PageRequest.of(
            query.page(),
            query.size(),
            Sort.by(Sort.Direction.ASC, "sentAt")
        );
        
        var page = messageRepository.findByChatRoomIdOrderBySentAtAsc(
            query.chatRoomId(),
            pageable
        );
        
        return page.getContent();
    }
    
    @Override
    public List<Message> handle(GetUnreadMessagesByUserIdQuery query) {
        return messageRepository.findByReceiverIdAndStatusNot(
            query.userId(),
            MessageStatus.READ
        );
    }
    
    @Override
    public List<Message> getUnreadMessagesByUserId(UUID userId) {
        return messageRepository.findByReceiverIdAndStatusNot(
            userId,
            MessageStatus.READ
        );
    }
}
