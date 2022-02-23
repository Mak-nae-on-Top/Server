package com.maknaeontop.communication.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

/**
 * TrilaterationModelMapper is an interface of methods that connect to the trilateration_model table of DB.
 */
@Mapper
@Repository
public interface TrilaterationModelMapper {
    void insertConstants(String uuid, float a, float b);

    HashMap<String, Float> selectConstants(String uuid);
}
