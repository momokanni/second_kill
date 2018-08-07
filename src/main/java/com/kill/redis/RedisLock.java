package com.kill.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * redis分布式锁
 *
 * @author Administrator
 * @create 2018-08-06 0:27
 */
@Slf4j
@Component
public class RedisLock {

    @Autowired
    private RedisServiceImpl redisService;
    
    /**
     * @param: 
     * describe: redis lock
     * creat_user: sl
     * creat_date: 2018/8/6
     * creat_time: 1:08
     * 注：Long.parseLong(currentValue) < System.currentTimeMillis() == time out
     **/
    public boolean lock(KeyPrefix prefix,String key,String value){
        log.info("【redis lock】 init value:{},{}",redisService.realKey(prefix,key),value);
        boolean lockStatus = redisService.setnx(prefix,key,value);
        log.info("【redis lock】 status:{}",lockStatus);
        if (lockStatus){
            return true;
        }

        String currentValue = redisService.get(prefix,key,String.class);
        log.info("【redis lock】 currentValue:{}",currentValue);
        if (!StringUtils.isEmpty(currentValue) && Long.parseLong(currentValue) < System.currentTimeMillis()){
            //inset new value , return old value
            String oldValue = redisService.getSet(prefix,key,String.class);
            if(!StringUtils.isEmpty(oldValue) && oldValue.equals(currentValue)){
                return true;
            }
        }
        return false;
    }

    public void unLock(KeyPrefix prefix,String key,String value){
        try {
            String currentValue = redisService.get(prefix,key,String.class);
            if (!StringUtils.isEmpty(currentValue) && currentValue.equals(value)){
                redisService.delete(prefix,key);
            }
        } catch (Exception e){
            log.error("【redis unlock】 exception{}",e);
        }
    }
}
