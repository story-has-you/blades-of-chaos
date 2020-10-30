package com.storyhasyou.kratos.toolkit;

import com.storyhasyou.kratos.annotation.AccessLimiter;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * The type Access limiter aspect.
 *
 * @author fangxi
 */
@Aspect
@Slf4j
@Component
public class AccessLimiterAspect {

    /**
     * The constant PREFIX.
     */
    public static final String PREFIX = "rate:limiter:";
    /**
     * The Access limiter handler.
     */
    @Autowired
    private AccessLimiterHandler accessLimiterHandler;

    /**
     * Before.
     *
     * @param joinPoint     the join point
     * @param accessLimiter the access limiter
     */
    @Before("@annotation(accessLimiter)")
    public void before(JoinPoint joinPoint, AccessLimiter accessLimiter) {
        String key = accessLimiter.methodKey();
        int limit = accessLimiter.limit();
        long timeout = accessLimiter.timeout();
        TimeUnit timeUnit = accessLimiter.unit();
        long millis = timeUnit.toMillis(timeout);
        if (StringUtils.isEmpty(key)) {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            String name = signature.getName();
            Class<?>[] parameterTypes = signature.getParameterTypes();
            key = name + Stream.of(parameterTypes).map(Class::getName).collect(Collectors.joining(",", "(", ")"));
        }
        accessLimiterHandler.limitAccess(PREFIX + key, limit, millis);
    }
}
