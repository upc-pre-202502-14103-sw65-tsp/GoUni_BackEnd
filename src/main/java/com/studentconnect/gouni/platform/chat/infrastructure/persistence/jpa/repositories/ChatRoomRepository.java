package com.studentconnect.gouni.platform.chat.infrastructure.persistence.jpa.repositories;

import com.studentconnect.gouni.platform.chat.domain.model.aggregates.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, UUID> {
    
    Optional<ChatRoom> findByRideId(UUID rideId);
    
    boolean existsByRideId(UUID rideId);
    
    java.util.List<ChatRoom> findByDriverUserIdOrPassengerUserId(UUID userId, UUID userId2);
}
