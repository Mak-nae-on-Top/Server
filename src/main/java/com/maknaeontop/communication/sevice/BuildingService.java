package com.maknaeontop.communication.sevice;

import com.maknaeontop.communication.mapper.BuildingMapper;
import com.maknaeontop.dto.Base64Image;
import com.maknaeontop.dto.UuidAndFloor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class BuildingService {
    private static BuildingMapper buildingMapper;

    public BuildingService(BuildingMapper buildingMapper){
        this.buildingMapper = buildingMapper;
    }

    public String getNameByUuid(){
        return buildingMapper.selectnamebyUuid();
    }

    public String[] getManagerByUuid(){
        return buildingMapper.selectManagerByUuid();
    }

    public List<HashMap<String, Object>> selectByManager(String manager){
        return buildingMapper.selectByManager(manager);
    }

    public HashMap<String, Integer> selectFloorRangeByUuid(String uuid){
        return buildingMapper.selectFloorRangeByUuid(uuid);
    }

    public void updateHighestFloor(String uuid, String highestFloor){
        buildingMapper.updateHighestFloor(uuid, highestFloor);
    }

    public void updateLowestFloor(String uuid, String lowestFloor){
        buildingMapper.updateLowestFloor(uuid, lowestFloor);
    }

    public void deleteFloor(UuidAndFloor uuidAndFloor){
        HashMap<String, Integer> lowestAndHighest = selectFloorRangeByUuid(uuidAndFloor.getUuid());

        if(lowestAndHighest.get("lowest_floor").equals(uuidAndFloor.getFloor())){
            int newLowestFloor = Integer.parseInt(uuidAndFloor.getFloor()) + 1;
            updateLowestFloor(uuidAndFloor.getUuid(), Integer.toString(newLowestFloor));
        }
        else if(lowestAndHighest.get("highest_floor").equals(uuidAndFloor.getFloor())){
            int newHighestFloor = Integer.parseInt(uuidAndFloor.getFloor()) - 1;
            updateHighestFloor(uuidAndFloor.getUuid(), Integer.toString(newHighestFloor));
        }
    }

    public boolean insertBuilding(String uuid, String name, String manager, int lowest_floor, int highest_floor){
        return buildingMapper.insertBuilding(uuid, name, manager, lowest_floor, highest_floor);
    }

    public boolean insertBuilding(Base64Image base64Image, String id) {
        return insertBuilding(base64Image.getUuid(), base64Image.getBuilding_name(), id, Integer.parseInt(base64Image.getFloor()), Integer.parseInt(base64Image.getFloor()));
    }
}
