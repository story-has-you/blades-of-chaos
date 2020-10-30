package com.storyhasyou.kratos.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 限流注解
 *
 * @author fangxi
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AccessLimiter {

    /**
     * 限流方法,redis中的key。默认是方法签名
     *
     * @return the string
     */
    String methodKey() default "";

    /**
     * 单位时间内允许的请求
     *
     * @return the int
     */
    int limit() default 10;

    /**
     * 时间单位
     *
     * @return the time unit
     */
    TimeUnit unit() default TimeUnit.SECONDS;

    /**
     * 时间
     *
     * @return the long
     */
    long timeout() default 1L;

}
