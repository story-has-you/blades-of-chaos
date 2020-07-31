-- 获取方法签名特征
local methodKey = KEYS[1]

-- 调用脚本的传入的限流大小
local limit = tonumber(ARGV[1])
local timeout = tonumber(ARGV[2])

-- 获取该方法的流量大小，默认0
local count = tonumber(redis.call('get', methodKey) or "0")

-- 是否超出限流阈值
if count + 1 > limit then
    -- 超过阈值
    return false
else
    -- 累加阈值
    redis.call("INCRBY", methodKey, 1)
    redis.call("PEXPIRE", methodKey, timeout)
    return true
end