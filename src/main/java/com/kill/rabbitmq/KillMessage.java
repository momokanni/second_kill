package com.kill.rabbitmq;

import com.kill.entity.KillUser;
import lombok.Data;

/**
 * 秒杀消息封装类
 *
 * @author Administrator
 * @create 2018-08-04 19:12
 */
@Data
public class KillMessage {

    private long goodsId;

    private KillUser user;
}
