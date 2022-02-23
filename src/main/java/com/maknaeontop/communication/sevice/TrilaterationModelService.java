package com.maknaeontop.communication.sevice;

import com.maknaeontop.communication.mapper.TrilaterationModelMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.HashMap;

/**
 * TrilaterationModelService is a class that implements methods declared in TrilaterationModelMapper.
 * There are methods related to accessing the trilateration_model table.
 */
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
