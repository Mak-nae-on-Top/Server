package com.maknaeontop.communication.sevice;

import com.maknaeontop.communication.mapper.TrilaterationModelMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.HashMap;

@Service
@AllArgsConstructor
public class TrilaterationModelService {
    private final TrilaterationModelMapper trilaterationModelMapper;

    public void insertConstants(String uuid, float a, float b){
        trilaterationModelMapper.insertConstants(uuid, a, b);
    }

    public HashMap<String, Float> selectConstants(String uuid){
        return trilaterationModelMapper.selectConstants(uuid);
    }
}
