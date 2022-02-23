package com.maknaeontop.communication.sevice;

import com.maknaeontop.communication.mapper.PopulationMapper;
import com.maknaeontop.dto.Coordinate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * PopulationService is a class that implements methods declared in populationMapper.
 * There are methods related to accessing the population table.
 */
@Service
public class PopulationService {
    private PopulationMapper populationMapper;

    public PopulationService(PopulationMapper populationMapper){
        this.populationMapper = populationMapper;
    }

    public List<HashMap<String, Float>> selectCoordinateAfterInsert(String deviceId, String uuid, float x, float y, int floor){
        return populationMapper.selectCoordinateAfterInsert(deviceId, uuid, x, y, floor);
    }

    public List<Coordinate> selectCoordinateInSameFloor(String uuid, int floor, String deviceId){
        return populationMapper.selectCoordinateInSameFloor(uuid, floor, deviceId);
    }
}
