package com.storyhasyou.kratos.utils;

import com.storyhasyou.kratos.toolkit.DatePattern;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * The type Date utils.
 *
 * @author fangxi
 */
public class DateUtils {



    private DateUtils() {

    }

    /**
     * Now string.
     *
     * @return the string
     */
    public static String now() {
        return dateTimeToString(LocalDateTime.now(), DatePattern.NORM_DATETIME_PATTERN);
    }

    /**
     * Date time to string string.
     *
     * @param date    the date
     * @param pattern the pattern
     * @return the string
     */
    public static String dateTimeToString(LocalDateTime date, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return date.format(formatter);
    }

    /**
     * Date time to string string.
     *
     * @param date the date
     * @return the string
     */
    public static String dateTimeToString(LocalDateTime date) {
        return dateTimeToString(date, DatePattern.NORM_DATETIME_PATTERN);
    }

    /**
     * String to date time local date time.
     *
     * @param date the date
     * @return the local date time
     */
    public static LocalDateTime stringToDateTime(String date) {
        return stringToDateTime(date, DatePattern.NORM_DATETIME_PATTERN);
    }

    /**
     * String to date time local date time.
     *
     * @param date    the date
     * @param pattern the pattern
     * @return the local date time
     */
    public static LocalDateTime stringToDateTime(String date, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(date, formatter);
    }


    /**
     * Date to string string.
     *
     * @param date    the date
     * @param pattern the pattern
     * @return the string
     */
    public static String dateToString(LocalDate date, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return date.format(formatter);
    }

    /**
     * Date to string string.
     *
     * @param date the date
     * @return the string
     */
    public static String dateToString(LocalDate date) {
        return dateToString(date, DatePattern.NORM_DATE_PATTERN);
    }

    /**
     * String to date local date.
     *
     * @param date the date
     * @return the local date
     */
    public static LocalDate stringToDate(String date) {
        return stringToDate(date, DatePattern.NORM_DATE_PATTERN);
    }

    /**
     * String to date local date.
     *
     * @param date    the date
     * @param pattern the pattern
     * @return the local date
     */
    public static LocalDate stringToDate(String date, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDate.parse(date, formatter);
    }


    /**
     * Date to string string.
     *
     * @param date    the date
     * @param pattern the pattern
     * @return the string
     */
    public static String dateToString(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        try {
            SimpleDateFormat sfDate = new SimpleDateFormat(pattern);
            sfDate.setLenient(false);
            return sfDate.format(date);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Date to string string.
     *
     * @param date the date
     * @return the string
     */
    public static String dateToString(Date date) {
        return dateToString(date, DatePattern.NORM_DATETIME_PATTERN);
    }

    /**
     * Current date time string.
     *
     * @return the string
     */
    public static String currentDateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN);
        return now.format(formatter);
    }

    /**
     * Current date string.
     *
     * @return the string
     */
    public static String today() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN);
        return today.format(formatter);
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

}
