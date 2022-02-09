package com.maknaeontop.communication.websocket;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Message {
    public enum MessageType{
        ENTER, COMM
    }

    private MessageType messageType;
    private String roomId;
    private String sender;
    private String message;
}
