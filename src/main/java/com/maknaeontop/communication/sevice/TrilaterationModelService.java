package com.maknaeontop.communication.sevice;

import com.maknaeontop.communication.mapper.TrilaterationModelMapper;
import org.springframework.stereotype.Service;
import java.util.HashMap;

@Service
public class TrilaterationModelService {
    private TrilaterationModelMapper trilaterationModelMapper;

    public void insertConstants(String uuid, float a, float b){
        trilaterationModelMapper.insertConstants(uuid, a, b);
    }

    public HashMap<String, Float> selectConstants(String uuid){
        return trilaterationModelMapper.selectConstants(uuid);
    }
}
