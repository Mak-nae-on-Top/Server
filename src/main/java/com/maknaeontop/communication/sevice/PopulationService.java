package com.maknaeontop.communication.sevice;

import com.maknaeontop.communication.mapper.PopulationMapper;
import com.maknaeontop.dto.Coordinate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class PopulationService {
    private PopulationMapper populationMapper;

    public PopulationService(PopulationMapper populationMapper){
        this.populationMapper = populationMapper;
    }

    public List<HashMap<String, Float>> selectLocationAfterInsert(String deviceId, String uuid, float x, float y, int floor){
        return populationMapper.selectLocationAfterInsert(deviceId, uuid, x, y, floor);
    }

    public List<Coordinate> selectLocationInSameFloor(String uuid, int floor, String deviceId){
        return populationMapper.selectLocationInSameFloor(uuid, floor, deviceId);
    }
}
