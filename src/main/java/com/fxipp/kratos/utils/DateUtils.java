package com.fxipp.kratos.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author fangxi
 */
public class DateUtils {

    /**
     * Base ISO 8601 Date format yyyyMMdd i.e., 20021225 for the 25th day of December in the year 2002
     */
    public static final String ISO_DATE_FORMAT = "yyyyMMdd";

    /**
     * Expanded ISO 8601 Date format yyyy-MM-dd i.e., 2002-12-25 for the 25th day of December in the year 2002
     */
    public static final String ISO_EXPANDED_DATE_FORMAT = "yyyy-MM-dd";

    /**
     * yyyy-MM-dd hh:mm:ss
     */
    public static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private DateUtils() {

    }

    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    public static String dateTimeToString(LocalDateTime date, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return date.format(formatter);
    }

    public static String dateTimeToString(LocalDateTime date) {
        return dateTimeToString(date, DATETIME_PATTERN);
    }

    public static LocalDateTime stringToDateTime(String date) {
        return stringToDateTime(date, DATETIME_PATTERN);
    }

    public static LocalDateTime stringToDateTime(String date, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(date, formatter);
    }


    public static String dateToString(LocalDate date, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return date.format(formatter);
    }

    public static String dateToString(LocalDate date) {
        return dateToString(date, ISO_EXPANDED_DATE_FORMAT);
    }

    public static LocalDate stringToDate(String date) {
        return stringToDate(date, ISO_EXPANDED_DATE_FORMAT);
    }

    public static LocalDate stringToDate(String date, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDate.parse(date, formatter);
    }


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

    public static String dateToString(Date date) {
        return dateToString(date, DATETIME_PATTERN);
    }

    public static String currentDateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATETIME_PATTERN);
        return now.format(formatter);
    }

    public static String currentDate() {
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(ISO_EXPANDED_DATE_FORMAT);
        return now.format(formatter);
    }

}
