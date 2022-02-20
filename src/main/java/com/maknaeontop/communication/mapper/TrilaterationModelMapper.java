package com.maknaeontop.communication.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Mapper
@Repository
public interface TrilaterationModelMapper {
    void insertConstants(String uuid, float a, float b);

    HashMap<String, Float> selectConstants(String uuid);
}
