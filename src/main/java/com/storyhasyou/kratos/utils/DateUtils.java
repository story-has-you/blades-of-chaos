package com.storyhasyou.kratos.utils;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.storyhasyou.kratos.toolkit.DatePattern;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
        return format(now(), DatePattern.CHINESE_DATE_FORMAT);
    }


    /**
     * Current date time string.
     *
     * @return the string
     */
    public static String currentDateTime() {
        return now().format(DatePattern.CHINESE_DATE_FORMAT);
    }

    /**
     * Current date string.
     *
     * @return the string
     */
    public static String today() {
        return format(LocalDate.now(), DatePattern.NORM_DATETIME_FORMAT);
    }

    /**
     * 获取今年年份
     *
     * @return int
     */
    public static int thisYear() {
        return LocalDate.now().getYear();
    }

    /**
     * 获取当前月份
     *
     * @return int
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
        return getSecondsNextEarlyMorning(ChronoUnit.SECONDS);
    }

    /**
     * 获取当前时间距离第二天凌晨时间
     *
     * @return 返回值单位为[ {@link ChronoUnit} ]
     */
    public static long getSecondsNextEarlyMorning(ChronoUnit chronoUnit) {
        LocalDateTime now = now();
        LocalDateTime midnight = now.plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        return chronoUnit.between(now, midnight);
    }

}
