package com.kill.rabbitmq;

import com.kill.redis.RedisServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * MQ发送者
 *
 * @author Administrator
 * @create 2018-08-02 22:17
 */
@Slf4j
@Service
public class MQSender {

    @Autowired
    AmqpTemplate amqpTemplate;

    public void send(Object msg){
        String message = RedisServiceImpl.beanTostring(msg);
        log.info("send message: "+ message );
        amqpTemplate.convertAndSend(MQConfig.DIRECT_QUEUE,message);


    }

    public void topicSend(Object msg){
        String message = RedisServiceImpl.beanTostring(msg);
        log.info("send topic message: " + message);

        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE,MQConfig.ROUTING_KEY,message + "_1");

        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE,MQConfig.ROUTING_KEY2,message + "_2");

        amqpTemplate.convertAndSend(MQConfig.FANOUT_EXCHANGE,"",message + "_3");


    }

    public void headerSend(Object msg){
        String message = RedisServiceImpl.beanTostring(msg);
        log.info("send header message: "+ message );

        MessageProperties mp = new MessageProperties();
        mp.setHeader("header","header_msg");
        mp.setHeader("header2","header_msg2");
        Message header_msg = new Message("header_msg".getBytes(),mp);
        amqpTemplate.convertAndSend(MQConfig.Header_EXCHANGE,"",header_msg);
    }

    /**
     * 秒杀入队
     * @param killMessage
     */
    public void sendKillMsg(KillMessage killMessage) {
        log.info("userId: "+ killMessage.getUser().getId() + "秒杀goodsId: "+ killMessage.getGoodsId() + "入队");
        String message = RedisServiceImpl.beanTostring(killMessage);
        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE,MQConfig.ROUTING_KEY,message);
    }
}
