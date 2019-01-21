package com.twinkle.controller;

import com.twinkle.common.authentication.util.SessionUserUtil;
import com.twinkle.common.cache.RedisTemplate;
import com.twinkle.common.rest.ApiResponse;
import com.twinkle.common.validator.TwinkleValidator;
import com.twinkle.user.repository.model.User;
import com.twinkle.user.repository.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * description: 登录接口
 *
 * @author ：King
 * @date ：2019/1/12 22:35
 */
@Slf4j
@Api(value = "LoginController", description = "身份认证模块")
@RestController
public class LoginController {
    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    @ApiOperation(value = "手机密码登录", notes = "手机密码登录接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "手机号", required = true, dataType = "int"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "string")
    })
    @RequestMapping("/login")
    public ApiResponse login(@RequestParam(value = "userName", required = false) String userName,
                             @RequestParam("password") String password) {
        TwinkleValidator.notNull(userName, "手机号不能为空");
        User user = userService.findByUserName(userName);
        if (user == null) {
            return ApiResponse.fail("用户名错误");
        } else if (!user.getPassword().equals(password)) {
            return ApiResponse.fail("密码错误");
        } else {
            UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
            Subject subject = SecurityUtils.getSubject();
            try {
                subject.login(token);
            } catch (ExcessiveAttemptsException ex) {
                log.warn("登录失败次数过多，请稍候在试！", ex);
                return ApiResponse.fail("登录失败次数过多，请30分钟后再试。");
            } catch (UnknownAccountException ex) {
                log.warn("用户名或密码错误！", ex);
                return ApiResponse.fail("用户名或密码错误。");
            } catch (IncorrectCredentialsException ex) {
                log.warn("用户名或密码错误！", ex);
                return ApiResponse.fail("用户名或密码错误。");
            } catch (LockedAccountException ex) {
                log.warn("帐号已被锁定！", ex);
                return ApiResponse.fail("帐号已被锁定。");
            } catch (DisabledAccountException ex) {
                log.warn("帐号已被禁用！", ex);
                return ApiResponse.fail("帐号已被禁用。");
            } catch (Exception ex) {
                log.error("登录出错！", ex);
                return ApiResponse.fail("登录出错。");
            }
            redisTemplate.set("test","king",2000);
            return ApiResponse.ok(SessionUserUtil.getShiroUser());
        }
    }
}
