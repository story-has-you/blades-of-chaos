package com.storyhasyou.kratos.toolkit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;

/**
 * Redis加载Lua脚本
 *
 * @author fangxi
 */
@Configuration
public class RedisScriptConfig {

    @Bean
    public DefaultRedisScript<Boolean> redisScript() {
        DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<>();
        // 脚本位置
        redisScript.setLocation(new ClassPathResource("ratelimiter.lua"));
        // 脚本的返回值，这里返回 boolean
        redisScript.setResultType(Boolean.class);
        return redisScript;
    }

}
