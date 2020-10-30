package com.storyhasyou.kratos.toolkit;

import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

/**
 * The type Access limiter handler.
 *
 * @author fangxi 限流，拦截用户请求
 */
@Slf4j
@Component
public class AccessLimiterHandler {

    /**
     * The Redis template.
     */
    @Autowired
    private StringRedisTemplate redisTemplate;
    /**
     * lua脚本
     */
    @Autowired
    private RedisScript<Boolean> rateLimitLua;

    /**
     * Limit access.
     *
     * @param key     方法
     * @param limit   限流个数，默认每秒的限流个数
     * @param millis the millis
     */
    public void limitAccess(String key, Integer limit, Long millis) {
        // 执行Lua脚本, Collections.singletonList(key) lua脚本中的key
        boolean acquired = redisTemplate.execute(rateLimitLua, Collections.singletonList(key), limit.toString(), millis.toString());
        if (!acquired) {
            // 被拦截了
            log.error("Your access is blocked, key: {}", key);
            throw new RuntimeException("操作频繁，请稍候重试");
        }
    }
}
