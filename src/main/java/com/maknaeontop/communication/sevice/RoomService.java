package com.maknaeontop.communication.sevice;

import com.maknaeontop.communication.mapper.RoomMapper;
import com.maknaeontop.dto.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class RoomService {
    private RoomMapper roomMapper;

    public RoomService(RoomMapper roomMapper){
        this.roomMapper = roomMapper;
    }

    public List<Coordinate> selectLocationByUuidAndFloorAndRoomName(RouteRequest routeRequest){
        return roomMapper.selectLocationByUuidAndFloorAndRoomName(routeRequest.getUuid(), Integer.parseInt(routeRequest.getFloor()), routeRequest.getDestination());
    }

    public void insertRoom(RoomListOnFloor roomListOnFloor){
        for(Room room : roomListOnFloor.getCoordinates()){
            roomMapper.insertRoom(room.getId(), roomListOnFloor.getUuid(), Integer.parseInt(roomListOnFloor.getFloor()), room.getRoom_name(), room.getX(), room.getY());
        }
    }

    public void deleteByUuidAndFloor(UuidAndFloor uuidAndFloor){
        roomMapper.deleteByUuidAndFloor(uuidAndFloor.getUuid(), Integer.parseInt(uuidAndFloor.getFloor()));
    }

    public List<HashMap<String, Object>> selectByUuidAndFloor(String uuid, int floor){
        return roomMapper.selectByUuidAndFloor(uuid, floor);
    }
}
