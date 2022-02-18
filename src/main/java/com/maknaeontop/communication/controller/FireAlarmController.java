package com.maknaeontop.communication.controller;

import com.maknaeontop.communication.Response;
import com.maknaeontop.dto.Message;
import com.maknaeontop.communication.websocket.MessageRepository;
import com.maknaeontop.communication.websocket.WebSocketRoom;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
public class FireAlarmController {

    MessageRepository messageRepository;
    Response response;

    @PostMapping("/fireAlarm/{uuid}")
    public String test(@RequestBody Message message, @PathVariable String uuid){
        WebSocketRoom webSocketRoom = messageRepository.getWebSocketRoomHashMap().get(uuid);
        webSocketRoom.httpCommunication(message.getType(), messageRepository);

        return response.statusResponse("success", "message is sent successfully");
    }
}