package com.maknaeontop.communication.websocket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import java.util.HashMap;

@Getter
@Setter
@AllArgsConstructor
@Service
public class MessageRepository {
    private static HashMap<String, WebSocketRoom> webSocketRoomHashMap = new HashMap<>();

    public void createRoom(String uuid, WebSocketRoom webSocketRoom){
        webSocketRoomHashMap.put(uuid, webSocketRoom);
    }

    public HashMap<String, WebSocketRoom> getWebSocketRoomHashMap() {
        return webSocketRoomHashMap;
    }
}
