package com.kill.exception;

import com.kill.enums.StatusCode;
import lombok.Getter;

/**
 * 异常类
 *
 * @author Administrator
 * @create 2018-07-16 15:08
 */
@Getter
public class KillException extends RuntimeException {

    private Integer code;

    public KillException(StatusCode statusCode) {
        super(statusCode.getMsg());
        this.code = statusCode.getCode();
    }

    public KillException(Integer code,String msg) {
        super(msg);
        this.code = code;
    }
}
