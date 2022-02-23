package com.maknaeontop.communication.controller;

/**
 * @author Namho Kim
 * @since 1.0
 * @version 1.0
 */

import com.maknaeontop.communication.Response;
import com.maknaeontop.dto.Message;
import com.maknaeontop.communication.websocket.MessageRepository;
import com.maknaeontop.communication.websocket.WebSocketRoom;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * FireAlarmController is the controller for the functions
 * that communicate with the app.
 */
@RestController
@AllArgsConstructor
public class FireAlarmController {
    private final MessageRepository messageRepository;
    private final Response response = new Response();

    /**
     * Method to receive fire occurrence from fire alarm in the building.
     *
     * @param uuid  the uuid of the building
     * @return      status and message in json format
     */
    @GetMapping("/fireAlarm/{uuid}")
    public String test(@PathVariable String uuid){
        WebSocketRoom webSocketRoom = messageRepository.getWebSocketRoomHashMap().get(uuid);
        webSocketRoom.httpCommunication(Message.MessageType.FIRE, messageRepository);

        return response.statusResponse("success", "alarm is successfully sent");
    }
}