package com.maknaeontop.communication.sevice;

import com.maknaeontop.communication.mapper.RoomMapper;
import com.maknaeontop.dto.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * RoomService is a class that implements methods declared in roomMapper.
 * There are methods related to accessing the room table.
 */
@Service
public class RoomService {
    private RoomMapper roomMapper;

    public RoomService(RoomMapper roomMapper){
        this.roomMapper = roomMapper;
    }

    /**
     * Method to select coordinate in DB by uuid, floor and room name.
     *
     * @param routeRequest  the uuid, floor and destination's name
     * @return              the selected room's coordinate
     */
    public List<Coordinate> selectCoordinateByUuidAndFloorAndRoomName(RouteRequest routeRequest){
        return roomMapper.selectCoordinateByUuidAndFloorAndRoomName(routeRequest.getUuid(), Integer.parseInt(routeRequest.getFloor()), routeRequest.getDestination());
    }

    /**
     * Method to insert room's information on DB.
     *
     * @param roomListOnFloor   the room's list
     */
    public void insertRoom(RoomListOnFloor roomListOnFloor){
        for(Room room : roomListOnFloor.getCoordinates()){
            roomMapper.insertRoom(room.getId(), roomListOnFloor.getUuid(), Integer.parseInt(roomListOnFloor.getFloor()), room.getRoom_name(), room.getX(), room.getY());
        }
    }

    /**
     * Method to delete room's information on DB using uuid and floor.
     * @param uuidAndFloor      the uuid and floor
     */
    public void deleteByUuidAndFloor(UuidAndFloor uuidAndFloor){
        roomMapper.deleteByUuidAndFloor(uuidAndFloor.getUuid(), Integer.parseInt(uuidAndFloor.getFloor()));
    }

    /**
     * Method to select rooms on DB using uuid and floor.
     * @param uuid      the building uuid
     * @param floor     the user's location floor
     * @return          the selected room list
     */
    public List<HashMap<String, Object>> selectByUuidAndFloor(String uuid, int floor){
        return roomMapper.selectByUuidAndFloor(uuid, floor);
    }
}
