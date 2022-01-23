package com.maknaeontop.communication.sevice;

import com.maknaeontop.communication.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class UserService {
    private UserMapper userMapper;

    public UserService(UserMapper userMapper){
        this.userMapper = userMapper;
    }

    public List<HashMap<String, Object>> getUsers(){
        return userMapper.selectUser();
    }
}
