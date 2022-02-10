package com.maknaeontop.communication.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MsgService {
    private final ObjectMapper objectMapper;
    private Map<String, MsgRoom> msgRoomMap;

    @PostConstruct
    private void init(){
        msgRoomMap = new LinkedHashMap<>();
    }

    public List<MsgRoom> findAllRoom(){
        return new ArrayList<>(msgRoomMap.values());
    }

    public MsgRoom findById(String roomId){
        return msgRoomMap.get(roomId);
    }

    public MsgRoom createRoom(String roomId){
        MsgRoom newRoom = MsgRoom.builder().roomId(roomId).build();
        msgRoomMap.put(roomId,newRoom);
        return newRoom;
    }

    public <T> void sendMessage(WebSocketSession session, T message){
        try{
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        }catch (IOException e){
            log.error(e.getMessage(), e);
        }
    }
}
