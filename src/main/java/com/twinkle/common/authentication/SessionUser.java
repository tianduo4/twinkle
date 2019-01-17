package com.twinkle.common.authentication;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * description: 自定义Authentication 会话态用户信息，使得Subject除了携带用户的登录名外还可以携带更多信息。
 *
 * @author ：King
 * @date ：2019/1/12 23:28
 */
@Data
public class SessionUser implements Serializable{
	private static final long serialVersionUID = 1L;

    /**
     * 会话token
     */
	private String token;
	/**
	 * 用户Id
	 */
	private String id;
	/**
	 * 手机号码
     */
	private String mobile;
	/**
	 * 用户名
	 */
	private String userName;
	/**
	 * 姓名
	 */
	private String name;
	/**
	 * 昵称
	 */
	private String nickname;
	/**
	 * 头像
	 */
	private String avatar;
	/**
	 * 性别
	 */
	private String gender;
	/**
	 * 生日
	 */
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
	private Date birthday;
}