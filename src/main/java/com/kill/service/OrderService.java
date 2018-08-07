package com.kill.service;

import com.kill.VO.GoodsVO;
import com.kill.entity.KillOrder;
import com.kill.entity.KillUser;
import com.kill.entity.OrderInfo;

public interface OrderService {

    KillOrder createOrder(KillUser user, GoodsVO goodsVO);

    OrderInfo getOrderById(long orderId);

    KillOrder getKillOrderByUserIdGoodsId(Long goodsId, long userId);
}
