package com.storyhasyou.kratos.handler.accesslimiter;

import com.storyhasyou.kratos.toolkit.LuaScriptConstant;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

/**
 * @author fangxi created by 2023/10/11
 */
@SpringBootConfiguration
@ConditionalOnClass(RedisOperations.class)
public class AccessLimiterConfig {

    @Bean
    public DefaultRedisScript<Boolean> redisScript() {
        DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<>();
        // 脚本位置
        redisScript.setScriptText(LuaScriptConstant.RATE_LIMITER);
        // 脚本的返回值，这里返回 boolean
        redisScript.setResultType(Boolean.class);
        return redisScript;
    }


    @Bean
    public AccessLimiterHandler accessLimiterHandler(RedisTemplate<String, Object> redisTemplate, DefaultRedisScript<Boolean> redisScript) {
        return new AccessLimiterHandler(redisTemplate, redisScript);
    }

    @Bean
    public AccessLimiterAspect accessLimiterAspect(AccessLimiterHandler accessLimiterHandler) {
        return new AccessLimiterAspect(accessLimiterHandler);
    }
}
