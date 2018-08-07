package com.kill.util;

import org.springframework.util.DigestUtils;

/**
 * MD5加密
 *
 * @author Administrator
 * @create 2018-07-13 15:45
 */
public class MD5Util {

    private static final String salt = "3#5&9c7d";

    public static String md5(String src){

        return DigestUtils.md5DigestAsHex(src.getBytes());
    }


    public static String inputPassToFormPass(String inputPass) {
        String str = ""+salt.charAt(0)+salt.charAt(2) + inputPass +salt.charAt(5) + salt.charAt(4);
        System.out.println(str);
        return md5(str);
    }

    /**
     * @param: 登录密码
     * describe: 第一次加密：登录密码(已在客户端加密)转后台
     * creat_user: sl
     * creat_date: 2018/7/13
     * creat_time: 16:13
     **/
    public static String inputToBack(String inputPwd){
        String str = salt.charAt(0) + salt.charAt(3) + salt.charAt(4) + inputPwd + salt.charAt(7) + salt.charAt(6);
        return md5(str);
    }
    
    /**
     * @param: 第一次加密值
     * describe: 第二次加密：在第一次加密的基础上再加密
     * creat_user: sl
     * creat_date: 2018/7/13
     * creat_time: 16:21
     **/
    public static String backToDB(String backPwd,String salt){
        String str = salt.charAt(2) + salt.charAt(5) + salt.charAt(7) + backPwd + salt.charAt(3) + salt.charAt(6);
        return md5(str);
    }

    public static void main(String[] args){
        String md5Pwd = inputToBack("123456");
        System.out.println("~~~~~~ClassName = MD5Util ,METHOD_NAME = main , md5 = "+backToDB(md5Pwd,"%B8V/MKQ"));
    }
}
