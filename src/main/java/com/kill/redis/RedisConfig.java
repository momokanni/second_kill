package com.kill.redis;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Redis配置类
 *
 * @author Administrator
 * @create 2018-07-06 9:50
 */
@Data
@Component
@ConfigurationProperties(prefix = "redis")
public class RedisConfig {
    /**
     * redis ip
     */
    private String host;
    /**
     * redis 端口
     */
    private Integer port;
    /**
     * 超时设置
     */
    private Integer timeOut;
    /**
     * redis密码
     */
    private String password;
    /**
     * 最大连接数
     */
    private Integer maxActive;
    /**
     * 最大闲置数
     */
    private Integer maxIdle;
    /**
     * 最大等待数
     */
    private Integer maxWait;
}
