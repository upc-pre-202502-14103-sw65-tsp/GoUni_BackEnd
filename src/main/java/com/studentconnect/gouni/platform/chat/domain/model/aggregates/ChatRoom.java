package com.studentconnect.gouni.platform.chat.domain.model.aggregates;

import com.studentconnect.gouni.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
public class ChatRoom extends AuditableAbstractAggregateRoot<ChatRoom> {
    
    @Column(nullable = false, unique = true)
    private UUID rideId;
    
    @Column(nullable = false)
    private UUID driverUserId;
    
    @Column(nullable = false)
    private UUID passengerUserId;
    
    @Column(nullable = false)
    private Boolean isActive;
    
    public ChatRoom() {
        this.isActive = true;
    }
    
    public ChatRoom(UUID rideId, UUID driverUserId, UUID passengerUserId) {
        this.rideId = rideId;
        this.driverUserId = driverUserId;
        this.passengerUserId = passengerUserId;
        this.isActive = true;
    }
    
    public String getRoomId() {
        return "ride-" + rideId.toString();
    }
    
    public void deactivate() {
        this.isActive = false;
    }
    
    public boolean isParticipant(UUID userId) {
        return userId.equals(driverUserId) || userId.equals(passengerUserId);
    }
}
