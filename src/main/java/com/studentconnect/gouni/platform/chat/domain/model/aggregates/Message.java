package com.studentconnect.gouni.platform.chat.domain.model.aggregates;

import com.studentconnect.gouni.platform.chat.domain.model.valueobjects.MessageStatus;
import com.studentconnect.gouni.platform.chat.domain.model.valueobjects.MessageType;
import com.studentconnect.gouni.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "messages")
public class Message extends AuditableAbstractAggregateRoot<Message> {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;
    
    @Column(nullable = false)
    private UUID senderId;
    
    @Column(nullable = false)
    private UUID receiverId;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageType messageType;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageStatus status;
    
    @Column(nullable = false)
    private LocalDateTime sentAt;
    
    private LocalDateTime deliveredAt;
    
    private LocalDateTime readAt;
    
    public Message() {
        this.messageType = MessageType.TEXT;
        this.status = MessageStatus.SENT;
        this.sentAt = LocalDateTime.now();
    }
    
    public Message(ChatRoom chatRoom, UUID senderId, UUID receiverId, String content) {
        this();
        this.chatRoom = chatRoom;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
    }
    
    public Message(ChatRoom chatRoom, UUID senderId, UUID receiverId, String content, MessageType messageType) {
        this(chatRoom, senderId, receiverId, content);
        this.messageType = messageType;
    }
    
    public void markAsDelivered() {
        this.status = MessageStatus.DELIVERED;
        this.deliveredAt = LocalDateTime.now();
    }
    
    public void markAsRead() {
        this.status = MessageStatus.READ;
        this.readAt = LocalDateTime.now();
    }
    
    public boolean isRead() {
        return this.status == MessageStatus.READ;
    }
}
