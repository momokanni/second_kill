package com.kill.entity;

import lombok.Data;

/**
 * 商品表
 *
 * @author Administrator
 * @create 2018-07-18 16:39
 */
@Data
public class Goods {

    /**
     * 商品ID
     */
    private Long id;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 商品标题
     */
    private String goodsTitle;
    /**
     * 商品图片
     */
    private String goodsImg;
    /**
     * 商品详情
     */
    private String goodsDetail;
    /**
     * 商品价格
     */
    private Double goodsPrice;
    /**
     * 商品库存
     */
    private Integer goodsStock;
}
