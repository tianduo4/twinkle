package com.twinkle.common.authentication.credentials;

import lombok.Data;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * ClassName: 限制密码重试次数凭证管理器。 <br/>
 * date: 2016年7月8日 下午5:49:56 <br/>
 *
 * @author ZHUANGWEILIANG1
 */
@Data
public class RetryLimitCredentialsMatcher extends SimpleCredentialsMatcherWapper {

    private Cache<String, AtomicInteger> cache;

    public RetryLimitCredentialsMatcher(CacheManager cacheManager) {
        this.cache = cacheManager.getCache("passwordRetryCache");
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken authcToken, AuthenticationInfo info) {
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        String username = token.getUsername();
        String cacheName = "password-retry-" + username;
        AtomicInteger retryCount = this.cache.get(cacheName);

        synchronized (cacheName) {
            if (retryCount == null) {
                retryCount = new AtomicInteger(0);
            }
        }

        if (retryCount.incrementAndGet() > 5) {
            throw new ExcessiveAttemptsException("登录失败次数过多，请30分钟后再试");
        }

        boolean matches = super.doCredentialsMatch(token, info);

        if (matches) {
            this.cache.remove(cacheName);
        } else {
            this.cache.put(cacheName, retryCount);
        }
        return matches;
    }

}
