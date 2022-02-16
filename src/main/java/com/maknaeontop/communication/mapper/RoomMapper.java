package com.maknaeontop.communication.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Mapper
@Repository
public interface RoomMapper {
    List<HashMap<String, Object>> selectRoom();

    List<HashMap<String, Object>> selectLocationByUuidNRoomName(String uuid, String roomName);

    boolean insertRoom(String uuid, int floor, String roomName, float x, float y);

    void deleteByUuidAndFloor(String uuid, int floor);
}
