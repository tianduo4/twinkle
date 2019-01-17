package com.twinkle.common.authentication.redis;

import com.twinkle.common.cache.RedisManager;
import com.twinkle.common.util.SerializationUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.util.CollectionUtils;

import java.util.*;

/**
 * description: shiro cache redis存储实现
 *
 * @author ：King
 * @date ：2019/1/12 23:28
 */
@Slf4j
public class RedisCache<K, V> implements Cache<K, V> {

    private RedisManager cache;

    /**
     * The Redis key prefix for the sessions
     */
    private String keyPrefix = "authc_cache_session:";

    /**
     * 通过一个JedisManager实例构造RedisCache
     */
    public RedisCache(RedisManager cache) {
        if (cache == null) {
            throw new IllegalArgumentException("Cache argument cannot be null.");
        }
        this.cache = cache;
    }

    /**
     * 获得byte[]型的key
     *
     * @param key
     * @return
     */
    private byte[] getByteKey(K key) {
        if (key instanceof String) {
            String preKey = this.keyPrefix + key;
            return preKey.getBytes();
        } else {
            return SerializationUtil.serialize(key);
        }
    }

    @Override
    public V get(K key) throws CacheException {
        log.info("根据key从Redis中获取对象。key [{}]", key);
        try {
            if (key == null) {
                return null;
            } else {
                byte[] rawValue = cache.get(getByteKey(key));
                @SuppressWarnings("unchecked")
                V value = (V) SerializationUtil.deserialize(rawValue);
                return value;
            }
        } catch (Throwable t) {
            throw new CacheException(t);
        }

    }

    @Override
    public V put(K key, V value) throws CacheException {
        log.info("根据key存储。 key [{}]", key);
        try {
            cache.set(getByteKey(key), SerializationUtil.serialize(value));
            return value;
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    @Override
    public V remove(K key) throws CacheException {
        log.info("从redis中删除。key [{}]", key);
        try {
            V previous = get(key);
            cache.del(getByteKey(key));
            return previous;
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    @Override
    public void clear() throws CacheException {
        log.debug("从redis中删除所有元素");
        try {
            cache.flushDB();
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    @Override
    public int size() {
        try {
            Long longSize = new Long(cache.dbSize());
            return longSize.intValue();
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<K> keys() {
        try {
            Set<byte[]> keys = cache.keys(this.keyPrefix + "*");
            if (CollectionUtils.isEmpty(keys)) {
                return Collections.emptySet();
            } else {
                Set<K> newKeys = new HashSet<K>();
                for (byte[] key : keys) {
                    newKeys.add((K) key);
                }
                return newKeys;
            }
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    @Override
    public Collection<V> values() {
        try {
            Set<byte[]> keys = cache.keys(this.keyPrefix + "*");
            if (!CollectionUtils.isEmpty(keys)) {
                List<V> values = new ArrayList<V>(keys.size());
                for (byte[] key : keys) {
                    @SuppressWarnings("unchecked")
                    V value = get((K) key);
                    if (value != null) {
                        values.add(value);
                    }
                }
                return Collections.unmodifiableList(values);
            } else {
                return Collections.emptyList();
            }
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

}
