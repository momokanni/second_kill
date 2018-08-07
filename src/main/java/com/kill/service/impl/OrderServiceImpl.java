package com.kill.service.impl;

import com.kill.VO.GoodsVO;
import com.kill.entity.KillOrder;
import com.kill.entity.KillUser;
import com.kill.entity.OrderInfo;
import com.kill.entity.mapper.KillOrderMapper;
import com.kill.entity.mapper.OrderMapper;
import com.kill.redis.KillOrderKey;
import com.kill.redis.OrderKey;
import com.kill.redis.RedisServiceImpl;
import com.kill.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 订单业务层
 *
 * @author Administrator
 * @create 2018-07-20 16:57
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private RedisServiceImpl redisService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private KillOrderMapper killOrderMapper;

    @Transactional
    @Override
    public KillOrder createOrder(KillUser user, GoodsVO goodsVO) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goodsVO.getId());
        orderInfo.setGoodsName(goodsVO.getGoodsName());
        orderInfo.setGoodsPrice(goodsVO.getKillPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(user.getId());
        orderMapper.createOrder(orderInfo);

        //插入秒杀order
        KillOrder killOrder = new KillOrder();
        killOrder.setGoodsId(goodsVO.getId());
        killOrder.setUserId(user.getId());
        killOrder.setOrderId(orderInfo.getId());
        killOrderMapper.createKillOrder(killOrder);

        redisService.set(KillOrderKey.killOrderKey,"_" + killOrder.getUserId() + "_" + killOrder.getGoodsId(),killOrder);
        return killOrder;
    }

    @Override
    public KillOrder getKillOrderByUserIdGoodsId(Long goodsId, long userId) {
        return redisService.get(KillOrderKey.killOrderKey,"_" + userId + "_" + goodsId, KillOrder.class);
    }

    @Override
    public OrderInfo getOrderById(long orderId) {

        return orderMapper.getOrderDetailById(orderId);
    }
}
