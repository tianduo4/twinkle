package com.twinkle.config;

import com.twinkle.common.authentication.filter.AuthcFilter;
import com.twinkle.common.authentication.TwinkleSessionIdGenerator;
import com.twinkle.common.authentication.TwinkleSessionManager;
import com.twinkle.common.authentication.credentials.RetryLimitCredentialsMatcher;
import com.twinkle.common.authentication.filter.VerifySignFilter;
import com.twinkle.common.authentication.redis.RedisCacheManager;
import com.twinkle.common.authentication.redis.RedisSessionDAO;
import com.twinkle.common.cache.RedisManager;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * description: shiro 配置类
 *
 * @author ：King
 * @date ：2019/1/12 16:27
 */
@Configuration
public class ShiroConfig {
    private final static long GLOBLE_SESSION_TIME_OUT = 7 * 24 * 3600L;

    /**
     * 是否开启签名验证
     */
    @Value("${app.signture.verification.enable}")
    private boolean signtureVerificationEnable = true;
    /**
     * 关闭签名验证时需要传入debugToken参数
     */
    @Value("${app.signture.verification.token}")
    private String debugToken = "";

    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //拦截器
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        // 配置不会被拦截的链接 顺序判断
        filterChainDefinitionMap.put("/login/**", "sign");
        filterChainDefinitionMap.put("/**.js", "anon");
        filterChainDefinitionMap.put("/druid/**", "anon");
        filterChainDefinitionMap.put("/swagger**/**", "anon");
        filterChainDefinitionMap.put("/webjars/**", "anon");
        filterChainDefinitionMap.put("/v2/**", "anon");
        filterChainDefinitionMap.put("/favicon.ico", "anon");
        filterChainDefinitionMap.put("/**/public/**", "anon");
        // 添加自己的过滤器并且取名为jwt
        Map<String, Filter> filterMap = new HashMap<String, Filter>(1);
        filterMap.put("authc", new AuthcFilter());
        filterMap.put("sign", new VerifySignFilter(signtureVerificationEnable,debugToken));
        shiroFilterFactoryBean.setFilters(filterMap);
        //<!-- 过滤链定义，从上向下顺序执行，一般将/**放在最为下边
        filterChainDefinitionMap.put("/**", "authc,sign");

        //未授权界面;
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    @Bean("securityManager")
    public SecurityManager securityManager(MyRealm myRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 设置realm.
        securityManager.setRealm(myRealm);
        // 自定义缓存实现 使用redis
        securityManager.setCacheManager(cacheManager());
        // 自定义session管理 使用redis
        securityManager.setSessionManager(sessionManager());

        return securityManager;
    }

    /**
     * cacheManager 缓存 redis实现
     */
    public RedisCacheManager cacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        return redisCacheManager;
    }

    @Bean
    public CredentialsMatcher credentialsMatcher() {
        RetryLimitCredentialsMatcher credentialsMatcher=new RetryLimitCredentialsMatcher(cacheManager());
        credentialsMatcher.setHashAlgorithm("md5");
        return credentialsMatcher;
    }

    @Bean
    public DefaultWebSessionManager sessionManager() {
        TwinkleSessionManager sessionManager = new TwinkleSessionManager();
        sessionManager.setGlobalSessionTimeout(GLOBLE_SESSION_TIME_OUT);
        sessionManager.setDeleteInvalidSessions(true);
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setSessionIdCookieEnabled(true);
        sessionManager.setSessionDAO(redisSessionDAO());
        sessionManager.setSessionIdCookie(sessionIdCookie());
        return sessionManager;
    }

    /**
     * RedisSessionDAO shiro sessionDao层的实现 通过redis
     */
    @Bean
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        redisSessionDAO.setSessionIdGenerator(new TwinkleSessionIdGenerator());
        return redisSessionDAO;
    }

    @Bean
    public SimpleCookie sessionIdCookie() {
        SimpleCookie sessionIdCookie = new SimpleCookie("sid");
        return sessionIdCookie;
    }

    /**
     * 配置shiro redisManager
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost("localhost");
        redisManager.setPort(6379);
        redisManager.setExpire(1800);// 配置缓存过期时间
        redisManager.setTimeout(0);
        // redisManager.setPassword(password);
        return redisManager;
    }

}