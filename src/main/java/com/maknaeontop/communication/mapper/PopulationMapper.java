package com.maknaeontop.communication.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Mapper
@Repository
public interface PopulationMapper {
    List<HashMap<String, Object>> selectAllUserLocation();

    List<HashMap<String, Object>> selectByUuid(String uuid);

    HashMap<String, Object> selectLocationById(String id);

    boolean insertUserLocation();
}
