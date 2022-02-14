package com.maknaeontop.communication.websocket;

import com.maknaeontop.communication.websocket.MessageRepository;
import com.maknaeontop.dto.Message;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

public class WebSocketRoom {
    private final Set<WebSocketSession> webSocketSessionSet;

    public WebSocketRoom(){
        this.webSocketSessionSet = new HashSet<>();
    }

    public void httpCommunication(Message.MessageType type, MessageRepository messageRepository) {
        if(Message.MessageType.FIRE.equals(type)){
            webSocketSessionSet.parallelStream().forEach(session -> messageRepository.sendMessage(session, "fire!"));
        }
    }

    public void socketCommunication(WebSocketSession session, Message.MessageType type){
        if(Message.MessageType.ENTER.equals(type)){
            webSocketSessionSet.add(session);
        }

    }
}
