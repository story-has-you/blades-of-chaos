package com.storyhasyou.kratos.ratelimiter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * @author fangxi
 *  限流，拦截用户请求
 */
@Slf4j
@Component
public class AccessLimiterHandler {

    @Autowired
    private StringRedisTemplate redisTemplate;
    /**
     * lua脚本
     */
    @Autowired
    private RedisScript<Boolean> rateLimitLua;

    /**
     * @param key 方法
     * @param limit 限流个数，默认每秒的限流个数
     */
    public void limitAccess(String key, Integer limit, Long pexpire) {
        // 执行Lua脚本, Collections.singletonList(key) lua脚本中的key
        boolean acquired = redisTemplate.execute(rateLimitLua, Collections.singletonList(key), limit.toString(), pexpire.toString());
        if (!acquired) {
            // 被拦截了
            log.error("Your access is blocked, key: {}", key);
            throw new RuntimeException("Your access is blocked");
        }
    }

}