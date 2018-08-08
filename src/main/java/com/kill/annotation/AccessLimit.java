package com.kill.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * class_name: AccessLimit
 * package: com.kill.annotation
 * describe: 限制访问次数注解 和 拦截器：AccessLimitInterceptor 配合使用
 * creat_user: sl
 * creat_date: 2018/8/8
 * creat_time: 11:50
 **/
@Target({METHOD})
@Retention(RUNTIME)
public @interface AccessLimit {

    int seconds();
    int maxCount();
    boolean needLogin() default true;
}
