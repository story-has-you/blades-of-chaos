package com.storyhasyou.kratos.toolkit;

/**
 * Lua脚本 For Redis 常量类
 *
 * @author fangxi
 *
 */
public interface LuaScriptConstant {

	/**
	 * 解锁
	 */
	String UN_LOCK = " if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

	/**
	 * setString
	 */
	String SET_STRING = " local setResult = redis.call('SET', KEYS[1], ARGV[1]); "
			+ "if tonumber(ARGV[2])  >= tonumber('0') then redis.call('EXPIRE', KEYS[1], ARGV[2]) end; return setResult ";

	/**
	 * setHash
	 */
	String SET_HASH = " local setResult =redis.call('HSET', KEYS[1], ARGV[1],ARGV[2]); "
			+ "if  tonumber(ARGV[3])  >= tonumber('0') then return redis.call('EXPIRE', KEYS[1], ARGV[3]) else return setResult end ";

	/**
	 * hash自增整型
	 */
	String SET_HASH_LONG_INCR = " local incrResult = redis.call('HINCRBY', KEYS[1], ARGV[1],ARGV[2]) ; "
			+ "if tonumber(ARGV[3])  >= tonumber('0')  then redis.call('EXPIRE', KEYS[1], ARGV[3]) end; return incrResult ";

	/**
	 * hash自增浮点型
	 */
	String SET_HASH_DOUBLE_INCR = " local incrResult = redis.call('HINCRBYFLOAT', KEYS[1], ARGV[1],ARGV[2]) ; "
			+ "if tonumber(ARGV[3])  >= tonumber('0') then redis.call('EXPIRE', KEYS[1], ARGV[3]) end; return incrResult ";

	/**
	 * setnx
	 */
	String SET_NX = " local setnx = redis.call('SETNX', KEYS[1], ARGV[1]) ; "
			+ "if tonumber(ARGV[2])  >= tonumber('0') then redis.call('EXPIRE', KEYS[1], ARGV[2]) end; return setnx ";

	/**
	 * [key,value]结构，value自增整型
	 */
	String SET_LONG_INCR = " local incrResult = redis.call('INCRBY', KEYS[1], ARGV[1]) ; "
			+ "if tonumber(ARGV[2])  >= tonumber('0') then redis.call('EXPIRE', KEYS[1], ARGV[2]) end; return incrResult ";

	/**
	 * [key,value]结构 ，value自增浮点型型
	 */
	String SET_DOUBLE_INCR = " local incrResult = redis.call('INCRBYFLOAT', KEYS[1], ARGV[1]) ; "
			+ "if tonumber(ARGV[2])  >= tonumber('0') then redis.call('EXPIRE', KEYS[1], ARGV[2]) end; return incrResult ";

	/**
	 * ADD GEO
	 */
	String GEO_ADD = " local addResult = redis.call('GEOADD', KEYS[1], ARGV[1], ARGV[2], ARGV[3]) ; "
			+ "if tonumber(ARGV[4])  >= tonumber('0') then redis.call('EXPIRE', KEYS[1], ARGV[4]) end; return addResult ";

	/**
	 * setStringWithRemainingExpire
	 */
	String SET_STRING_WITH_REMAINING_EXPIRE = " local ttl = redis.call('TTL', KEYS[1]); "
			+ " local setResult = redis.call('SET', KEYS[1], ARGV[1]);   "
			+ "if tonumber(ttl) > 0 then redis.call('EXPIRE', KEYS[1], ttl) end; return setResult ";

	/**
	 * 更新key 剩余过期时间
	 */
	String EXPIRE_WITH_REMAINING_EXPIRE = " local ttl = redis.call('TTL', KEYS[1]); "
			+ "if tonumber(ttl) > 0 then redis.call('EXPIRE', KEYS[1], tonumber(ttl)+tonumber(ARGV[1])) "
			+ "else redis.call('EXPIRE', KEYS[1], tonumber(ARGV[1])) end; return redis.call('TTL', KEYS[1]) ";

	/**
	 * 获取key 的value, 并移除此key
	 */
	String GET_AND_DELETE = "local result = redis.call('GET',KEYS[1]);  redis.call('DEL',KEYS[1]); return result;";
}