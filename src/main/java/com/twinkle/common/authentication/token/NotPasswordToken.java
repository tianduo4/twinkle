/** 
 * Project Name:mixc-framework-core 
 * File Name:NotPasswordToken.java 
 * Package Name:com.crland.mixc.framework.security.shiro.token 
 * Date:2016年7月11日下午2:55:08 
 * Copyright (c) 2016, crland.com.cn All Rights Reserved. 
 * 
 */
package com.twinkle.common.authentication.token;

import lombok.Data;
import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * description: 非密码验证token，扩展动态码验证。
 *
 * @author ：King
 * @date ：2019/1/12 23:28
 */
@Data
public class NotPasswordToken extends UsernamePasswordToken {

	private static final long serialVersionUID = 1L;

	private String userName;

	public NotPasswordToken(final String userName) {
		this.userName = userName;
	}

    @Override
	public void clear() {
		this.userName = null;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getName());
		sb.append(" - ");
		sb.append(userName);
		return sb.toString();
	}

	@Override
	public Object getPrincipal() {
		return getUsername();
	}

	@Override
	public Object getCredentials() {
		return null;
	}
}
