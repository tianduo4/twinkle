package com.twinkle.common.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.text.MessageFormat;
import java.util.Map;
import java.util.Set;

/**
 * ClassName: RedisManager <br/>
 * date: 2016年7月6日 下午5:52:01 <br/>
 *
 * @author ZHUANGWEILIANG1
 */
public class RedisManager {
    private static final Logger logger = LoggerFactory.getLogger(RedisManager.class);


    private String host = "127.0.0.1";

    private int port = 6379;

    // 0 - never expire
    private int expire = 0;

    // timeout for jedis try to connect to redis server, not expire time! In milliseconds
    private int timeout = 0;

    private String password = "";

    private JedisPoolConfig poolConfig = new JedisPoolConfig();

    private static JedisPool jedisPool = null;

    private boolean enableRedisLog = false;

    public RedisManager() {
    }

    /**
     * 初始化方法
     */
    public void init() {
        if (jedisPool == null) {
            if (password != null && !"".equals(password)) {
                jedisPool = new JedisPool(poolConfig, host, port, timeout, password);
            } else if (timeout != 0) {
                jedisPool = new JedisPool(poolConfig, host, port, timeout);
            } else {
                jedisPool = new JedisPool(poolConfig, host, port);
            }
        }
    }

    /**
     * 初始化方法
     */
    public Jedis getJedis() {
        if(jedisPool==null) {
            init();
        }
        return jedisPool.getResource();
    }

    /**
     * get value from redis
     *
     * @param key
     * @return
     */
    public byte[] get(byte[] key) {
        byte[] value = null;
        Jedis jedis = jedisPool.getResource();
        try {
            value = jedis.get(key);
        } finally {
            jedisPool.returnResource(jedis);
        }
        return value;
    }

