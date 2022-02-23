package com.maknaeontop.communication.sevice;

import com.maknaeontop.dto.User;
import com.maknaeontop.communication.mapper.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * UserService is a class that implements methods declared in UserMapper.
 * There are methods related to accessing the user table.
 */
@Service
public class UserService implements UserDetailsService {
    private static UserMapper userMapper;

    public UserService(UserMapper userMapper){
        this.userMapper = userMapper;
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
