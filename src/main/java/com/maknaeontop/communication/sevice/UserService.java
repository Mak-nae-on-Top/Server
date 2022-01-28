package com.maknaeontop.communication.sevice;

import com.maknaeontop.User;
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

        if(selectIdUsingId(id).equals("")){
            userMapper.addUser(id, pw, authority);
            return true;
        }
        return false;
    }

    private String selectPwUsingId(String id){
        return userMapper.selectPwUsingId(id).get(0).get("User_pw").toString();
    }

    private String selectIdUsingId(String id){
        return userMapper.selectIdUsingId(id).get(0).get("User_id").toString();
    }
}
