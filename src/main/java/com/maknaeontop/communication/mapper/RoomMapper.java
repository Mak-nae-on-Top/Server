package com.maknaeontop.communication.mapper;

import com.maknaeontop.dto.Coordinate;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * RoomMapper is an interface of methods that connect to the room table of DB.
 */
@Mapper
@Repository
public interface RoomMapper {
    List<Coordinate> selectCoordinateByUuidAndFloorAndRoomName(String uuid, int floor, String roomName);

    boolean insertRoom(Long id, String uuid, int floor, String roomName, float x, float y);

    void deleteByUuidAndFloor(String uuid, int floor);

    List<HashMap<String, Object>> selectByUuidAndFloor(String uuid, int floor);
}
