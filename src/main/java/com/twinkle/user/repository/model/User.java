package com.twinkle.user.repository.model;

import lombok.Data;

/**
 * description: 用户
 *
 * @author ：King
 * @date ：2019/1/12 11:31
 */
@Data
public class User {

    private Integer id;

    private String userName;

    private String password;

    private String phone;
}
