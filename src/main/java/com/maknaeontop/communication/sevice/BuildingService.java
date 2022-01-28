package com.maknaeontop.communication.sevice;

import com.maknaeontop.communication.mapper.BuildingMapper;
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

    public List<HashMap<String, Object>> getAllByManager(){
        return buildingMapper.selectByManager();
    }

    public boolean insertBuilding(){
        return buildingMapper.insertBuilding();
    }
}
