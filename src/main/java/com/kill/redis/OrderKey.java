package com.kill.redis;

/**
 * 订单key
 *
 * @author Administrator
 * @create 2018-08-02 14:31
 */
public class OrderKey extends BasePrefix{

    public OrderKey(Integer expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static OrderKey orderInfo = new OrderKey(3600,"orderInfo");
}
