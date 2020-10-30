-- 调用脚本的传入的限流大小
local limit = tonumber(ARGV[1])
local timeout = tonumber(ARGV[2])

-- 获取该方法的流量大小，默认0
local count = tonumber(redis.call('get', KEYS[1]) or "0")

-- 是否超出限流阈值
if count + 1 > limit then
    -- 超过阈值
    return false
else
    -- 累加阈值
    redis.call("INCRBY", KEYS[1], 1)
    redis.call("PEXPIRE", KEYS[1], timeout)
    return true
end