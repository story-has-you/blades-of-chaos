package com.storyhasyou.kratos.handler.accesslimiter;

import com.storyhasyou.kratos.annotation.AccessLimiter;
import com.storyhasyou.kratos.utils.SpelUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author fangxi
 */
@Aspect
@Slf4j
@RequiredArgsConstructor
public class AccessLimiterAspect {

    private final AccessLimiterHandler accessLimiterHandler;
    public static final String PREFIX = "rate:limiter:";

    @Before("@annotation(accessLimiter)")
    public void before(JoinPoint joinPoint, AccessLimiter accessLimiter) {
        String key = SpelUtils.parse(joinPoint, accessLimiter.methodKey());
        int limit = accessLimiter.limit();
        TimeUnit timeUnit = accessLimiter.unit();
        long timeout = timeUnit.toMillis(accessLimiter.timeout());
        if (StringUtils.isBlank(key)) {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            String name = signature.getName();
            Class<?>[] parameterTypes = signature.getParameterTypes();
            key = name + Stream.of(parameterTypes).map(Class::getName).collect(Collectors.joining(",", "(", ")"));
        }
        accessLimiterHandler.limitAccess(PREFIX + key, limit, timeout);
    }


}
