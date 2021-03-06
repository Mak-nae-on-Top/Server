package com.maknaeontop.communication.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maknaeontop.dto.Message;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.List;

/**
 * This class extends TextWebSocketHandler and runs when a message is received through WebSocket.
 * How to handle messages is implemented.
 */
@Component
@AllArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    MessageRepository messageRepository;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws JsonProcessingException {
        String payload = message.getPayload();
        Message msg = objectMapper.readValue(payload, Message.class);
        String roomName = msg.getUuid();
        WebSocketRoom webSocketRoom = messageRepository.getWebSocketRoomHashMap().get(roomName);
        if(webSocketRoom == null){
            System.out.println("WARN : webSocket room is null");
        }else{
            webSocketRoom.socketCommunication(session, msg.getType());
        }
    }
}
