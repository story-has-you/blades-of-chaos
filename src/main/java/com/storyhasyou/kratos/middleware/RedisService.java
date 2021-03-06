package com.storyhasyou.kratos.middleware;

import com.fasterxml.jackson.core.type.TypeReference;
import com.storyhasyou.kratos.toolkit.LuaScriptConstant;
import com.storyhasyou.kratos.utils.BeanUtils;
import com.storyhasyou.kratos.utils.CollectionUtils;
import com.storyhasyou.kratos.utils.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StreamOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.util.Assert;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * The type Redis service.
 *
 * @author fangxi
 */
@Slf4j
public class RedisService {

    /**
     * The String redis template.
     */
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 开启Redis 事务
     */
    public void begin() {
        // 开启Redis 事务权限
        redisTemplate.setEnableTransactionSupport(true);
        // 开启事务
        redisTemplate.multi();
    }

    /**
     * 提交事务
     */
    public void commit() {
        // 成功提交事务
        redisTemplate.exec();
    }

    /**
     * 回滚Redis 事务
     */
    public void discard() {
        redisTemplate.discard();
    }


    /**
     * Try lock boolean.
     *
     * @param key      the key
     * @param value    the value
     * @param timeout  the timeout
     * @param timeUnit the time unit
     * @return the boolean
     */
    public boolean tryLock(String key, Object value, long timeout, TimeUnit timeUnit) {
        Boolean tryLock = redisTemplate.opsForValue().setIfAbsent(key, value, timeout, timeUnit);
        return Optional.ofNullable(tryLock).orElse(false);
    }

    /**
     * Try lock boolean.
     *
     * @param key      the key
     * @param value    the value
     * @param duration the duration
     * @return the boolean
     */
    public boolean tryLock(String key, Object value, Duration duration) {
        Boolean tryLock = redisTemplate.opsForValue().setIfAbsent(key, value, duration);
        return Optional.ofNullable(tryLock).orElse(false);
    }

    /**
     * Un lock boolean.
     *
     * @param key the key
     * @return the boolean
     */
    public boolean releaseLock(String key, String value) {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(LuaScriptConstant.UN_LOCK, Long.class);
        Long execute = redisTemplate.execute(redisScript, Collections.singletonList(key), value);
        return Optional.ofNullable(execute).map(e -> e == 1L).orElse(false);
    }


    /**
     * Multi get list.
     *
     * @param <T>  the type parameter
     * @param keys the keys
     * @param type the type
     * @return the list
     */
    public <T> List<T> multiGet(Collection<String> keys, Class<T> type) {
        List<Object> values = redisTemplate.opsForValue().multiGet(keys);
        if (org.springframework.util.CollectionUtils.isEmpty(values)) {
            return Collections.emptyList();
        }
        return BeanUtils.copyProperties(values, type);
    }


    /**
     * Multi get list.
     *
     * @param <T>  the type parameter
     * @param keys the keys
     * @param type the type
     * @return the list
     */
    public <T> List<T> multiGet(Collection<String> keys, TypeReference<T> type) {
        List<Object> values = redisTemplate.opsForValue().multiGet(keys);
        if (org.springframework.util.CollectionUtils.isEmpty(values)) {
            return Collections.emptyList();
        }
        return values.stream().map(value -> JacksonUtils.convertValue(value, type)).collect(Collectors.toList());
    }

    /**
     * 普通缓存获取
     *
     * @param <T>  the type parameter
     * @param key  键
     * @param type the type
     * @return 值 t
     */
    public <T> T get(String key, TypeReference<T> type) {
        Object value = redisTemplate.opsForValue().get(key);
        if (Objects.isNull(value)) {
            return null;
        }
        return JacksonUtils.convertValue(value, type);
    }


    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return the boolean
     */
    public boolean expire(String key, long time) {

        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            log.error("", e);
        }
        return false;
    }


    // ==============================common start =============================

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String key) {
        Long expire = redisTemplate.getExpire(key);
        return Optional.ofNullable(expire).orElse(-1L);
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        try {
            Boolean hasKey = redisTemplate.hasKey(key);
            return Optional.ofNullable(hasKey).orElse(false);
        } catch (Exception e) {
            log.error("", e);
            return false;
        }
    }

    /**
     * 删除缓存
     *
     * @param keys the keys
     */
    public void remove(Collection<String> keys) {
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    /**
     * 删除缓存
     *
     * @param key the key
     */
    public void remove(String key) {
        redisTemplate.delete(key);
    }

    /**
     * Remove.
     *
     * @param key the key
     */
    public void remove(Object key) {
        redisTemplate.delete(key.toString());
    }

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值 string
     */
    public <T> T get(String key) {
        return get(key, new TypeReference<T>() {
        });
    }


    // ==============================common end =============================

    //============================String start =============================

    /**
     * Get t.
     *
     * @param <T>   the type parameter
     * @param key   the key
     * @param clazz the clazz
     * @return the t
     */
    public <T> T get(String key, Class<T> clazz) {
        Object value = redisTemplate.opsForValue().get(key);
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        return JacksonUtils.convertValue(value, clazz);
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     */
    public void set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value.toString());
        } catch (Exception e) {
            log.error("", e);
        }
    }


    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     */
    public void set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time,
                        TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }

    /**
     * 普通缓存放入并设置时间, 并设置时间单位
     *
     * @param key      键
     * @param value    值
     * @param time     时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @param timeUnit the time unit
     */
    public void set(String key, Object value, long time, TimeUnit timeUnit) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, timeUnit);
            } else {
                set(key, value);
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }

    /**
     * 普通缓存放入并设置时间, 并设置时间单位
     *
     * @param key     键
     * @param value   值
     * @param timeout the timeout
     */
    public void set(String key, Object value, Duration timeout) {
        try {
            if (timeout != null) {
                redisTemplate.opsForValue().set(key, value, timeout);
            } else {
                set(key, value);
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }

    /**
     * Keys set.
     *
     * @param prefix the prefix
     * @return the set
     */
    public Set<String> keys(String prefix) {
        Set<String> keys = redisTemplate.keys(prefix);
        return CollectionUtils.isEmpty(keys) ? Collections.emptySet() : keys;
    }

    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return the long
     */
    public long incr(String key, long delta) {
        Assert.isTrue(delta > 0, "递增因子必须大于0");
        Long increment = redisTemplate.opsForValue().increment(key, delta);
        return Optional.ofNullable(increment).orElseThrow(() -> new RuntimeException("incr fail"));
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return long long
     */
    public long decr(String key, long delta) {
        Assert.isTrue(delta > 0, "递增因子必须大于0");
        Long increment = redisTemplate.opsForValue().increment(key, -delta);
        return Optional.ofNullable(increment).orElseThrow(() -> new RuntimeException("decr fail"));
    }

    public RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }

    public ValueOperations<String, Object> opsForValue() {
        return redisTemplate.opsForValue();
    }

    public <HK, HV> HashOperations<String, HK, HV> opsForHash() {
        return redisTemplate.opsForHash();
    }

    public GeoOperations<String, Object> opsForGeo() {
        return redisTemplate.opsForGeo();
    }

    public ListOperations<String, Object> opsForList() {
        return redisTemplate.opsForList();
    }

    public SetOperations<String, Object> opsForSet() {
        return redisTemplate.opsForSet();
    }

    public <HK, HV> StreamOperations<String, HK, HV> opsForStream() {
        return redisTemplate.opsForStream();
    }

    public ZSetOperations<String, Object> opsForZSet() {
        return redisTemplate.opsForZSet();
    }

}
