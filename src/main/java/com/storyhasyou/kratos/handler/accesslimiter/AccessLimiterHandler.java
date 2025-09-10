package com.storyhasyou.kratos.handler.accesslimiter;

import com.storyhasyou.kratos.exceptions.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.List;

/**
 * @author fangxi
 * 限流，拦截用户请求
 */
@Slf4j
@RequiredArgsConstructor
public class AccessLimiterHandler {

    private final RedisTemplate<String, Object> redisTemplate;
    /**
     * lua脚本
     */
    private final RedisScript<Boolean> rateLimitLua;

    /**
     * @param key   方法
     * @param limit 限流个数，默认每秒的限流个数
     */
    public void limitAccess(String key, Integer limit, Long timeout) {
        // 执行Lua脚本, Collections.singletonList(key) lua脚本中的key
        Boolean acquired = redisTemplate.execute(rateLimitLua, StringRedisSerializer.UTF_8, new BooleanRedisSerializer(), List.of(key), limit.toString(), timeout.toString());
        if (Boolean.FALSE.equals(acquired)) {
            // 被拦截了
            log.error("Your access is blocked, key: {}", key);
            throw new BusinessException("Your access is blocked");
        }
    }


    private static class BooleanRedisSerializer implements RedisSerializer<Boolean> {

        @Override
        public byte[] serialize(Boolean value) throws SerializationException {
            return value ? new byte[]{1} : new byte[]{0};
        }

        @Override
        public Boolean deserialize(byte[] bytes) throws SerializationException {
            if (bytes == null || bytes.length == 0) {
                return false;
            }
            return bytes[0] == 1;
        }
    }

}
