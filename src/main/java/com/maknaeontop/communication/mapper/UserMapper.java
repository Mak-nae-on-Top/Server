package com.maknaeontop.communication.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * UserMapper is an interface of methods that connect to the user table of DB.
 */
@Mapper
@Repository
public interface UserMapper {
    String selectPwUsingId(String id);

    int addUser(String id, String password, String name);

    int countSameId(String id);

}
