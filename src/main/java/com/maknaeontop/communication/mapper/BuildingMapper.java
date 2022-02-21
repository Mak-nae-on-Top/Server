package com.maknaeontop.communication.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Mapper
@Repository
public interface BuildingMapper {

    List<HashMap<String, Object>> selectByManager(String manager);

    HashMap<String, Integer> selectFloorRangeByUuid(String uuid);

    void updateHighestFloor(String uuid, String highestFloor);

    void updateLowestFloor(String uuid, String lowestFloor);

    boolean insertBuilding(String uuid, String name, String manager, int lowest_floor, int highest_floor);
}
