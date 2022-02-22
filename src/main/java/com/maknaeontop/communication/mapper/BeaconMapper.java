package com.maknaeontop.communication.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Mapper
@Repository
public interface BeaconMapper {
    HashMap<String, Object> selectCoordinate(String uuid, String major, String minor);

    List<HashMap<String, Object>> addBeacon(long id, String uuid, String major, String minor, float x, float y, int floor);

    void deleteByUuidAndFloor(String uuid, String floor);

    List<HashMap<String, Object>> selectByUuidAndFloor(String uuid, int floor);
}
