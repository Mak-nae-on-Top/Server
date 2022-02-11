package com.maknaeontop.communication.websocket;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Message {
    public enum MessageType{
        ADD, FIRE, ERROR
    }

    private MessageType messageType;
    private String uuid;
}
