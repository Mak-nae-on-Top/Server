package com.maknaeontop.communication.sevice;

import com.maknaeontop.communication.mapper.PopulationMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class PopulationService {
    private PopulationMapper populationMapper;

    public PopulationService(PopulationMapper populationMapper){
        this.populationMapper = populationMapper;
    }

    public List<HashMap<String, Object>> selectAllUserLocation(){
        return populationMapper.selectAllUserLocation();
    }

    public List<HashMap<String, Object>> selectByUuid(String uuid){
        return populationMapper.selectByUuid(uuid);
    }

    public HashMap<String, Object> selectLocationById(String deviceId){
        return populationMapper.selectLocationByDeviceId(deviceId);
    }

    public HashMap<String,Object> selectUuidAndFloorByDeviceId(String deviceId){
        return populationMapper.selectUuidAndFloorByDeviceId(deviceId);
    }

    public List<HashMap<String, Float>> selectLocationByUuid(String uuid, int floor, String deviceId){
        return populationMapper.selectLocationByUuidWithoutDeviceId(uuid, floor, deviceId);
    }

    public boolean insertUserLocation(String deviceId, String uuid, float x, float y, int floor){
        return populationMapper.insertUserLocation(deviceId, uuid, x, y, floor);
    }
}
