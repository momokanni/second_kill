package com.kill.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * redis工厂类
 *
 * @author Administrator
 * @create 2018-07-06 17:37
 */
@Service
public class RedisFactory {

    @Autowired
    private RedisConfig redisConfig;


    @Bean
    public JedisPool jedisPool(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(redisConfig.getMaxActive());
        config.setMaxIdle(redisConfig.getMaxIdle());
        config.setMaxWaitMillis(redisConfig.getMaxWait());

        JedisPool jp = new JedisPool(config,redisConfig.getHost(),
                redisConfig.getPort(),redisConfig.getTimeOut(),redisConfig.getPassword(),0);

        return jp;
    }
}
