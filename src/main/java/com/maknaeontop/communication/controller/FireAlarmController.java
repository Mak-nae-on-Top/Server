package com.maknaeontop.communication.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

//@RequestMapping("fireAlarm")
@RestController
@EnableWebSocketMessageBroker
@Configuration
@Component
public class FireAlarmController {

    private SimpMessagingTemplate messagingTemplate;

    //@MessageMapping("/fireAlarm")
    @GetMapping("/fireAlarm")
    public boolean communicateWithFireAlarm(@RequestParam String roomId){
        // TODO: Change address and try-catch to exception handler
        try{
            messagingTemplate.convertAndSend("/sub/app/event/" + roomId, "test");
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
