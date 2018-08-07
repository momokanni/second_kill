package com.kill.VO;

import com.kill.entity.Goods;
import lombok.Data;

import java.util.Date;

/**
 * 商品表+秒杀商品表 非重复项整合起来,有利于一次查询
 *
 * @author Administrator
 * @create 2018-07-19 14:01
 */
@Data
public class GoodsVO extends Goods{

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
