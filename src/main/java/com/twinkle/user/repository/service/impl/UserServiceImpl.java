package com.twinkle.user.repository.service.impl;

import com.twinkle.user.repository.mapper.UserMapper;
import com.twinkle.user.repository.model.User;
import com.twinkle.user.repository.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * description: TODO
 *
 * @author ：King
 * @date ：2019/1/12 11:16
 */
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public User selectById(int id) {
        return userMapper.selectById(id);
    }

    @Override
    public User findByUserName(String userName) {
        return userMapper.findByUserName(userName);
    }
}
