package com.studentconnect.gouni.platform.chat.infrastructure.persistence.jpa.repositories;

import com.studentconnect.gouni.platform.chat.domain.model.aggregates.Message;
import com.studentconnect.gouni.platform.chat.domain.model.valueobjects.MessageStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {
    
    Page<Message> findByChatRoomIdOrderBySentAtAsc(UUID chatRoomId, Pageable pageable);
    
    List<Message> findByChatRoomIdOrderBySentAtAsc(UUID chatRoomId);
    
    List<Message> findByReceiverIdAndStatusNot(UUID receiverId, MessageStatus status);
    
    long countByReceiverIdAndStatusNot(UUID receiverId, MessageStatus status);
    
    List<Message> findBySenderIdOrderBySentAtDesc(UUID senderId);
    
    @Query("SELECT m FROM Message m WHERE m.chatRoom.id = :chatRoomId ORDER BY m.sentAt DESC")
    Page<Message> findRecentMessagesByChatRoom(@Param("chatRoomId") UUID chatRoomId, Pageable pageable);
}
