package com.kill.util;

import com.kill.VO.ResultVO;
import com.kill.enums.StatusCode;

/**
 * 响应封装类的工具包
 *
 * @author Administrator
 * @create 2018-07-02 11:50
 */
public class ResultUtil {

    public ResultUtil(){};

    public static ResultVO success(Object obj){
        ResultVO result = new ResultVO();
        result.setCode(StatusCode.SUCCESS.getCode());
        result.setMsg(StatusCode.SUCCESS.getMsg());
        result.setData(obj);
        return result;
    }

    public static ResultVO success(){
        return success(null);
    }

    public static ResultVO error(Integer code,String msg){
        ResultVO result = new ResultVO();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(null);
        return result;
    }

    public static ResultVO waitResponse(Integer code,String msg){
        ResultVO result = new ResultVO();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(null);
        return result;
    }

}
