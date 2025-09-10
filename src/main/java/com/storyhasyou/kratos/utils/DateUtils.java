package com.storyhasyou.kratos.utils;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.GregorianCalendar;

/**
 * The type Date utils.
 *
 * @author fangxi
 */
public class DateUtils extends LocalDateTimeUtil {


    private DateUtils() {

    }

    /**
     * Now string.
     *
     * @return the string
     */
    public static String nowStr() {
        return format(now(), DatePattern.NORM_DATETIME_PATTERN);
    }


    /**
     * Current date time string.
     *
     * @return the string
     */
    public static String currentDateTime() {
        return now().format(DatePattern.NORM_DATETIME_FORMATTER);
    }

    /**
     * Current date string.
     *
     * @return the string
     */
    public static String today() {
        return format(LocalDate.now(), DatePattern.NORM_DATETIME_FORMATTER);
    }

    /**
     * 获取今年年份
     *
     * @return int int
     */
    public static int thisYear() {
        return LocalDate.now().getYear();
    }

    /**
     * 获取当前月份
     *
     * @return int int
     */
    public static int thisMonth() {
        return LocalDate.now().getMonthValue();
    }

    /**
     * 是否闰年
     *
     * @param year 年
     * @return 是否闰年 boolean
     */
    public static boolean isLeapYear(int year) {
        return new GregorianCalendar().isLeapYear(year);
    }

    /**
     * 获取当前时间距离第二天凌晨的秒数
     *
     * @return 返回值单位为[s :秒]
     */
    public static long getSecondsNextEarlyMorning() {
        return getNextEarlyMorning(ChronoUnit.SECONDS);
    }

    /**
     * 获取当前时间距离第二天凌晨时间
     *
     * @param chronoUnit the chrono unit
     * @return 返回值单位为[ {@link ChronoUnit} ]
     */
    public static long getNextEarlyMorning(ChronoUnit chronoUnit) {
        LocalDateTime now = now();
        LocalDateTime midnight = now.plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        return chronoUnit.between(now, midnight);
    }

    /**
     * 将LocalDateTime转成时间戳
     *
     * @param time the time
     * @return the long
     */
    public static long toTimestamp(LocalDateTime time) {
        return time.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
    }
    
    /**
     * 将毫秒转成LocalDateTime
     *
     * @param millisecond the millisecond
     * @return local date time
     */
    public static LocalDateTime ofMillisecond(long millisecond) {
        Instant instant = Instant.ofEpochMilli(millisecond);
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }

    /**
     * Of seconds local date time.
     *
     * @param seconds the seconds
     * @return the local date time
     */
    public static LocalDateTime ofSeconds(long seconds) {
        return LocalDateTime.ofEpochSecond(seconds, 0, ZoneOffset.ofHours(8));
    }

}
