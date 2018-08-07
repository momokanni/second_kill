package com.kill.context;

import com.kill.entity.KillUser;

/**
 * KillUser全局环境
 *
 * @author Administrator
 * @create 2018-08-07 11:19
 */
public class KillUserContext {

    public static ThreadLocal<KillUser> userHolder = new ThreadLocal<>();

    public static KillUser getUserHolder() {
        return userHolder.get();
    }

    public static void setUserHolder(KillUser user) {
        userHolder.set(user);
    }
}
