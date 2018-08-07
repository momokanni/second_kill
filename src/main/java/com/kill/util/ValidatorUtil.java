package com.kill.util;

import org.springframework.util.StringUtils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 验证工具类
 *
 * @author Administrator
 * @create 2018-07-16 15:09
 */
public class ValidatorUtil {

    private static final Pattern mobile_pattern = Pattern.compile("1\\d{10}");
    
    /**
     * @param: mobileNum(手机号)
     * describe: 验证用户手机号是否符合规则
     * creat_user: sl
     * creat_date: 2018/7/16
     * creat_time: 15:13
     **/
    public static boolean isMobile(String mobileNum){

        if(StringUtils.isEmpty(mobileNum)){
            return false;
        }
        Matcher matcher = mobile_pattern.matcher(mobileNum);

        return matcher.matches();
    }

    public static void main(String[] args){
        System.out.println("~~~~~~~~~~~~~~~~~~ClassName = ValidatorUtil ,METHOD_NAME = main , isMobile = "+isMobile("18615204581"));
    }
}
