package com.maknaeontop.communication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

//@RequestMapping("fireAlarm")
@RestController
//@EnableWebSocketMessageBroker
//@Configuration
//@Component
public class FireAlarmController {

    private SimpMessagingTemplate messagingTemplate;

    //@MessageMapping("/fireAlarm")
    //@SendTo("/app/event/{roomId}")
    @GetMapping("/fireAlarm")
    public String communicateWithFireAlarm(@DestinationVariable("roomId") String roomId){
        /*
        // TODO: Change address and try-catch to exception handler
        try{
            messagingTemplate.convertAndSend("/sub/app/event/" + roomId, roomId);
            return true;
        }catch (Exception e){
            return false;
        }
         */
        return roomId;
    }
}
