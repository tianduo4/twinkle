package com.twinkle.user.repository.mapper;

import com.twinkle.user.repository.model.User;
import org.apache.ibatis.annotations.Param;

/**
 * description: 会员服务
 *
 * @author ：King
 * @date ：2019/1/12 11:22
 */
public interface UserMapper {

    User selectById(@Param("id")int id);

    User findByUserName(@Param("userName") String userName);
}
