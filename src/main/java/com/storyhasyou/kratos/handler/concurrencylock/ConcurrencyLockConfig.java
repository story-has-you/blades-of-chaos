package com.storyhasyou.kratos.handler.concurrencylock;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

/**
 * @author fangxi created by 2023/10/25
 */
@RequiredArgsConstructor
@SpringBootConfiguration
@ConditionalOnClass(RedisOperations.class)
public class ConcurrencyLockConfig {

    private final List<ConcurrencyLockCallback> concurrencyLockCallbackList;

    @Bean
    public ConcurrencyLockAspect concurrencyLockAspect(RedisTemplate<String, Object> redisTemplate) {
        return new ConcurrencyLockAspect(redisTemplate, concurrencyLockCallbackList);
    }

}
