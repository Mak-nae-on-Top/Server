package com.maknaeontop.communication.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashMap;

@Getter
@Setter
@AllArgsConstructor
@Service
public class MessageRepository {
    private static HashMap<String, WebSocketRoom> webSocketRoomHashMap = new HashMap<>();

    public <T> void sendMessage(WebSocketSession session, String message) {
        try{
            if(session.isOpen()){
                session.sendMessage(new TextMessage(message));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createRoom(String uuid, WebSocketRoom webSocketRoom){
        webSocketRoomHashMap.put(uuid, webSocketRoom);
    }

    public HashMap<String, WebSocketRoom> getWebSocketRoomHashMap() {
        return webSocketRoomHashMap;
    }
}
