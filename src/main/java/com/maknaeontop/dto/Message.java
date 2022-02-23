package com.maknaeontop.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Message is a class used for communication between app and web socket,
 * and has type and uuid.
 */
@Getter
@Setter
public class Message {
    public enum MessageType{
        ENTER, FIRE, EXIT
    }

    private String uuid;
    private MessageType type;

}
