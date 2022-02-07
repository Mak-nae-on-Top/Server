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

    HashMap<String, Object> getLocation(String uuid, String major, String minor);

    List<HashMap<String, Object>> addBeacon(String uuid, String major, String minor, float x, float y, int floor);
}
