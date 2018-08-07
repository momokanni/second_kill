package com.kill.entity;

import lombok.Data;

/**
 * 秒杀订单
 *
 * @author Administrator
 * @create 2018-07-18 16:42
 */
@Data
public class KillOrder {

    /**
     * 秒杀订单ID
     */
    private Long id;
    /**
     * 秒杀用户ID
     */
    private Long userId;
    /**
     * 秒杀订单ID
     */
    private Long  orderId;
    /**
     * 秒杀商品ID
     */
    private Long goodsId;
}
