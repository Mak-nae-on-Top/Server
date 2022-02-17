package com.maknaeontop.communication.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Mapper
@Repository
public interface RoomMapper {
    List<HashMap<String, Object>> selectRoom();

    List<HashMap<String, Object>> selectLocationByUuidAndFloorAndRoomName(String uuid, int floor, String roomName);

    boolean insertRoom(Long id, String uuid, int floor, String roomName, float x, float y);

    void deleteByUuidAndFloor(String uuid, int floor);

    List<HashMap<String, Object>> selectByUuidAndFloor(String uuid, int floor);
}
