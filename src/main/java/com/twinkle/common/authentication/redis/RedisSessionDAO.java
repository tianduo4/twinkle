package com.twinkle.common.authentication.redis;

import com.twinkle.common.cache.RedisManager;
import com.twinkle.common.util.SerializationUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * description: reids session存储实现
 *
 * @author ：King
 * @date ：2019/1/12 23:28
 */
@Slf4j
public class RedisSessionDAO extends AbstractSessionDAO {

    private RedisManager redisManager;

    private final String DEFAULT_KEY_PREFIX = "authc_session:";

    @Override
    public void update(Session session) throws UnknownSessionException {
        this.saveSession(session);
    }

    /**
     * save session
     *
     * @param session
     * @throws UnknownSessionException
     */
    private void saveSession(Session session) throws UnknownSessionException {
        if (session == null || session.getId() == null) {
            log.warn("session or session id is null");
            return;
        }
        byte[] key = getByteKey(session.getId());
        byte[] value = SerializationUtil.serialize(session);
        int expire = redisManager.getExpire();
        if (session.getTimeout() != 0) {
            expire = (int) (session.getTimeout() / 1000);
        }else{
            expire=24*3600;
        }
        this.redisManager.set(key, value, expire);
    }

    @Override
    public void delete(Session session) {
        if (session == null || session.getId() == null) {
            log.warn("session or session id is null");
            return;
        }
        redisManager.del(this.getByteKey(session.getId()));
    }

    @Override
    public Collection<Session> getActiveSessions() {
        Set<Session> sessions = new HashSet<Session>();
//        Set<byte[]> keys = redisManager.keys(DEFAULT_KEY_PREFIX + "*");
//        if (keys != null && keys.size() > 0) {
//            for (byte[] key : keys) {
//                Session s = (Session) SerializationUtil.deserialize(redisManager.get(key));
//                sessions.add(s);
//            }
//        }
        return sessions;
    }

    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = this.generateSessionId(session);
        this.assignSessionId(session, sessionId);
        this.saveSession(session);

        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        if (sessionId == null) {
            log.warn("session id is null");
            return null;
        }
        Session session = (Session) SerializationUtil.deserialize(redisManager.get(this.getByteKey(sessionId)));
        return session;
    }

    private byte[] getByteKey(Serializable sessionId) {
        String preKey = DEFAULT_KEY_PREFIX + sessionId;
        return preKey.getBytes();
    }

//    private String getRedisSessionKey(Serializable sessionId) {
//        return DEFAULT_KEY_PREFIX + sessionId;
//    }

    public RedisManager getRedisManager() {
        return redisManager;
    }

    public void setRedisManager(RedisManager redisManager) {
        this.redisManager = redisManager;
        this.redisManager.init();
    }
}
