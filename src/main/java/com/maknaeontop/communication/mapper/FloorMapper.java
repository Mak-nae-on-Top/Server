package com.maknaeontop.communication.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Mapper
@Repository
public interface FloorMapper {
    HashMap<String, Integer> selectHeightsAndWidthsByFloor(String uuid, String floor);

    void insertImageInfo(String uuid, String floor, int imageHeight, int imageWidth);

    void updateBlueprintInfo(String uuid, String floor, int blueprintHeight, int blueprintWidth);
}
