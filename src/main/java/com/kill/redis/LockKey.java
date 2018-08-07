package com.kill.redis;

/**
 * redis lock key
 *
 * @author Administrator
 * @create 2018-08-06 1:30
 */
public class LockKey extends BasePrefix{

    public LockKey(String prefix) {
        super(prefix);
    }

    public static LockKey lock_kill = new LockKey("kill");
}
