package com.maknaeontop.communication.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Mapper
@Repository
public interface PopulationMapper {
    List<HashMap<String, Float>> selectLocationAfterInsert(String deviceId, String uuid, float x, float y, int floor);

    List<HashMap<String, Float>> selectLocationInSameFloor(String uuid, int floor, String deviceId);
}
