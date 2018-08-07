package com.kill.controller;

import com.kill.VO.GoodsVO;
import com.kill.VO.OrderDetailVO;
import com.kill.VO.ResultVO;
import com.kill.entity.OrderInfo;
import com.kill.enums.StatusCode;
import com.kill.redis.RedisServiceImpl;
import com.kill.service.GoodsService;
import com.kill.service.OrderService;
import com.kill.util.ResultUtil;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 订单控制器
 *
 * @author Administrator
 * @create 2018-08-01 21:17
 */
@Slf4j
@Api(tags ="订单控制器")
@Controller
@RequestMapping(value = "/order")
public class OrderInfoController {

    @Autowired
    RedisServiceImpl redisService;

    @Autowired
    OrderService orderService;

    @Autowired
    GoodsService goodsService;


    @ApiOperation(value="订单详情接口")
    @ApiImplicitParam(name = "orderId", value = "订单ID", required = true, dataType = "Long")
    @ApiResponses({
            @ApiResponse(code=301,message = "秒杀用户不存在")
    })
    @RequestMapping(value = "/detail")
    @ResponseBody
    public ResultVO<OrderInfo> detail(@RequestParam(value = "orderId") long orderId){
        OrderInfo orderInfo = orderService.getOrderById(orderId);
        if (orderInfo == null){
            return ResultUtil.error(StatusCode.KILL_ORDER_NOT_EXIST.getCode(),StatusCode.KILL_ORDER_NOT_EXIST.getMsg());
        }
        GoodsVO goodsVO = goodsService.getGoodsDetail(orderInfo.getGoodsId());
        OrderDetailVO detailVO = new OrderDetailVO();
        detailVO.setGoodsVO(goodsVO);
        detailVO.setOrderInfo(orderInfo);
        return ResultUtil.success(detailVO);
    }
}
