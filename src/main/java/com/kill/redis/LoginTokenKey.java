package com.kill.redis;

import com.kill.constants.CookieConstant;

/**
 * tokenKey配置类
 *
 * @author Administrator
 * @create 2018-07-17 14:32
 */
public class LoginTokenKey extends BasePrefix {

    public LoginTokenKey(Integer expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static LoginTokenKey cookieKey = new LoginTokenKey(CookieConstant.EXPIRE,CookieConstant.TOKEN);
}
