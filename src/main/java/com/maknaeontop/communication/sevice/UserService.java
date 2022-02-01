package com.maknaeontop.communication.sevice;

import com.maknaeontop.dto.User;
import com.maknaeontop.communication.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class UserService {
    private static UserMapper userMapper;

    public UserService(UserMapper userMapper){
        this.userMapper = userMapper;
    }

    public List<HashMap<String, Object>> getUsers(){
        return userMapper.selectUser();
    }

    public boolean validateUser(User user){
        String pwInDatabase = selectPwUsingId(user.getId());
        return pwInDatabase.equals(user.getPw());
    }

    public boolean addUser(User user){
        String id = user.getId();
        String pw = user.getPw();
        String authority = user.getAuthority();

        if(countSameId(id) == 0){
            userMapper.addUser(id, pw, authority);
            return true;
        }
        return false;
    }

    private String selectPwUsingId(String id){
        return userMapper.selectPwUsingId(id);
    }

    private int countSameId(String id){
        return userMapper.countSameId(id);
    }
}
