package com.studentconnect.gouni.platform.chat.interfaces.rest;

import com.studentconnect.gouni.platform.chat.domain.model.commands.CreateChatRoomCommand;
import com.studentconnect.gouni.platform.chat.domain.model.queries.GetChatRoomByRideIdQuery;
import com.studentconnect.gouni.platform.chat.domain.model.queries.GetMessagesByChatRoomIdQuery;
import com.studentconnect.gouni.platform.chat.domain.model.queries.GetUnreadMessagesByUserIdQuery;
import com.studentconnect.gouni.platform.chat.domain.services.ChatCommandService;
import com.studentconnect.gouni.platform.chat.domain.services.ChatQueryService;
import com.studentconnect.gouni.platform.chat.interfaces.rest.resources.ChatRoomResource;
import com.studentconnect.gouni.platform.chat.interfaces.rest.resources.MessageResource;
import com.studentconnect.gouni.platform.chat.interfaces.rest.transform.ChatRoomResourceFromEntityAssembler;
import com.studentconnect.gouni.platform.chat.interfaces.rest.transform.MessageResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for chat operations (history, chat room management)
 */
@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*", methods = { RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE })
@RequestMapping(value = "/api/v1/chats", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Chat", description = "Chat Management Endpoints")
public class ChatController {
    
    private final ChatCommandService commandService;
    private final ChatQueryService queryService;
    
    @PostMapping("/rooms")
    public ResponseEntity<ChatRoomResource> createChatRoom(
            @RequestParam UUID rideId,
            @RequestParam UUID driverUserId,
            @RequestParam UUID passengerUserId
    ) {
        var command = new CreateChatRoomCommand(rideId, driverUserId, passengerUserId);
        var chatRoom = commandService.handle(command);
        
        if (chatRoom.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        var resource = ChatRoomResourceFromEntityAssembler.toResourceFromEntity(chatRoom.get());
        return new ResponseEntity<>(resource, HttpStatus.CREATED);
    }
    
    @GetMapping("/rooms/ride/{rideId}")
    public ResponseEntity<ChatRoomResource> getChatRoomByRideId(@PathVariable UUID rideId) {
        var query = new GetChatRoomByRideIdQuery(rideId);
        var chatRoom = queryService.handle(query);
        
        if (chatRoom.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        var resource = ChatRoomResourceFromEntityAssembler.toResourceFromEntity(chatRoom.get());
        return ResponseEntity.ok(resource);
    }
    
    @GetMapping("/rooms/{chatRoomId}/messages")
    public ResponseEntity<List<MessageResource>> getMessages(
            @PathVariable UUID chatRoomId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size
    ) {
        var query = new GetMessagesByChatRoomIdQuery(chatRoomId, page, size);
        var messages = queryService.handle(query);
        
        if (messages.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        
        var resources = messages.stream()
            .map(MessageResourceFromEntityAssembler::toResourceFromEntity)
            .toList();
        
        return ResponseEntity.ok(resources);
    }
    
    @GetMapping("/messages/unread")
    public ResponseEntity<List<MessageResource>> getUnreadMessages(@RequestParam UUID userId) {
        var query = new GetUnreadMessagesByUserIdQuery(userId);
        var messages = queryService.handle(query);
        
        var resources = messages.stream()
            .map(MessageResourceFromEntityAssembler::toResourceFromEntity)
            .toList();
        
        return ResponseEntity.ok(resources);
    }
    
    @GetMapping("/messages/unread/count")
    public ResponseEntity<UnreadCountResource> getUnreadCount(@RequestParam UUID userId) {
        var messages = queryService.getUnreadMessagesByUserId(userId);
        return ResponseEntity.ok(new UnreadCountResource(messages.size()));
    }
    
    public record UnreadCountResource(int count) {}
}
