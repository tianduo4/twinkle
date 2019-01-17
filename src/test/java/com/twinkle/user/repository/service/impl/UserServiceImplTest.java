package com.twinkle.user.repository.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.twinkle.user.repository.mapper.UserMapper;
import com.twinkle.user.repository.model.User;
import mockit.Injectable;
import mockit.Tested;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.test4j.module.jmockit.IMockict;
import org.testng.annotations.Test;

/**
 * description: TODO
 *
 * @author ：King
 * @date ：2019/1/13 16:25
 */
public class UserServiceImplTest {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImplTest.class);

    @Tested
    private UserServiceImpl userServiceImpl;

    @Injectable
    private UserMapper userMapper;

    @Test
    public void selectById() throws Exception {
        new IMockict.NonStrictExpectations() {
            {
                userMapper.selectById(1);
                String mockData="{\"id\":1,\"password\":\"123456\",\"phone\":\"18520895966\",\"userName\":\"King\"}";
                result = JSONObject.parseObject(mockData,User.class);
            }
        };
        User user = userServiceImpl.selectById(1);
        logger.info(JSONObject.toJSONString(user));
        Assert.assertNotNull(user);
    }

    @Test
    public void findByUserName() throws Exception {

    }

}