package com.maknaeontop.communication.sevice;

import com.maknaeontop.communication.mapper.FloorMapper;
import com.maknaeontop.dto.UuidAndFloor;
import lombok.AllArgsConstructor;
import org.apache.ibatis.javassist.runtime.Inner;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class FloorService {
    private static FloorMapper floorMapper;

    public FloorService(FloorMapper floorMapper){
        this.floorMapper = floorMapper;
    }

    public HashMap<String, Integer> selectHeightsAndWidthsByFloor(String uuid, int floor){
        return floorMapper.selectHeightsAndWidthsByFloor(uuid, floor);
    }

    public void insertImageInfo(String uuid, int floor, int imageHeight, int imageWidth){
        floorMapper.insertImageInfo(uuid, floor, imageHeight, imageWidth);
    }

    public void updateBlueprintInfo(String uuid, int floor, int blueprintHeight, int blueprintWidth){
        floorMapper.updateBlueprintInfo(uuid, floor, blueprintHeight, blueprintWidth);
    }

    public void deleteFloorByUuidAndFloor(UuidAndFloor uuidAndFloor){
        floorMapper.deleteFloorByUuidAndFloor(uuidAndFloor.getUuid(), uuidAndFloor.getFloor());
    }
}
