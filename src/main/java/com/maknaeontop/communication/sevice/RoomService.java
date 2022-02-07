package com.maknaeontop.communication.sevice;

import com.maknaeontop.communication.mapper.RoomMapper;
import com.maknaeontop.dto.Room;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class RoomService {
    private RoomMapper roomMapper;

    public RoomService(RoomMapper roomMapper){
        this.roomMapper = roomMapper;
    }

    public List<HashMap<String, Object>> selectRoom(){
        return roomMapper.selectRoom();
    }

    public List<HashMap<String, Object>> selectLocationByUuidNRoomName(String uuid, String roomName){
        return roomMapper.selectLocationByUuidNRoomName(uuid, roomName);
    }

    public boolean insertRoom(Room room){
        return roomMapper.insertRoom(room.getUuid(), room.getRoomName(), room.getX(), room.getY(), room.getFloor());
    }
}
