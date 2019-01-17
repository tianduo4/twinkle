package com.twinkle.common.authentication;

import com.twinkle.common.util.IdGenUtil;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;

import java.io.Serializable;

/**
 * description: 非密码验证token，扩展动态码验证。
 *
 * @author ：King
 * @date ：2019/1/12 23:28
 */
public class TwinkleSessionIdGenerator extends JavaUuidSessionIdGenerator {
	/**
	 * description: 会话id生成器
	 * created by King on 2019/1/17 22:36.
	 * @Param: session
	 * @return java.io.Serializable
	 */
	@Override
	public Serializable generateId(Session session) {

        return IdGenUtil.getNextId();
    }
}