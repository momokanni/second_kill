package com.kill.redis;


/**
 * 用户模块key值设定
 *
 * @author Administrator
 * @create 2018-07-10 15:55
 */
public class KillUserKey extends BasePrefix{

    private KillUserKey(String prefix) {
        super(prefix);
    }

    public static KillUserKey getById = new KillUserKey("id");

    public static KillUserKey getByName = new KillUserKey("name");

}
