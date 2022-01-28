package com.maknaeontop.communication.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Mapper
@Repository
public interface UserMapper {
    List<HashMap<String, Object>> selectUser();

    List<HashMap<String, Object>> selectPwUsingId(String id);

    List<HashMap<String, Object>> addUser(String id, String pw, String authority);

    List<HashMap<String, Object>> selectIdUsingId(String id);

}
