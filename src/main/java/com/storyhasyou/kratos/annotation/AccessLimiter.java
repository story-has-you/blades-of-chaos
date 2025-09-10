package com.storyhasyou.kratos.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @author fangxi
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AccessLimiter {

    /**
     * 限流方法,redis中的key。默认是方法签名
     */
    String methodKey() default "";

    /**
     * 单位时间内允许的请求
     */
    int limit() default 10;

    /**
     * 时间单位
     */
    TimeUnit unit() default TimeUnit.SECONDS;

    /**
     * 时间
     */
    long timeout() default 1;

}
