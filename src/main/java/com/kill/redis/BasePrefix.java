package com.kill.redis;

/**
 * key 处理类
 *
 * @author Administrator
 * @create 2018-07-09 16:32
 */
public abstract class BasePrefix implements KeyPrefix {

    /**
     * 超时
     */
    private Integer expireSeconds;
    /**
     * 前缀
     */
    private String prefix;

    /**
     * 0代表永不过期
     * @param prefix
     */
    public BasePrefix(String prefix) {
        this(0,prefix);
        this.prefix = prefix;
    }

    public BasePrefix(Integer expireSeconds, String prefix) {
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }

    /**
     * @param: 
     * describe:
     * creat_user: sl
     * creat_date: 2018/7/9
     * creat_time: 16:34
     * 注：默认0代表永不过期
     **/
    @Override
    public Integer expireSeconds() {

        return expireSeconds;
    }

    /**
     * @param: 
     * describe: 
     * creat_user: sl
     * creat_date: 2018/7/10
     * creat_time: 15:45
     **/
    @Override
    public String getPrefix() {
        String className = getClass().getSimpleName();
        return className + ":" + prefix;
    }
}
