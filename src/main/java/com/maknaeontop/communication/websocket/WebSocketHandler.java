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

@Component
@AllArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    MessageRepository messageRepository;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws JsonProcessingException {
        if(session != null){
            String payload = message.getPayload();
            Message msg = objectMapper.readValue(payload, Message.class);
            String roomName = msg.getUuid();
            WebSocketRoom webSocketRoom = messageRepository.getWebSocketRoomHashMap().get(roomName);
            webSocketRoom.socketCommunication(session, msg.getType());
        }
    }
}
