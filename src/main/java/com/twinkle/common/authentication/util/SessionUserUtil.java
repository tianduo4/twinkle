package com.twinkle.common.authentication.util;

import com.alibaba.fastjson.JSONObject;
import com.twinkle.common.authentication.SessionUser;
import com.twinkle.config.MyRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;


/**
 * description: 获取登录用户信息
 *
 * @author ：King
 * @date ：2019/1/13 9:23
 */
public class SessionUserUtil {

    /**
     * description: 获取当前用户ID
     * created by King on 2019/1/17 22:20.
     * @Param:
     * @return java.lang.String
     */
	public static String getUserId() {
		SessionUser sessionUser = getShiroUser();
		return (sessionUser == null) ? null : sessionUser.getId();
	}
    
    /**
     * description: 获取当前用户名
     * created by King on 2019/1/17 22:21.
     * @Param:
     * @return java.lang.String
     */
	public static String getUserName() {
		SessionUser sessionUser =  getShiroUser();
		return (sessionUser == null) ? null : sessionUser.getName();
	}

    /**
     * description: 获取当前用户手机号
     * created by King on 2019/1/17 22:23.
     * @Param:
     * @return java.lang.String
     */
    public static String getMobile() {
        SessionUser sessionUser =  getShiroUser();
        return (sessionUser == null) ? null : sessionUser.getMobile();
    }

    /**
     * description: 获取当前会话用户
     * created by King on 2019/1/17 22:22.
     * @Param:
     * @return com.twinkle.common.authentication.SessionUser
     */
    public static SessionUser getShiroUser() {
        Subject subject = SecurityUtils.getSubject();
        if (subject == null) {
            return null;
        }
        Session session = subject.getSession();
        if (session == null) {
            return null;
        }
        //TODO 直接获取对象转型报错，临时用json解决
        String value = (String)session.getAttribute(MyRealm.SESSION_USER_KEY);
        return JSONObject.parseObject(value,SessionUser.class);
    }
}
