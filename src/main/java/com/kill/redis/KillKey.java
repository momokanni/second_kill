package com.kill.redis;

/**
 * 秒杀key
 *
 * @author Administrator
 * @create 2018-08-06 21:37
 */
public class KillKey extends BasePrefix {

    public KillKey(Integer expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static KillKey killKey = new KillKey(60,"verifyCode");
}