    /**
     * set
     *
     * @param key
     * @param value
     * @return
     */
    public byte[] set(byte[] key, byte[] value) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.set(key, value);
            if (this.expire != 0) {
                jedis.expire(key, this.expire);
            }
        } finally {
            jedisPool.returnResource(jedis);
        }
        return value;
    }

    /**
     * set
     *
     * @param key
     * @param value
     * @param expire
     * @return
     */
    public byte[] set(byte[] key, byte[] value, int expire) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.set(key, value);
            if (expire != 0) {
                jedis.expire(key, expire);
            }
        } finally {
            jedisPool.returnResource(jedis);
        }
        return value;
    }

    /**
     * del
     *
     * @param key
     */
    public void del(byte[] key) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.del(key);
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    /**
     * flush
     */
    public void flushDB() {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.flushDB();
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    /**
     * size
     */
    public Long dbSize() {
        Long dbSize = 0L;
        Jedis jedis = jedisPool.getResource();
        try {
            dbSize = jedis.dbSize();
        } finally {
            jedisPool.returnResource(jedis);
        }
        return dbSize;
    }

    /**
     * keys
     *
     * @param pattern
     * @return
     */
    public Set<byte[]> keys(String pattern) {
        Set<byte[]> keys = null;
        Jedis jedis = jedisPool.getResource();
        try {
            keys = jedis.keys(pattern.getBytes());
        } finally {
            jedisPool.returnResource(jedis);
        }
        return keys;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the poolConfig.
     *
     * @return Returns the poolConfig
     */
    public JedisPoolConfig getPoolConfig() {
        return poolConfig;
    }

    /**
     * Sets the pool configuration for this factory.
     *
     * @param poolConfig The poolConfig to set.
     */
    public void setPoolConfig(JedisPoolConfig poolConfig) {
        this.poolConfig = poolConfig;
    }


    /**
     * redis 锁
     *
     * @param key        锁对象
     * @param value      内容
     * @param expireTime 锁超时时间
     * @return
     */
    public Long tryLock(String key, String value, int expireTime) {
        Jedis jedis = getJedis();
        try {
            Long result = jedis.setnx(key, value);
            if (result == 1) {
                if (expireTime > 0) {
                    try {
                        jedis.expire(key, expireTime);
                    }catch(Exception e){
                        logger.error("redis 并发锁设置超时时间出错,key【"+key+"】需要设置的超时时间【"+expireTime+"s】");
                        throw e;  //错误往外抛
                    }
                }
            }
            if (isEnableRedisLog() && logger.isInfoEnabled()) {
                logger.info(MessageFormat.format("Param[key={0},expiresTime={1,number,#},value={2}] Result[{3}]", key, expireTime, value, result));
            }
            return result;
        } catch (Exception e) {
            logException(e);
            return new Long(0);
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    /**
     * redis 锁
     *
     * @param key 锁对象
     * @return
     */
    public Long unLock(String key) {
        Jedis jedis = getJedis();
        try {
            Long result = jedis.del(key);
            if (isEnableRedisLog() && logger.isInfoEnabled()) {
                logger.info(MessageFormat.format("Param[key={0}] Result[{1}]", key, result));
            }
            return result;
        } catch (Exception e) {
            logException(e);
            return new Long(0);
        } finally {
            jedisPool.returnResource(jedis);
        }
    }


    /**
     * redis set方法
     *
     * @param key
     * @return
     */
    public String set(String key, String value, int expireTime) {
        Jedis jedis = getJedis();
        try {
            String result = jedis.set(key, value);
            if (expireTime > 0) {
                jedis.expire(key, expireTime);
            }
            if (isEnableRedisLog() && logger.isInfoEnabled()) {
                logger.info(MessageFormat.format("Param[key={0},expiresTime={1,number,#},value={2}] Result[{3}]", key, expireTime, value, result));
            }
            return result;
        } catch (Exception e) {
            logException(e);
            return "";
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    /**
     * get value from redis
     *
     * @param key
     * @return
     */
    public String get(String key) {
        Jedis jedis = getJedis();
        String result;
        try {
            result = jedis.get(key);
            if (isEnableRedisLog() && logger.isInfoEnabled()) {
                logger.info(MessageFormat.format("Param[key={0}] Result[{1}]", key, result));
            }
        } catch (Exception e) {
            logException(e);
            return "";
        } finally {
            jedisPool.returnResource(jedis);
        }
        return result;
    }

    /**
     * get value from redis
     *
     * @param key
     * @return
     */
    public Long del(String key) {
        Jedis jedis = getJedis();
        try {
            Long result = jedis.del(key);
            if (isEnableRedisLog() && logger.isInfoEnabled()) {
                logger.info(MessageFormat.format("Param[key={0}] Result[{1}]", key, result));
            }
            return result;
        } catch (Exception e) {
            logException(e);
            return new Long(0);
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    /**
     * redis set方法
     *
     * @param key
     * @return
     */
    public Long hset(String key, String field, String value, int expireTime) {
        Jedis jedis = getJedis();
        try {
            Long result = jedis.hset(key, field, value);
            if (expireTime > 0) {
                jedis.expire(key, expireTime);
            }
            if (isEnableRedisLog() && logger.isInfoEnabled()) {
                logger.info(MessageFormat.format("Param[key={0},expiresTime={1},field={2},value={3}] Result[{4}]", key, expireTime, field, value, result));
            }
            return result;
        } catch (Exception e) {
            logException(e);
            return new Long(0);
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    /**
     * redis set方法
     *
     * @param key
     * @return
     */
    public String hget(String key, String field) {
        Jedis jedis = getJedis();
        try {
            String result = jedis.hget(key, field);
            if (isEnableRedisLog() && logger.isInfoEnabled()) {
                logger.info(MessageFormat.format("Param[key={0},field={1}] Result[{2}]", key, field, result));
            }
            return result;
        } catch (Exception e) {
            logException(e);
            return "";
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    public String hmget(final String key, final String fields) {
        Jedis jedis = getJedis();
        try {
            String result = jedis.hmget(key, fields).get(0);
            if (isEnableRedisLog() && logger.isInfoEnabled()) {
                logger.info(MessageFormat.format("Param[key={0},field={1}] Result[{2}]", key, fields, result));
            }
            return result;
        } catch (Exception e) {
            logException(e);
            return "";
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    public Map<String, String> hgetAll(String key) {
        Jedis jedis = getJedis();
        try {
            Map<String, String> result = jedis.hgetAll(key);
            if (isEnableRedisLog() && logger.isInfoEnabled()) {
                logger.info(MessageFormat.format("Param[key={0}] Result[{1}]", key, result));
            }
            return result;
        } catch (Exception e) {
            logException(e);
            return null;
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    /**
     * redis原子性自增序列
     *
     * @param key
     * @return
     */
    public Long incr(String key) {
        Jedis jedis = getJedis();
        try {
            Long result = jedis.incr(key);
            if (isEnableRedisLog() && logger.isInfoEnabled()) {
                logger.info(MessageFormat.format("Param[key={0}] Result[{1}]", key, result));
            }
            return result;
        } catch (Exception e) {
            logException(e);
            throw e;
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    /**
     * redis原子性自增序列
     *
     * @param key
     * @return
     */
    public Long decr(String key) {
        Jedis jedis = getJedis();
        try {
            return jedis.decr(key);
        } catch (Exception e) {
            logException(e);
            throw e;
        } finally {
            jedisPool.returnResource(jedis);
        }
    }


    public boolean push(String key, String field) {
        Jedis jedis = getJedis();
        try {
            long[] returnNum = new long[1];
            returnNum[0] = jedis.lpush(key, field);
            if (isEnableRedisLog() && logger.isInfoEnabled()) {
                logger.info(MessageFormat.format("Param[key={0},field={1}] Result[long={2},result={3}]", key, field, returnNum[0], Boolean.TRUE));
            }
            return Boolean.TRUE;
        } catch (Exception e) {
            logException(e);
            throw e;
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    public String pop(String key) {
        Jedis jedis = getJedis();
        try {
            String result = jedis.rpop(key);
            if (isEnableRedisLog() && logger.isInfoEnabled()) {
                logger.info(MessageFormat.format("Param[key={0}] Result[{1}]", key, result));
            }
            return result;
        } catch (Exception e) {
            logException(e);
            throw e;
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    public boolean expire(String key, int expiresTime) {
        Jedis jedis = getJedis();
        boolean result = false;
        try {
            if (expiresTime > 0) {
                long l = jedis.expire(key, expiresTime);
                result = l > 0 ? Boolean.TRUE : Boolean.FALSE;
            } else {
                result = Boolean.FALSE;
            }
            if (isEnableRedisLog() && logger.isInfoEnabled()) {
                logger.info(MessageFormat.format("Param[key={0},expiresTime={1,number,#}] Result[{2}]", key, expiresTime, result));
            }
            return result;
        } catch (Exception e) {
            logException(e);
            throw e;
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    public Boolean exists(final String key) {
        Jedis jedis = getJedis();
        try {
            boolean result = jedis.exists(key);
            if (isEnableRedisLog() && logger.isInfoEnabled()) {
                logger.info(MessageFormat.format("Param[key={0}] Result[{1}]", key, result));
            }
            return result;
        } catch (Exception e) {
            logException(e);
            throw e;
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    private void logException(Exception e) {
        logger.error("redis error", e);
    }

    public boolean isEnableRedisLog() {
        return enableRedisLog;
    }

    public void setEnableRedisLog(boolean enableRedisLog) {
        this.enableRedisLog = enableRedisLog;
    }
}
