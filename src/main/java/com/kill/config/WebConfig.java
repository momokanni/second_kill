package com.kill.config;

import com.kill.interceptors.AccessLimitInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Web请求配置类
 *
 * @author Administrator
 * @create 2018-07-17 22:09
 * 注：
 *     springBoot2.0 及 spring5.0已将WebMvcConfigureAdapter废弃
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private KillUserArgumentResolver killUserArgumentResolver;

    @Autowired
    private AccessLimitInterceptor accessLimitInterceptor;


    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(killUserArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(accessLimitInterceptor);
    }
}
