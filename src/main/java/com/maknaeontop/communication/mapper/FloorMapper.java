package com.maknaeontop.communication.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Mapper
@Repository
public interface FloorMapper {
    HashMap<String, Integer> selectHeightsAndWidthsByFloor(String uuid, int floor);

    void insertImageInfo(String uuid, int floor, int imageHeight, int imageWidth);

    void updateBlueprintInfo(String uuid, int floor, int blueprintHeight, int blueprintWidth);

    void deleteFloorByUuidAndFloor(String uuid, String floor);
}
