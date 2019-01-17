package com.twinkle.user.repository.service;

import com.twinkle.user.repository.model.User;

/**
 * description: 会员服务接口
 *
 * @author ：King
 * @date ：2019/1/12 11:11
 */
public interface UserService {
    User selectById(int id);

    User findByUserName(String username);
}
