package com.twinkle.config;

import com.alibaba.fastjson.JSONObject;
import com.twinkle.common.authentication.SessionUser;
import com.twinkle.user.repository.model.User;
import com.twinkle.user.repository.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * description: TODO
 *
 * @author ：King
 * @date ：2019/1/12 16:24
 */
@Component
public class MyRealm extends AuthorizingRealm {
    public final static String SESSION_USER_KEY = "SESSION_USER_KEY";

    @Autowired
    private UserService userService;

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        return simpleAuthorizationInfo;
    }

    /**
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        String username = (String) auth.getPrincipal();
        if (username == null) {
            throw new AuthenticationException("token无效");
        }

        User user = userService.findByUserName(username);
        if (user == null) {
            throw new AuthenticationException("用户不存在!");
        }

//        if (!JwtUtil.verify(token, username, user.getPassword())) {
//            throw new AuthenticationException("用户名或密码错误");
//        }
        SimpleAuthenticationInfo authenticationInfo = null;
//        if(StringUtils.isNotBlank(user.getPassword())){
//            authenticationInfo = new SimpleAuthenticationInfo(user.getMobile(),
//                    user.getPassword(), ByteSource.Util.bytes(user.getSalt()), getName());
//        }else{
//            authenticationInfo = new SimpleAuthenticationInfo(user.getMobile(),
//                    null, null, getName());
//        }
        authenticationInfo = new SimpleAuthenticationInfo(user.getUserName(),
                user.getPassword(), null, getName());
        SessionUser sessionUser = new SessionUser();
        sessionUser.setId(user.getId()+"");
        sessionUser.setUserName(user.getUserName());
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        sessionUser.setToken(session.getId().toString());
        session.setAttribute(SESSION_USER_KEY, JSONObject.toJSONString((sessionUser)));
        return authenticationInfo;
    }

    @Override
    public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);

    }

    @Override
    public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        super.clearCachedAuthenticationInfo(principals);
    }

    @Override
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }

    public void clearAllCachedAuthorizationInfo() {
        getAuthorizationCache().clear();
    }

    public void clearAllCachedAuthenticationInfo() {
        getAuthenticationCache().clear();
    }

    public void clearAllCache() {
        clearAllCachedAuthenticationInfo();
        clearAllCachedAuthorizationInfo();
    }
}