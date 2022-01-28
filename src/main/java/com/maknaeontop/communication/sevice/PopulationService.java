package com.maknaeontop.communication.sevice;

import com.maknaeontop.communication.mapper.PopulationMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class PopulationService {
    PopulationMapper populationMapper;

    public PopulationService(PopulationMapper populationMapper){
        this.populationMapper = populationMapper;
    }

    public List<HashMap<String, Object>> selectAllUserLocation(){
        return populationMapper.selectAllUserLocation();
    }

    public List<HashMap<String, Object>> selectByUuid(String uuid){
        return populationMapper.selectByUuid(uuid);
    }

    public HashMap<String, Object> selectLocationById(String id){
        return populationMapper.selectLocationById(id);
    }

    public boolean insertUserLocation(String uuid, String id, float x, float y, float z){
        return populationMapper.insertUserLocation();
    }
}
