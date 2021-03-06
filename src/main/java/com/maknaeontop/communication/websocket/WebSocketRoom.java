package com.maknaeontop.communication.websocket;


import com.maknaeontop.dto.Message;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import java.util.HashSet;
import java.util.Set;

/**
 * WebSocketRoom is the class that implements the action to be performed according to the type of message.
 */
public class WebSocketRoom {
    private final Set<WebSocketSession> webSocketSessionSet = new HashSet<>();

    public void httpCommunication(Message.MessageType type, MessageRepository messageRepository) {
        if(Message.MessageType.FIRE.equals(type)){
            webSocketSessionSet.parallelStream().forEach(session -> sendMessage(session, "fire!"));
        }
    }

    public void socketCommunication(WebSocketSession session, Message.MessageType type){
        if(Message.MessageType.ENTER.equals(type)){
            webSocketSessionSet.add(session);
        }
        else if(Message.MessageType.EXIT.equals(type)){
            webSocketSessionSet.remove(session);
        }

    }

    private <T> void sendMessage(WebSocketSession session, String message) {
        try{
            if(!session.isOpen()){
                webSocketSessionSet.remove(session);
                return;
            }
            session.sendMessage(new TextMessage(message));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
