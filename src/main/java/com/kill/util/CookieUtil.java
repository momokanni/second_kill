package com.kill.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Cookie生成工具类
 *
 * @author Administrator
 * @create 2018-07-17 11:40
 */
public class CookieUtil {

    /**
     * @param: 
     * describe: 将cookie插入响应中
     * creat_user: sl
     * creat_date: 2018/7/17
     * creat_time: 21:47
     **/
    public static void set(HttpServletResponse response,
                           String name,
                           String value,
                           int maxAge){
        Cookie cookie = new Cookie(name,value);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);

    }
    
    /**
     * @param: 
     * describe: 从请求中获取cookie
     * creat_user: sl
     * creat_date: 2018/7/17
     * creat_time: 21:55
     **/
    public static Cookie get(HttpServletRequest request,String name){
        Map<String,Cookie> map = readCookieMap(request);
        if (map.containsKey(name)){
            return map.get(name);
        } else {
            return null;
        }
    }
    
    /**
     * @param: 
     * describe: 获取cookie value值
     * creat_user: sl
     * creat_date: 2018/7/24
     * creat_time: 17:21
     **/
    public static String getCookieValue(HttpServletRequest request,String name){
        Map<String,Cookie> map = readCookieMap(request);
        if (map.containsKey(name)){
            return map.get(name).getValue();
        } else {
            return null;
        }
    }

    private static Map<String,Cookie> readCookieMap(HttpServletRequest request){
        Map<String,Cookie> map = new HashMap<>();
        Cookie[] cookies = request.getCookies();
        if (cookies != null){
            for (Cookie cookie: cookies) {
                map.put(cookie.getName(),cookie);
            }
        }
        return map;
    }
}
