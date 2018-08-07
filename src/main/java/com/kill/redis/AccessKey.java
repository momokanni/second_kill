package com.kill.redis;

/**
 * 请求key
 *
 * @author Administrator
 * @create 2018-08-07 10:20
 */
public class AccessKey extends BasePrefix {

    public AccessKey(Integer expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static AccessKey accessExpire(Integer seconds){
        return new AccessKey(seconds,"access_limit");
    }
}
