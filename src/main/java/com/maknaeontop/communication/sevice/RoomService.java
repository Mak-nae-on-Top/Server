package com.maknaeontop.communication.sevice;

import com.maknaeontop.communication.mapper.RoomMapper;
import com.maknaeontop.dto.RoomListOnFloor;
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

    public void insertRoom(RoomListOnFloor roomListOnFloor){
        for(Room room : roomListOnFloor.getRoomList()){
            roomMapper.insertRoom(roomListOnFloor.getUuid(), Integer.parseInt(roomListOnFloor.getFloor()), room.getRoomName(), room.getX(), room.getY());
        }
    }
}
