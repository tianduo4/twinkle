package com.twinkle.user.repository.mapper;

import com.alibaba.fastjson.JSONObject;
import com.twinkle.common.MapperBaseTest;
import com.twinkle.user.repository.model.User;
import org.test4j.module.spring.annotations.SpringBeanByName;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * description: 会员测试类
 *
 * @author ：King
 * @date ：2019/1/13 14:37
 */
public class UserMapperTest extends MapperBaseTest {

    @SpringBeanByName
    private UserMapper userMapper;

    @Test
    public void selectById() throws Exception {
        User user = userMapper.selectById(1);
        logger.info(JSONObject.toJSONString(user));
        Assert.assertNotNull(user);
    }

    @Test
    public void findByUserName() throws Exception {
        User user = userMapper.findByUserName("King");
        logger.info(JSONObject.toJSONString(user));
        Assert.assertNotNull(user);
    }

}