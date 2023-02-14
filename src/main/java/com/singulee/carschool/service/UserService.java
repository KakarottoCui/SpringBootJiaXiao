package com.singulee.carschool.service;

import com.singulee.carschool.pojo.User;
import com.singulee.carschool.pojo.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserService {
    @Resource
    UserMapper userMapper;

    public User findByUsername(String username) {
        return userMapper.findOneByUsername(username);
    }

    public boolean updatePassword(String phone, String password) {
        User u = new User();
        u.setPassword(password);
        u.setUsername(phone);
        u.setStatus(1);
        return userMapper.updatePassword(u) > 0;
    }

    public void insert(User user) {
        userMapper.insert(user);
    }

    public boolean update(User user) {
        return userMapper.update(user) > 0;
    }
}
