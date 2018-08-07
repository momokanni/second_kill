package com.kill.redis;

/**
 * 真实且唯一的key
 */
public interface KeyPrefix {

    /**
     * 有效期
     * @return
     */
    Integer expireSeconds();

    /**
     * 前缀
     * @return
     */
    String getPrefix();
}
