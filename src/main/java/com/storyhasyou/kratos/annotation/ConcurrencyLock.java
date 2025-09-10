package com.storyhasyou.kratos.annotation;

import com.storyhasyou.kratos.handler.concurrencylock.ConcurrencyLockCallback;
import com.storyhasyou.kratos.handler.concurrencylock.DefaultConcurrencyLockCallback;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author fangxi created by 2023/10/18
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConcurrencyLock {

    String prefix() default "concurrency-lock:";

    String key() default "";

    int expireInSeconds() default 10;

    boolean throwException() default false;

    String code() default "";

    String msg() default "";

    /**
     * 抢不到锁的情况下，实现回调方法的实现类
     */
    Class<? extends ConcurrencyLockCallback> callbackClass() default DefaultConcurrencyLockCallback.class;

}
