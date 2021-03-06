package com.maknaeontop.communication.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * BeaconMapper is an interface of methods that connect to the beacon table of DB.
 */
@Mapper
@Repository
public interface BeaconMapper {
    HashMap<String, Object> selectCoordinate(String uuid, String major, String minor);

    void addBeacon(long id, String uuid, String major, String minor, float x, float y, int floor);

    void deleteByUuidAndFloor(String uuid, String floor);

    List<HashMap<String, Object>> selectByUuidAndFloor(String uuid, int floor);
}
