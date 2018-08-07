package com.kill.VO;

import com.kill.entity.OrderInfo;
import lombok.Data;

/**
 * 订单明细视图对象类
 *
 * @author Administrator
 * @create 2018-08-01 21:23
 */
@Data
public class OrderDetailVO {

    private GoodsVO goodsVO;

    private OrderInfo orderInfo;
}
