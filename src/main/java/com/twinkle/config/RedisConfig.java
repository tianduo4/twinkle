package com.twinkle.config;

import com.twinkle.common.cache.RedisTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * description: redis配置类,基于jedis实现
 *
 * @author ：King
 * @date ：2019/1/18 16:47
 */
@Slf4j
@Configuration
public class RedisConfig extends CachingConfigurerSupport {
    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.timeout}")
    private int timeout;

    @Value("${spring.redis.pool.max-active}")
    private int maxActive;

    @Value("${spring.redis.pool.max-idle}")
    private int maxIdle;

    @Value("${spring.redis.pool.min-idle}")
    private int minIdle;

    @Value("${spring.redis.pool.max-wait}")
    private long maxWaitMillis;

    @Value("${spring.redis.pool.test-on-borrow}")
    private boolean testOnBorrow;

    @Bean
    public JedisPool redisPoolFactory(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        jedisPoolConfig.setMaxTotal(maxActive);
        jedisPoolConfig.setMinIdle(minIdle);
        jedisPoolConfig.setTestOnBorrow(testOnBorrow);
        JedisPool jedisPool=null;
        if (password != null && !"".equals(password)) {
            jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, password);
        } else if (timeout != 0) {
            jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout);
        }
        return  jedisPool;
    }

    @Bean
    public RedisTemplate redisTemplate(JedisPool jedisPool) {
        RedisTemplate redisTemplate = new RedisTemplate(jedisPool);
        return redisTemplate;
    }
}
