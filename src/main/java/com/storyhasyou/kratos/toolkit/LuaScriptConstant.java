package com.storyhasyou.kratos.toolkit;

/**
 * Lua脚本 For Redis 常量类
 *
 * @author fangxi
 */
public class LuaScriptConstant {

    /**
     * 获取锁
     */
    public static final String TRY_LOCK = """
            if(redis.call('exists', KEYS[1]) == 0) then redis.call('hset', KEYS[1], ARGV[1], '1'); redis.call('expire', KEYS[1], ARGV[2]); return 1; end;
            if(redis.call('hexists', KEYS[1], ARGV[1]) == 1) then redis.call('hincrby', KEYS[1], ARGV[1], '1'); redis.call('expire', KEYS[1], ARGV[2]); return 1; end;
            return 0;
            """;

    /**
     * 解锁
     */
    public static final String UN_LOCK = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

    /**
     * lua限流
     */
    public static final String RATE_LIMITER = """
            local methodKey = KEYS[1]
            local limit = tonumber(ARGV[1])
            local timeout = tonumber(ARGV[2])
            local count = tonumber(redis.call("GET", methodKey) or "0")
            if count + 1 > limit then
                return false
            else
                redis.call("INCRBY", methodKey, 1)
                redis.call("PEXPIRE", methodKey, timeout)
                return true
            end
            """;

}
