package com.kill.enums;

import lombok.Getter;

/**
 * class_name: StatusCode
 * package: com.kill.enums
 * describe: 状态返回码
 * creat_user: sl
 * creat_date: 2018/7/2
 * creat_time: 11:56
 **/
@Getter
public enum StatusCode {

    /**
     * system errors
     */
    SUCCESS(200,"成功"),
    FAILED(201,"失败"),
    UNKONW(202,"未知错误"),
    PARAM_ERROR(203,"参数不正确"),

    /**
     * request 211 ~ 220
     */
    REQUEST_ILLEGAL(211,"非法请求"),

    /**
     * redis lock 290 ~ 300
     */
    LOCK_STATUS_FAILED(290,"人从众,稍后再试"),

    /**
     * login 301 ~ 309
     */
    KILL_USER_NOT_EXISTS(301,"秒杀用户不存在"),
    LOGIN_PWD_ERROR(302,"登录密码错误"),
    LOGIN_SUCCESS(303,"登录成功"),
    LOGIN_FAILED(304,"登录失败"),
    LOGIN_NOT_LOGGED_IN(305,"未登录"),

    /**
     * param Bind 310 ~ 319
     */
    PARAM_BIND_ERROR(310,"参数绑定异常"),

    /**
     * 商品状态码 320 ~ 349
     */
    GOODS_NOT_EXISTS(320,"商品不存在"),
    GOODS_SELL_OUT(321,"该商品已售罄"),
    GOODS_NO_SECOND_KILLING(322,"该商品暂无秒杀活动"),
    GOODS_DETAIL_ERROR(323,"商品详情异常"),

    /**
     * 秒杀状态 350 ~ 360
     */
    KILL_ORDER_ALREADY_EXISTED(350,"不能重复秒杀"),
    KILL_ORDER_NOT_EXIST(351,"该秒杀订单不存在"),
    KILL_ORDER_SUCCESS_WAIT(352,"排队中"),
    KILL_GOODS_FAILED(353,"秒杀失败"),
    KILL_ORDER_SUCCESS(354,"秒杀成功"),
    KILL_PATH_FAILED(355,"无效链接"),

    /**
     * verify code 370 ~ 375
     */
    VERIFYCODE_CREATE_ERROR(370,"获取验证码失败"),
    VERIFYCODE_INPUT_VALUE_ERROR(371,"数学是体育老师教的吗？"),

    /**
     * Access LIMIT
     */
    ACCESS_LIMIT__OVERRUN(372,"请求太频繁，稍后重试")



    ;

    private Integer code;

    private String msg;

    StatusCode(Integer code, String msg){
        this.code = code;
        this.msg = msg;
    }

 }
