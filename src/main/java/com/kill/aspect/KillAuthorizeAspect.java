package com.kill.aspect;

import com.kill.constants.CookieConstant;
import com.kill.entity.KillUser;
import com.kill.enums.StatusCode;
import com.kill.exception.KillAuthorizeException;
import com.kill.exception.KillException;
import com.kill.redis.LoginTokenKey;
import com.kill.redis.RedisServiceImpl;
import com.kill.util.CookieUtil;
import com.kill.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;


/**
 * 秒杀登录切面
 *
 * @author Administrator
 * @create 2018-07-20 14:28
 */
@Slf4j
@Aspect
@Component
public class KillAuthorizeAspect {

    @Autowired
    private RedisServiceImpl redisService;

    @Pointcut("execution( public * com.kill.controller.Kill*.*(..))")
    public void verify(){};

    @Before("verify()")
    public void doVerify(){
        ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        Cookie cookie = CookieUtil.get(request, CookieConstant.TOKEN);
        if(cookie == null){
            log.warn("【登录校验】cookie为空");
            String accept = request.getHeader("accept");
            if (accept.contains("/json")){
                throw new KillException(StatusCode.LOGIN_NOT_LOGGED_IN.getCode(),StatusCode.LOGIN_NOT_LOGGED_IN.getMsg());
            } else {
                throw new KillAuthorizeException();
            }
        }
        KillUser user = redisService.get(LoginTokenKey.cookieKey,cookie.getValue(),KillUser.class);
        if(StringUtils.isEmpty(user)){
            log.warn("【登录校验】Redis中查不到token");
            throw new KillException(StatusCode.LOGIN_NOT_LOGGED_IN.getCode(),StatusCode.LOGIN_NOT_LOGGED_IN.getMsg());
        }

    }
}
