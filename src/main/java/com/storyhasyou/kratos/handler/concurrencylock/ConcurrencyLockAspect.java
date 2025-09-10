package com.storyhasyou.kratos.handler.concurrencylock;

import com.storyhasyou.kratos.annotation.ConcurrencyLock;
import com.storyhasyou.kratos.exceptions.BusinessException;
import com.storyhasyou.kratos.utils.SpelUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author fangxi created by 2023/10/18
 */
@Aspect
@Slf4j
@RequiredArgsConstructor
public class ConcurrencyLockAspect {

    private static final int DEFAULT_VALUE = 1;

    private final RedisTemplate<String, Object> redisTemplate;
    private final List<ConcurrencyLockCallback> concurrencyLockCallbackList;

    @Around("@annotation(concurrencyLock)")
    public Object around(ProceedingJoinPoint joinPoint, ConcurrencyLock concurrencyLock) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        log.info("begin to aop " + signature.getMethod().getName());
        String key = SpelUtils.parse(joinPoint, concurrencyLock.key());
        if (key == null) {
            log.warn("concurrencyLock uniqueKey is null {}", concurrencyLock);
        }
        String uniqueKey = null != key ? concurrencyLock.prefix() + key : concurrencyLock.prefix() + concurrencyLock.key();
        int expire = concurrencyLock.expireInSeconds() > 0 ? concurrencyLock.expireInSeconds() : 10;
        Boolean success = redisTemplate.opsForValue().setIfAbsent(uniqueKey, DEFAULT_VALUE, expire, TimeUnit.SECONDS);
        if (Boolean.TRUE.equals(success)) {
            try {
                return joinPoint.proceed();
            } finally {
                redisTemplate.delete(uniqueKey);
            }
        } else {
            // 没有抢到并发锁
            log.warn("duplicate request with unique key {}, abort", uniqueKey);
            // 处理回调方法
            handleCallback(joinPoint, concurrencyLock);
            // 处理异常
            handleException(concurrencyLock);
        }
        return null;
    }


    private void handleCallback(JoinPoint joinPoint, ConcurrencyLock concurrencyLock) {
        Class<? extends ConcurrencyLockCallback> callbackClass = concurrencyLock.callbackClass();
        if (callbackClass == null) {
            return;
        }
        concurrencyLockCallbackList.stream()
                .filter(callback -> callback.getClass().equals(callbackClass))
                .findFirst()
                .ifPresent(callback -> callback.execute(joinPoint, concurrencyLock));

    }

    private void handleException(ConcurrencyLock concurrencyLock) {
        if (concurrencyLock.throwException()) {
            throw new BusinessException(concurrencyLock.msg());
        }
    }

}
