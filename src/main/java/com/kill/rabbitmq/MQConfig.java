package com.kill.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * MQ配置类
 *
 * @author Administrator
 * @create 2018-08-02 22:18
 */
@Configuration
public class MQConfig {

    /**
     * Queue
     */
    public static final String DIRECT_QUEUE = "queue";
    public static final String TOPIC_QUEUE = "topic_queue";
    public static final String TOPIC_QUEUE2 = "topic_queue2";
    public static final String HEADER_QUEUE = "header_queue";
    public static final String KILL_QUEUE = "kill_queue";

    /**
     * Exchange
     */
    public static final String TOPIC_EXCHANGE = "topic_exchange";
    public static final String FANOUT_EXCHANGE = "fanout_exchange";
    public static final String Header_EXCHANGE = "header_exchange";


    /**
     * Routing Key
     */
    public static final String ROUTING_KEY = "topic.key";
    public static final String ROUTING_KEY2 = "topic.#"; //#通配符，可匹配0~多个单次

    /**
     * Direct模式
     * @param
     */
    @Bean
    public Queue directQueue(){
        return new Queue(DIRECT_QUEUE,true);
    }

    /**
     * Topic模式
     * @param
     */
    @Bean
    public Queue topicQueue(){
        return new Queue(TOPIC_QUEUE,true);
    }

    /**
     * Topic模式
     * @param
     */
    @Bean
    public Queue topicQueue2(){
        return new Queue(TOPIC_QUEUE2,true);
    }

    /**
     * topic交换机
     * @return
     */
    @Bean
    public TopicExchange topicExchange(){

        return new TopicExchange(TOPIC_EXCHANGE);
    }

    @Bean
    public Binding topicBinding(){
        return BindingBuilder.bind(topicQueue()).to(topicExchange()).with(ROUTING_KEY);
    }

    @Bean
    public Binding topicBinding2(){
        return BindingBuilder.bind(topicQueue2()).to(topicExchange()).with(ROUTING_KEY2);
    }


    /**
     * Fanout(广播)模式
     */
    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange(FANOUT_EXCHANGE);
    }

    @Bean
    public Binding faoutBinding(){
        return BindingBuilder.bind(topicQueue()).to(fanoutExchange());
    }

    @Bean
    public Binding fanoutBinding2(){
        return BindingBuilder.bind(topicQueue2()).to(fanoutExchange());
    }

    @Bean
    public Queue headerQueue(){
        return new Queue(HEADER_QUEUE);
    }
    /**
     * header
     * @return
     */
    @Bean
    public HeadersExchange headersExchange(){
        return new HeadersExchange(Header_EXCHANGE);
    }

    @Bean
    public Binding headerBinding(){
        Map<String,Object> map = new HashMap<>();
        map.put("header","header_msg");
        map.put("header2","header_msg2");
        return BindingBuilder.bind(headerQueue()).to(headersExchange()).whereAll(map).match();
    }

    /**
     * 秒杀队列
     * @return
     */
    @Bean
    public Queue killQueue(){
        return new Queue(KILL_QUEUE);
    }

    @Bean
    public Binding topicKillBinding(){
        return BindingBuilder.bind(killQueue()).to(topicExchange()).with(ROUTING_KEY);
    }

}
