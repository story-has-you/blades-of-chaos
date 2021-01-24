package com.storyhasyou.kratos.utils;

import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.storyhasyou.kratos.toolkit.StringPool;
import com.storyhasyou.kratos.toolkit.DatePattern;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The type Identity utils.
 *
 * @author fangxi
 * @date 2020 /3/5
 */
public class IdUtils {

    /**
     * 毫秒格式化时间
     */
    public static final DateTimeFormatter MILLISECOND = DatePattern.PURE_DATETIME_MS_FORMAT;
    /**
     * 主机和进程的机器码
     */
    private static final IdentifierGenerator IDENTIFIER_GENERATOR = new DefaultIdentifierGenerator();

    /**
     * 获取唯一ID
     *
     * @return id id
     */
    public static long getId() {
        return getId(new Object());
    }

    /**
     * 获取唯一ID
     *
     * @param entity the entity
     * @return id id
     */
    public static long getId(Object entity) {
        return IDENTIFIER_GENERATOR.nextId(entity).longValue();
    }

    /**
     * 获取唯一ID
     *
     * @return id id str
     */
    public static String getIdStr() {
        return getIdStr(new Object());
    }

    /**
     * 获取唯一ID
     *
     * @param entity the entity
     * @return id id str
     */
    public static String getIdStr(Object entity) {
        return IDENTIFIER_GENERATOR.nextId(entity).toString();
    }

    /**
     * 格式化的毫秒时间
     *
     * @return the millisecond
     */
    public static String getMillisecond() {
        return DateUtils.now().format(MILLISECOND);
    }

    /**
     * 时间 ID = Time + ID
     * <p>例如：可用于商品订单 ID</p>
     *
     * @return the time id
     */
    public static String getTimeId() {
        return getMillisecond() + getIdStr();
    }

    /**
     * 使用ThreadLocalRandom获取UUID获取更优的效果 去掉"-"
     *
     * @return the string
     */
    public static String uuid() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return new UUID(random.nextLong(), random.nextLong()).toString().replace(StringPool.DASH, StringPool.EMPTY);
    }
}
