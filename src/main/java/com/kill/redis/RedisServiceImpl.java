package com.kill.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import com.alibaba.fastjson.*;
import redis.clients.jedis.JedisPool;

/**
 * redis业务层
 *
 * @author Administrator
 * @create 2018-07-06 11:03
 */
@Service
public class RedisServiceImpl {

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private RedisConfig redisConfig;

    /**
     * @param: 
     * describe: get 获取
     * creat_user: sl
     * creat_date: 2018/7/6
     * creat_time: 14:49
     **/
    public <T> T get(KeyPrefix prefix, String key, Class<T> clazz){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            //生成真正的key
            String realKey = realKey(prefix,key);
            String str = jedis.get(realKey);
            T t = stringToBean(str,clazz);
            return t;
        } finally {
            returnToPool(jedis);
        }
    }
    
    /**
     * @param:
     * describe: set 插入
     * creat_user: sl
     * creat_date: 2018/7/6
     * creat_time: 14:50
     **/
    public <T> boolean set(KeyPrefix prefix, String key, T value){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String val = beanTostring(value);
            if(val == null || val.length() <=0){
                return false;
            }
            String realKey = realKey(prefix,key);
            int seconds = prefix.expireSeconds();
            if(seconds <= 0){
                jedis.set(realKey,val);
            } else {
                jedis.setex(realKey,seconds,val);
            }
            return true;
        } finally {
            returnToPool(jedis);
        }
    }
    
    /**
     * @param: 
     * describe: 1 if the key was set 0 if the key was not set
     * creat_user: sl
     * creat_date: 2018/8/6
     * creat_time: 1:01
     **/
    public <T> boolean setnx(KeyPrefix prefix, String key, T value){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String val = beanTostring(value);
            if(val == null || val.length() <=0){
                return false;
            }
            String realKey = realKey(prefix,key);
            Long setnxResult = jedis.setnx(realKey,val);
            if (setnxResult == 1){
                return true;
            } else {
                return false;
            }
        } finally {
            returnToPool(jedis);
        }
    }
    
    /**
     * @param: KeyPrefix,Key,Value
     * describe: 设置指定 key 的值，并返回 key 的旧值
     * creat_user: sl
     * creat_date: 2018/7/11
     * creat_time: 15:30
     **/
    public <T> String getSet(KeyPrefix prefix, String key,T value){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String val = beanTostring(value);
            if(val == null || val.length() <=0){
                return null;
            }
            String realKey = realKey(prefix,key);
            return jedis.getSet(realKey,val);
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * @param: KeyPrefix, key
     * describe: 判断key是否存在
     * creat_user: sl
     * creat_date: 2018/7/11
     * creat_time: 15:16
     **/
    public <T> boolean exists(KeyPrefix prefix,String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = realKey(prefix,key);
            return jedis.exists(realKey);
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * @param: KeyPrefix, key
     * describe: 增加值
     * creat_user: sl
     * creat_date: 2018/7/11
     * creat_time: 15:18
     **/
    public <T> Long incr(KeyPrefix prefix, String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = realKey(prefix,key);
            return jedis.incr(realKey);
        } finally {
            returnToPool(jedis);
        }
    }
    
    /**
     * @param: KeyPrefix,key
     * describe: 减少值
     * creat_user: sl
     * creat_date: 2018/7/11
     * creat_time: 15:20
     **/
    public <T> Long decr(KeyPrefix prefix, String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = realKey(prefix,key);
            return jedis.decr(realKey);
        } finally {
            returnToPool(jedis);
        }
    }


    /**
     * @param:
     * describe: an integer greater than 0 if one or more keys were removed
     *         0 if none of the specified key existed
     * creat_user: sl
     * creat_date: 2018/8/6
     * creat_time: 1:01
     **/
    public <T> boolean delete(KeyPrefix prefix, String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = realKey(prefix,key);
            Long delResult = jedis.del(realKey);
            if (delResult > 0){
                return true;
            } else {
                return false;
            }
        } finally {
            returnToPool(jedis);
        }
    }

    
    /**
     * @param: key
     * describe: 
     * creat_user: sl
     * creat_date: 2018/7/6
     * creat_time: 11:43
     **/
    public static  <T> T stringToBean(String key,Class<T> clazz){
        if(key == null || key.length() <= 0 || clazz == null){
            return null;
        }
        if(clazz == int.class || clazz == Integer.class){
            return (T)Integer.valueOf(key);
        } else if(clazz == String.class){
            return (T)String.valueOf(key);
        } else if(clazz == long.class || clazz == Long.class){
            return (T)Long.valueOf(key);
        }else {
            return JSON.toJavaObject(JSON.parseObject(key),clazz);
        }
    }

    /**
     * @param: T 对象
     * describe: 看方法名
     * creat_user: sl
     * creat_date: 2018/7/6
     * creat_time: 11:50
     **/
    public static <T> String beanTostring(T value){
        if(value == null){
            return null;
        }

        Class<?> clazz = value.getClass();
        if(clazz == int.class || clazz == Integer.class){
            return "" + value;
        } else if(clazz == String.class){
            return (String)value;
        } else if(clazz == long.class || clazz == Long.class){
            return "" + value;
        }else {
            return JSON.toJSONString(value);
        }
    }

    /**
     * @param: Jedis
     * describe: 释放访问redis线程
     * creat_user: sl
     * creat_date: 2018/7/6
     * creat_time: 11:42
     **/
    private void returnToPool(Jedis jedis){
        if (jedis != null) {
            jedis.close();
        }
    }

    public String realKey(KeyPrefix prefix, String key){

        return prefix.getPrefix() + key;
    }

}
