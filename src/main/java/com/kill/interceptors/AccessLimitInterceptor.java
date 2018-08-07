package com.kill.interceptors;

import com.alibaba.fastjson.JSON;
import com.kill.annotation.AccessLimit;
import com.kill.constants.CookieConstant;
import com.kill.context.KillUserContext;
import com.kill.entity.KillUser;
import com.kill.enums.StatusCode;
import com.kill.redis.AccessKey;
import com.kill.redis.RedisServiceImpl;
import com.kill.service.KillUserService;
import com.kill.util.CookieUtil;
import com.kill.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Access Limit 拦截器
 *
 * @author Administrator
 * @create 2018-08-07 11:14
 */
@Component
public class AccessLimitInterceptor  extends HandlerInterceptorAdapter{

    @Autowired
    private KillUserService killUserService;

    @Autowired
    private RedisServiceImpl redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod){
            KillUser user = getUser(request,response);
            KillUserContext.setUserHolder(user);

            HandlerMethod method = (HandlerMethod) handler;
            AccessLimit accessLimit = method.getMethodAnnotation(AccessLimit.class);
            if (accessLimit == null){
                return true;
            }

            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();
            boolean needLogin = accessLimit.needLogin();

            String uri = request.getRequestURI();
            String key = null;
            if (needLogin){
                if (user == null){
                    render(response, StatusCode.LOGIN_NOT_LOGGED_IN);
                    return false;
                }
                key = uri + "_" +user.getId();
            } else {
                //
            }


            AccessKey ack = AccessKey.accessExpire(seconds);
            Integer access_count = redisService.get(ack,key,Integer.class);
            if (access_count == null){
                redisService.set(ack,key,1);
            } else if(access_count < maxCount){
                redisService.incr(ack,key);
            } else {
                render(response,StatusCode.ACCESS_LIMIT__OVERRUN);
                return false;
            }
        }
        return true;
    }

    private void render(HttpServletResponse response, StatusCode statusCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        String msg = JSON.toJSONString(ResultUtil.error(statusCode.getCode(),statusCode.getMsg()));
        out.write(msg.getBytes());
        out.flush();
        out.close();
    }

    private KillUser getUser(HttpServletRequest request, HttpServletResponse response) {

        String token = CookieUtil.getCookieValue(request, CookieConstant.TOKEN);
        KillUser user = killUserService.getByToken(response,token);
        return user;
    }
}
