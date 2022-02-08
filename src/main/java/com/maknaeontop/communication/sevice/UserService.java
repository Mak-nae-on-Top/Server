package com.maknaeontop.communication.sevice;

import com.maknaeontop.dto.User;
import com.maknaeontop.communication.mapper.UserMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class UserService implements UserDetailsService {
    private static UserMapper userMapper;

    public UserService(UserMapper userMapper){
        this.userMapper = userMapper;
    }

    public List<HashMap<String, Object>> getUsers(){
        return userMapper.selectUser();
    }

    public String validateUser(User user){
        String pwInDatabase = selectPwUsingId(user.getId());
        if(pwInDatabase.equals("")){
            return "fail: no matching ID exist";
        }
        if(!pwInDatabase.equals(user.getPassword())){
            return "fail: password does not match";
        }
        return null;
    }

    public boolean addUser(User user){
        String id = user.getId();
        String password = user.getPassword();
        String name = user.getName();

        if(countSameId(id) == 0){
            userMapper.addUser(id, password, name);
            return true;
        }
        return false;
    }

    public String selectPwUsingId(String id){
        return userMapper.selectPwUsingId(id);
    }

    private int countSameId(String id){
        return userMapper.countSameId(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String pw = selectPwUsingId(username);
        return new org.springframework.security.core.userdetails.User(username, pw, new ArrayList<>());
    }
}
