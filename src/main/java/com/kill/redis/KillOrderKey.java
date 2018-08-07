package com.kill.redis;


/**
 * 秒杀订单key
 *
 * @author Administrator
 * @create 2018-08-04 20:07
 */
public class KillOrderKey extends BasePrefix{

    public KillOrderKey(Integer expireSeconds,String prefix) {

        super(expireSeconds,prefix);
    }

    public static KillOrderKey killOrderKey = new KillOrderKey(3600,"killOrder");

    public static KillOrderKey stockCountKey = new KillOrderKey(3600,"goods_sell_out");
}
