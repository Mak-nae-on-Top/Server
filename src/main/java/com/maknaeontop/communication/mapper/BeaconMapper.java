package com.maknaeontop.communication.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Mapper
@Repository
public interface BeaconMapper {
    List<HashMap<String, Object>> selectAllBeacons();

    List<HashMap<String, Object>> selectBeaconsUsingUUID(String uuid);

    HashMap<String, Object> selectLocation(String uuid, String major, String minor);

    int selectFloor(String uuid, String major, String minor);

    HashMap<String, Float> selectMaxXYByUuidAndFloor(String uuid, int floor);

    List<HashMap<String, Object>> addBeacon(long id, String uuid, String major, String minor, float x, float y, int floor);

    void deleteByUuidAndFloor(String uuid, String floor);

    List<HashMap<String, Object>> selectByUuidAndFloor(String uuid, int floor);
}
