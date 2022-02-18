package com.maknaeontop.communication.sevice;

import com.maknaeontop.communication.mapper.RoomMapper;
import com.maknaeontop.dto.RoomListOnFloor;
import com.maknaeontop.dto.Room;
import com.maknaeontop.dto.RouteRequest;
import com.maknaeontop.dto.UuidAndFloor;
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

    public List<HashMap<String, Object>> selectLocationByUuidAndFloorAndRoomName(RouteRequest routeRequest){
        return roomMapper.selectLocationByUuidAndFloorAndRoomName(routeRequest.getUuid(), Integer.parseInt(routeRequest.getFloor()), routeRequest.getDestination());
    }

    public void insertRoom(RoomListOnFloor roomListOnFloor){
        for(Room room : roomListOnFloor.getCoordinates()){
            roomMapper.insertRoom(room.getId(), roomListOnFloor.getUuid(), Integer.parseInt(roomListOnFloor.getFloor()), room.getRoomName(), room.getX(), room.getY());
        }
    }

    public void deleteByUuidAndFloor(UuidAndFloor uuidAndFloor){
        roomMapper.deleteByUuidAndFloor(uuidAndFloor.getUuid(), Integer.parseInt(uuidAndFloor.getFloor()));
    }

    public List<HashMap<String, Object>> selectByUuidAndFloor(String uuid, int floor){
        return roomMapper.selectByUuidAndFloor(uuid, floor);
    }
}
