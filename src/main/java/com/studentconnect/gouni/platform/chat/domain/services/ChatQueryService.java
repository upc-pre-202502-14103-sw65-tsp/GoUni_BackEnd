package com.studentconnect.gouni.platform.chat.domain.services;

import com.studentconnect.gouni.platform.chat.domain.model.aggregates.ChatRoom;
import com.studentconnect.gouni.platform.chat.domain.model.aggregates.Message;
import com.studentconnect.gouni.platform.chat.domain.model.queries.GetChatRoomByRideIdQuery;
import com.studentconnect.gouni.platform.chat.domain.model.queries.GetMessagesByChatRoomIdQuery;
import com.studentconnect.gouni.platform.chat.domain.model.queries.GetUnreadMessagesByUserIdQuery;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChatQueryService {
    
    Optional<ChatRoom> handle(GetChatRoomByRideIdQuery query);
    
    List<Message> handle(GetMessagesByChatRoomIdQuery query);
    
    List<Message> handle(GetUnreadMessagesByUserIdQuery query);
    
    List<Message> getUnreadMessagesByUserId(UUID userId);
}
