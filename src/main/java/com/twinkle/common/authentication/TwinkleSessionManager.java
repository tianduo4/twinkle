package com.twinkle.common.authentication;

import com.twinkle.common.constant.ApiBaseConstant;
import com.twinkle.common.util.IdGenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * description: 自定义会话管理器
 *
 * @author ：King
 * @date ：2019/1/12 23:28
 */
@Slf4j
public class TwinkleSessionManager extends DefaultWebSessionManager {

    public final static String SESSION_USER = "SESSION_USER_KEY";

    /**
     * 自定义缓存，存储 客户端-sessionid
     */
    private static final Map<String, Serializable> SESSION_MAP = new HashMap<>();

    /**
     * 默认session的过期时间。单位为毫秒
     */
    private static Integer DEFAULT_SESSION_TIME_OUT = 7 * 24 * 60 * 60 * 1000;// session的过期时间。单位为MS

    /**
     * 根据客户端的sessionIdKey获取真正的sessionId
     *
     * @see org.apache.shiro.web.session.mgt.DefaultWebSessionManager#getSessionId(javax.servlet.ServletRequest,
     * javax.servlet.ServletResponse)
     */
    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        String sessionId = req.getParameter(ApiBaseConstant.ACCESS_TOKEN);
        if (StringUtils.isEmpty(sessionId)) {
            sessionId = req.getHeader(ApiBaseConstant.ACCESS_TOKEN);
        }
        Serializable id = SESSION_MAP.get(sessionId);
        return id == null ? sessionId : id;
    }

    /**
     * 创建一个session
     *
     * @see org.apache.shiro.web.session.mgt.DefaultWebSessionManager#onStart(org.apache.shiro.session.Session,
     * org.apache.shiro.session.mgt.SessionContext)
     */
    @Override
    protected void onStart(Session session, SessionContext context) {
        if (!WebUtils.isHttp(context)) {
            log.warn("非HTTP请求不创建session");
            return;
        }
        HttpServletRequest request = WebUtils.getHttpRequest(context);
        request.removeAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE);
        request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_IS_NEW, Boolean.TRUE);
        String sessionId = IdGenUtil.getNextId();
        Serializable id = session.getId();
        session.setTimeout(DEFAULT_SESSION_TIME_OUT);
        // 存储sessionIdKey和真正的sessionId
        SESSION_MAP.put(sessionId, id);
    }
}
