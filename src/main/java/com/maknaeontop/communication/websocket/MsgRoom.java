package com.maknaeontop.communication.websocket;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class MsgRoom {
    private String roomId;
    private Set<WebSocketSession> sessionSet = new HashSet<>();

    @Builder
    public MsgRoom(String roomId){
        this.roomId = roomId;
    }

    public void handleActions(WebSocketSession session, Message message, MsgService msgService){
        switch (message.getMessageType()){
            case FIRE:

                break;
            case ERROR:
                break;
        }/*
        if(message.getMessageType().equals(Message.MessageType.ENTER)){
            sessionSet.add(session);
            message.setMessage(message.getMessage() + "님이 입장했습니다.");
        }
        */
        sendMessage(message, msgService);
    }

    public <T> void sendMessage(T message, MsgService msgService){
        sessionSet.parallelStream().forEach(session -> msgService.sendMessage(session,message));
    }
}
