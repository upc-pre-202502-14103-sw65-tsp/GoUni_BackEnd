package com.studentconnect.gouni.platform.chat.domain.model.valueobjects;

/**
 * Enum representing the type of message in the chat system.
 */
public enum MessageType {
    /**
     * Regular text message sent by users
     */
    TEXT,
    
    /**
     * Image shared in the chat
     */
    IMAGE,
    
    /**
     * Location/coordinates shared
     */
    LOCATION,
    
    /**
     * System-generated message (e.g., "Ride has started")
     */
    SYSTEM
}
