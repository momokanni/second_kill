package com.kill.config;

import com.kill.constants.CookieConstant;
import com.kill.context.KillUserContext;
import com.kill.entity.KillUser;
import com.kill.service.KillUserService;
import com.kill.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 秒杀用户参数转化类
 *
 * @author Administrator
 * @create 2018-07-17 21:05
 */
@Service
public class KillUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private KillUserService killUserService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        //获取参数类型
        Class<?> clazz = parameter.getParameterType();

        return clazz == KillUser.class;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        return KillUserContext.getUserHolder();
    }
}
