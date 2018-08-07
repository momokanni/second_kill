package com.kill.handler;

import com.kill.VO.ResultVO;
import com.kill.constants.Constants;
import com.kill.enums.StatusCode;
import com.kill.exception.KillAuthorizeException;
import com.kill.exception.KillException;
import com.kill.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.BindingException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * 异常捕获类
 *
 * @author Administrator
 * @create 2018-07-16 20:10
 */
@Slf4j
@ControllerAdvice
public class ExceptionHandle {

    /**
     * 授权异常
     * @return
     */
    @ExceptionHandler(value = KillAuthorizeException.class)
    public ModelAndView handlerAuthException(){

        return new ModelAndView("redirect:".concat(Constants.LOGIN_URL));
    }

    /**
     * 其他异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ResultVO handle(Exception e){
        if (e instanceof KillException){
            KillException killException = (KillException) e;
            return ResultUtil.error(killException.getCode(),killException.getMessage());
        } else if(e instanceof BindException || e instanceof BindingException){
            BindException bindException = (BindException) e;
            List<ObjectError> errors = bindException.getAllErrors();
            ObjectError error = errors.get(0);
            return ResultUtil.error(StatusCode.PARAM_BIND_ERROR.getCode(),error.getDefaultMessage());
        }
        e.printStackTrace();
        log.error("未知错误",e.toString());
        return ResultUtil.error(StatusCode.UNKONW.getCode(),StatusCode.UNKONW.getMsg());
    }
}
