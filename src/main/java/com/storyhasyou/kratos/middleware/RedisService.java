package com.storyhasyou.kratos.middleware;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Sets;
import com.storyhasyou.kratos.utils.BeanUtils;
import com.storyhasyou.kratos.utils.CollectionUtils;
import com.storyhasyou.kratos.utils.IdUtils;
import com.storyhasyou.kratos.utils.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
     * @param timeout  the timeout
     * @param timeUnit the time unit
     * @return the boolean
     */
    public boolean tryLock(String key, long timeout, TimeUnit timeUnit) {
        return this.tryLock(key, IdUtils.getId(), timeout, timeUnit);
    }

    /**
     * Try lock boolean.
     *
     * @param key      the key
     * @param value    the value
     * @param duration the duration
     * @return the boolean
     */
    public boolean tryLock(String key,Object value, Duration duration) {
        Boolean tryLock = redisTemplate.opsForValue().setIfAbsent(key, value, duration);
        return Optional.ofNullable(tryLock).orElse(false);
    }

    /**
     * Try lock boolean.
     *
     * @param key      the key
     * @param duration the duration
     * @return the boolean
     */
    public boolean tryLock(String key, Duration duration) {
        return tryLock(key, IdUtils.getId(), duration);
    }

    /**
     * Un lock boolean.
     *
     * @param key the key
     * @return the boolean
     */
    public boolean unLock(String key) {
        try {
            Long result = redisTemplate.delete(Collections.singletonList(key));
            return Optional.ofNullable(result).map(r -> r.equals(1L)).orElse(false);
        } catch (Exception e) {
            log.error("unLock fail", e);
            return false;
        }
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
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
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
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     */
    public void set(Object key, Object value) {
        try {
            redisTemplate.opsForValue().set(key.toString(), value.toString());
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
                redisTemplate.opsForValue().set(key, value.toString(), time,
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
                redisTemplate.opsForValue().set(key, value.toString(), time, timeUnit);
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
                redisTemplate.opsForValue().set(key, value.toString(), timeout);
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
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
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
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        Long increment = redisTemplate.opsForValue().increment(key, -delta);
        return Optional.ofNullable(increment).orElseThrow(() -> new RuntimeException("decr fail"));
    }

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值 object
     */
    public Object hget(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    //============================String end =============================

    //============================Map start =============================

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值 map
     */
    public Map<Object, Object> hmget(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public boolean hmset(String key, Map<String, String> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            log.error("", e);
            return false;
        }
    }

    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public boolean hmset(String key, Map<String, String> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("", e);
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            log.error("", e);
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒)  注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("", e);
            return false;
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public void hdel(String key, String item) {
        redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     * @return the long
     */
    public long hincr(String key, String item, long by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于0)
     * @return the long
     */
    public long hdecr(String key, String item, long by) {
        return redisTemplate.opsForHash().increment(key, item, -by);
    }

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return the set
     */
    public Set<Object> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            log.error("", e);
            return null;
        }
    }

    /**
     * 根据key获取Set中的所有值
     *
     * @param <T>   the type parameter
     * @param key   键
     * @param clazz the clazz
     * @return the set
     */
    public <T> Set<T> sGet(String key, Class<T> clazz) {
        try {
            Set<Object> members = redisTemplate.opsForSet().members(key);
            if (CollectionUtils.isNotEmpty(members)) {
                return Sets.newHashSet(BeanUtils.copyProperties(members, clazz));
            }
            return Collections.emptySet();
        } catch (Exception e) {
            log.error("", e);
            return null;
        }
    }
    //============================map end =============================

    //============================set start =============================

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public boolean sHasKey(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     */
    public void sSet(String key, String values) {
        try {
            redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            log.error("", e);
        }
    }

    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @param time   时间(秒)
     */
    public void sSetAndTime(String key, String values, long time) {
        try {
            redisTemplate.opsForSet().add(key, values);
            if (time > 0) {
                expire(key, time);
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return the long
     */
    public long sGetSetSize(String key) {
        try {
            Long size = redisTemplate.opsForSet().size(key);
            return Optional.ofNullable(size).orElse(-1L);
        } catch (Exception e) {
            log.error("", e);
            return 0;
        }
    }

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值
     * @return 移除的个数 remove
     */
    public boolean setRemove(String key, String... values) {
        try {
            if (values.length > 0) {
                for (String value : values) {
                    redisTemplate.opsForSet().remove(key, value);
                }
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("", e);
            return false;
        }
    }

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束  0 到 -1代表所有值
     * @return the list
     */
    public List<Object> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            log.error("", e);
            return null;
        }
    }


    /**
     * 获取list缓存的内容
     *
     * @param <T>   the type parameter
     * @param key   键
     * @param start 开始
     * @param end   结束  0 到 -1代表所有值
     * @param clazz the clazz
     * @return the list
     */
    public <T> List<T> lGet(String key, long start, long end, Class<T> clazz) {
        try {
            List<Object> range = redisTemplate.opsForList().range(key, start, end);
            if (CollectionUtils.isNotEmpty(range)) {
                return BeanUtils.copyProperties(range, clazz);
            }
            return Collections.emptyList();
        } catch (Exception e) {
            log.error("", e);
            return null;
        }
    }
    //===============================list=================================

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return the long
     */
    public long lGetListSize(String key) {
        try {
            Long size = redisTemplate.opsForList().size(key);
            return Optional.ofNullable(size).orElse(-1L);
        } catch (Exception e) {
            log.error("", e);
            return 0;
        }
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引  index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return the object
     */
    public Object lGetIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            log.error("", e);
            return null;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return the boolean
     */
    public boolean lSet(String key, String value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            log.error("", e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return the boolean
     */
    public boolean lSet(String key, String value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("", e);
            return false;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return the boolean
     */
    public boolean lUpdateIndex(String key, long index, String value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            log.error("", e);
            return false;
        }
    }

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数 long
     */
    public long lRemove(String key, long count, Object value) {
        try {
            Long remove = redisTemplate.opsForList().remove(key, count, value);
            return Optional.ofNullable(remove).orElse(-1L);
        } catch (Exception e) {
            log.error("", e);
            return 0;
        }
    }

    /**
     * 获取容量
     *
     * @return the int
     */
    public long size() {
        Long execute = redisTemplate.execute(RedisServerCommands::dbSize);
        return Optional.ofNullable(execute).orElse(-1L);
    }
    
}
