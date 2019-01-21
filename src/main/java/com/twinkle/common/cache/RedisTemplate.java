package com.twinkle.common.cache;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.text.MessageFormat;
import java.util.Map;

/**
 * description: Redis 操作方法
 *
 * @author ：King
 * @date ：2019/1/19 0:26
 */
@Slf4j
public class RedisTemplate {
    private JedisPool jedisPool;
    private boolean enableRedisLog = true;

    public RedisTemplate(JedisPool jedisPool) {
        if (jedisPool == null) {
            throw new IllegalArgumentException("jedis pool illegal");
        }
        this.jedisPool = jedisPool;
    }

    private Jedis getJedis() {
        return jedisPool.getResource();
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

            if (enableRedisLog) {
                log.info(MessageFormat.format("Param[key={0},expiresTime={1,number,#}] Result[{2}]", key, expiresTime, result));
            }
            return result;
        } catch (Exception e) {
            log.error("redis error", e);
            throw e;
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    public boolean exists(final String key) {
        Jedis jedis = getJedis();
        try {
            boolean result = jedis.exists(key);
            if (enableRedisLog) {
                log.info(MessageFormat.format("Param[key={0}] Result[{1}]", key, result));
            }
            return result;
        } catch (Exception e) {
            log.error("redis error", e);
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
    public Long incr(String key) {
        Jedis jedis = getJedis();
        try {
            Long result = jedis.incr(key);
            if (enableRedisLog) {
                log.info(MessageFormat.format("Param[key={0}] Result[{1}]", key, result));
            }
            return result;
        } catch (Exception e) {
            log.error("redis error", e);
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
            Long result = jedis.decr(key);
            if (enableRedisLog) {
                log.info(MessageFormat.format("Param[key={0}] Result[{1}]", key, result));
            }
            return result;
        } catch (Exception e) {
            log.error("redis error", e);
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
            if (enableRedisLog) {
                log.info(MessageFormat.format("Param[key={0},field={1}] Result[long={2},result={3}]", key, field, returnNum[0], Boolean.TRUE));
            }
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("redis error", e);
            throw e;
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    public String pop(String key) {
        Jedis jedis = getJedis();
        try {
            String result = jedis.rpop(key);
            if (enableRedisLog) {
                log.info(MessageFormat.format("Param[key={0}] Result[{1}]", key, result));
            }
            return result;
        } catch (Exception e) {
            log.error("redis error", e);
            throw e;
        } finally {
            jedisPool.returnResource(jedis);
        }
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
                        log.error("redis 并发锁设置超时时间出错,key【"+key+"】需要设置的超时时间【"+expireTime+"s】");
                        throw e;  //错误往外抛
                    }
                }
            }
            if (enableRedisLog) {
                log.info(MessageFormat.format("Param[key={0},expiresTime={1,number,#},value={2}] Result[{3}]", key, expireTime, value, result));
            }
            return result;
        } catch (Exception e) {
            log.error("redis error", e);
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
            if (enableRedisLog) {
                log.info(MessageFormat.format("Param[key={0}] Result[{1}]", key, result));
            }
            return result;
        } catch (Exception e) {
            log.error("redis error", e);
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
            if (enableRedisLog) {
                log.info(MessageFormat.format("Param[key={0},expiresTime={1,number,#},value={2}] Result[{3}]", key, expireTime, value, result));
            }
            return result;
        } catch (Exception e) {
            log.error("redis error", e);
            return "";
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    public String get(String key) {
        Jedis jedis = getJedis();
        String result;
        try {
            result = jedis.get(key);
            if (enableRedisLog) {
                log.info(MessageFormat.format("Param[key={0}] Result[{1}]", key, result));
            }
        } catch (Exception e) {
            log.error("redis error", e);
            return "";
        } finally {
            jedisPool.returnResource(jedis);
        }
        return result;
    }

    public Long del(String key) {
        Jedis jedis = getJedis();
        try {
            Long result = jedis.del(key);
            if (enableRedisLog) {
                log.info(MessageFormat.format("Param[key={0}] Result[{1}]", key, result));
            }
            return result;
        } catch (Exception e) {
            log.error("redis error", e);
            return new Long(0);
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    public Long hset(String key, String field, String value, int expireTime) {
        Jedis jedis = getJedis();
        try {
            Long result = jedis.hset(key, field, value);
            if (expireTime > 0) {
                jedis.expire(key, expireTime);
            }
            if (enableRedisLog) {
                log.info(MessageFormat.format("Param[key={0},expiresTime={1},field={2},value={3}] Result[{4}]", key, expireTime, field, value, result));
            }
            return result;
        } catch (Exception e) {
            log.error("redis error", e);
            return new Long(0);
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    public String hget(String key, String field) {
        Jedis jedis = getJedis();
        try {
            String result = jedis.hget(key, field);
            if (enableRedisLog) {
                log.info(MessageFormat.format("Param[key={0},field={1}] Result[{2}]", key, field, result));
            }
            return result;
        } catch (Exception e) {
            log.error("redis error", e);
            return "";
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    public String hmget(final String key, final String fields) {
        Jedis jedis = getJedis();
        try {
            String result = jedis.hmget(key, fields).get(0);
            if (enableRedisLog) {
                log.info(MessageFormat.format("Param[key={0},field={1}] Result[{2}]", key, fields, result));
            }
            return result;
        } catch (Exception e) {
            log.error("redis error", e);
            return "";
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    public Map<String, String> hgetAll(String key) {
        Jedis jedis = getJedis();
        try {
            Map<String, String> result = jedis.hgetAll(key);
            if (enableRedisLog) {
                log.info(MessageFormat.format("Param[key={0}] Result[{1}]", key, result));
            }
            return result;
        } catch (Exception e) {
            log.error("redis error", e);
            return null;
        } finally {
            jedisPool.returnResource(jedis);
        }
    }
}
