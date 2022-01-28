package com.maknaeontop.communication.sevice;

import com.maknaeontop.communication.mapper.RoomMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class RoomService {
    RoomMapper roomMapper;

    public RoomService(RoomMapper roomMapper){
        this.roomMapper = roomMapper;
    }

    List<HashMap<String, Object>> selectRoom(){
        return roomMapper.selectRoom();
    }

    List<HashMap<String, Object>> selectLocationByUuidNRoomName(String uuid, String roomName){
        return roomMapper.selectLocationByUuidNRoomName(uuid, roomName);
    }

    boolean insertRoom(String uuid, String roomName, float x, float y, float z){
        return roomMapper.insertRoom(uuid, roomName, x, y, z);
    }
}
