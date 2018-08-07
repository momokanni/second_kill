package com.kill.rabbitmq;

import com.kill.VO.GoodsVO;
import com.kill.entity.KillOrder;
import com.kill.entity.KillUser;
import com.kill.enums.StatusCode;
import com.kill.exception.KillException;
import com.kill.redis.RedisServiceImpl;
import com.kill.service.GoodsService;
import com.kill.service.KillService;
import com.kill.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * MQ接收者
 *
 * @author Administrator
 * @create 2018-08-02 22:17
 */
@Slf4j
@Service
public class MQReceiver {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private KillService killService;

    @RabbitListener(queues = MQConfig.DIRECT_QUEUE)
    public void receive(String message){
        log.info("receive message: "+ message);
    }

    @RabbitListener(queues = MQConfig.TOPIC_QUEUE)
    public void topicReceive(String message){
        log.info("receive topic message: "+message);
    }

    @RabbitListener(queues = MQConfig.TOPIC_QUEUE2)
    public void topicReceive2(String message){
        log.info("receive topic2 message: "+message);
    }

    @RabbitListener(queues = MQConfig.HEADER_QUEUE)
    public void headerReceive2(byte[]  message){
        log.info("receive header message: "+new String(message));
    }

    /**
     * 秒杀
     * @param message
     */
    @RabbitListener(queues = MQConfig.KILL_QUEUE)
    public void headerReceive2(String message){
        log.info("receive kill message: "+message);
        KillMessage killMessage = RedisServiceImpl.stringToBean(message,KillMessage.class);
        long goodsId = killMessage.getGoodsId();
        KillUser user = killMessage.getUser();

        GoodsVO goodsVO = goodsService.getGoodsDetail(goodsId);
        //判断秒杀时间
        long startDate = goodsVO.getStartDate().getTime();
        long endDate = goodsVO.getEndDate().getTime();
        long now = System.currentTimeMillis();
        if(now > startDate && now < endDate){ //秒杀进行中
            //判断库存
            int stockCount = goodsVO.getStockCount();
            if (stockCount <= 0){
                throw new KillException(StatusCode.GOODS_SELL_OUT.getCode(),StatusCode.GOODS_SELL_OUT.getMsg());
            }
            //避免同一个用户重复秒杀
            KillOrder killOrder = orderService.getKillOrderByUserIdGoodsId(goodsId,user.getId());
            if (!StringUtils.isEmpty(killOrder)){
                throw new KillException(StatusCode.KILL_ORDER_ALREADY_EXISTED.getCode(),StatusCode.KILL_ORDER_ALREADY_EXISTED.getMsg());
            }
            killService.secondKill(user,goodsVO);
        } else {
            log.info("秒杀无效请求,goodsId: " + goodsId);
            throw new KillException(StatusCode.GOODS_NO_SECOND_KILLING.getCode(),StatusCode.GOODS_NO_SECOND_KILLING.getMsg());
        }
    }
}
