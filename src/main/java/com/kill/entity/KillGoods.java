package com.kill.entity;

import lombok.Data;
import java.util.Date;

/**
 * 秒杀商品
 *
 * @author Administrator
 * @create 2018-07-18 16:40
 */
@Data
public class KillGoods {

    /**
     * 秒杀商品ID
     */
    private Long id;
    /**
     * 商品ID
     */
    private Long goodsId;
    /**
     * 秒杀价格
     */
    private Double killPrice;
    /**
     * 秒杀库存
     */
    private Integer stockCount;
    /**
     * 秒杀开始时间
     */
    private Date startDate;
    /**
     * 秒杀结束时间
     */
    private Date endDate;
}
