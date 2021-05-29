package com.storyhasyou.kratos.toolkit;

/**
 * Lua脚本 For Redis 常量类
 *
 * @author fangxi
 *
 */
public interface LuaScriptConstant {

	/**
	 * 获取锁
	 */
	String TRY_LOCK = "if(redis.call('exists', KEYS[1]) == 0) then redis.call('hset', KEYS[1], ARGV[1], '1'); redis.call('expire', KEYS[1], ARGV[2]); return 1; end;" +
			"if(redis.call('hexists', KEYS[1], ARGV[1]) == 1) then redis.call('hincrby', KEYS[1], ARGV[1], '1'); redis.call('expire', KEYS[1], ARGV[2]); return 1; end;" +
			"return 0;";

	/**
	 * 解锁
	 */
	String UN_LOCK = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

}