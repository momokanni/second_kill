package com.kill.entity;

import lombok.Data;
import java.util.Date;

/**
 * 订单表
 *
 * @author Administrator
 * @create 2018-07-18 16:42
 */
@Data
public class OrderInfo {

    /**
     * 订单ID
     */
    private Long id;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 商品ID
     */
    private Long goodsId;
    /**
     * 收货地址ID
     */
    private Long  deliveryAddrId;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 订单商品数量
     */
    private Integer goodsCount;
    /**
     * 订单商品单价
     */
    private Double goodsPrice;
    /**
     * 下单方式
     */
    private Integer orderChannel;
    /**
     * 订单状态
     */
    private Integer status;
    /**
     * 订单创建时间
     */
    private Date createDate;
    /**
     * 订单支付时间
     */
    private Date payDate;
}
