package com.maknaeontop.communication.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Mapper
@Repository
public interface UserMapper {
    List<HashMap<String, Object>> selectUser();

    String selectPwUsingId(String id);

    int addUser(String id, String password, String name);

    int countSameId(String id);

}
