package com.kill.VO;

import lombok.Data;

import java.io.Serializable;

/**
 * 统一响应转换类
 *
 * @author Administrator
 * @create 2018-07-02 10:53
 * 响应格式：
 * {
 *     "code" : 200,
 *     "msg" : "XXX",
 *     "data" : {}
 * }
 */
@Data
public class ResultVO<T> implements Serializable {

    /**
     * Ctrl + Shift + Alt + U
     */
    private static final long serialVersionUID = 2022920359652970137L;

    //响应码
    private Integer code;
    //响应消息
    private String msg;
    //响应数据
    private T data;
}
