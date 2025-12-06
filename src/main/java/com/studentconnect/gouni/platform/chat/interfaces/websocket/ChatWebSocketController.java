package com.studentconnect.gouni.platform.chat.interfaces.websocket;

import com.studentconnect.gouni.platform.chat.domain.services.ChatCommandService;
import com.studentconnect.gouni.platform.chat.interfaces.rest.resources.SendMessageResource;
import com.studentconnect.gouni.platform.chat.interfaces.rest.transform.MessageResourceFromEntityAssembler;
import com.studentconnect.gouni.platform.chat.interfaces.rest.transform.SendMessageCommandFromResourceAssembler;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.UUID;

/**
 * WebSocket controller for real-time chat functionality
 */
@Controller
@AllArgsConstructor
public class ChatWebSocketController {
    
    private final ChatCommandService chatCommandService;
    private final SimpMessagingTemplate messagingTemplate;
    private final com.studentconnect.gouni.platform.chat.infrastructure.persistence.jpa.repositories.ChatRoomRepository chatRoomRepository;
    
    @MessageMapping("/chat/{rideId}/send")
    public void sendMessage(
            @DestinationVariable String rideId,
            @Payload SendMessageResource resource
    ) {
        try {
            UUID rideUuid = UUID.fromString(rideId);
            var chatRoomOptional = chatRoomRepository.findByRideId(rideUuid);
            
            if (chatRoomOptional.isEmpty()) {
                messagingTemplate.convertAndSend(
                    "/topic/chat/" + rideId + "/error",
                    "Chat room not found for ride: " + rideId
                );
                return;
            }
            
            UUID chatRoomId = chatRoomOptional.get().getId();
            
            var command = SendMessageCommandFromResourceAssembler.toCommandFromResource(
                chatRoomId,
                resource
            );
            
            var message = chatCommandService.handle(command);
            
            if (message.isPresent()) {
                var messageResource = MessageResourceFromEntityAssembler
                    .toResourceFromEntity(message.get());
                
                messagingTemplate.convertAndSend(
                    "/topic/chat/" + rideId,
                    messageResource
                );
                
                messagingTemplate.convertAndSendToUser(
                    message.get().getReceiverId().toString(),
                    "/queue/notifications",
                    messageResource
                );
            }
        } catch (Exception e) {
            messagingTemplate.convertAndSend(
                "/topic/chat/" + rideId + "/error",
                "Error sending message: " + e.getMessage()
            );
        }
    }
    
    @MessageMapping("/chat/{rideId}/read")
    public void markAsRead(
            @DestinationVariable String rideId,
            @Payload String messageId
    ) {
        try {
            UUID msgId = UUID.fromString(messageId);
            chatCommandService.markAsRead(msgId);
            
            messagingTemplate.convertAndSend(
                "/topic/chat/" + rideId + "/status",
                new MessageStatusUpdate(msgId, "READ")
            );
        } catch (Exception e) {
            messagingTemplate.convertAndSend(
                "/topic/chat/" + rideId + "/error",
                "Error marking message as read: " + e.getMessage()
            );
        }
    }
    
    @MessageMapping("/chat/{rideId}/typing")
    public void userTyping(
            @DestinationVariable String rideId,
            @Payload TypingIndicator indicator
    ) {
        messagingTemplate.convertAndSend(
            "/topic/chat/" + rideId + "/typing",
            indicator
        );
    }
    
    /**
     * Simple record for message status updates
     */
    public record MessageStatusUpdate(UUID messageId, String status) {}
    
    /**
     * Simple record for typing indicators
     */
    public record TypingIndicator(UUID userId, boolean isTyping) {}
}
