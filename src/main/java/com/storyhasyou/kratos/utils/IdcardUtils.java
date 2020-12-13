package com.storyhasyou.kratos.utils;

import cn.hutool.core.util.IdcardUtil;
import com.storyhasyou.kratos.toolkit.DatePattern;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 身份证相关工具类<br>
 *
 * @author fangxi
 */
public class IdcardUtils extends IdcardUtil {

    /**
     * 中国公民身份证号码最小长度。
     */
    private static final int CHINA_ID_MIN_LENGTH = 15;
    /**
     * 中国公民身份证号码最大长度。
     */
    private static final int CHINA_ID_MAX_LENGTH = 18;
    /**
     * 每位加权因子
     */
    private static final int[] POWER = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
    /**
     * 省市代码表
     */
    private static final Map<String, String> CITY_CODES = new HashMap<>();
    /**
     * 台湾身份首字母对应数字
     */
    private static final Map<String, Integer> TW_FIRST_CODE = new HashMap<>();

    static {
        CITY_CODES.put("11", "北京");
        CITY_CODES.put("12", "天津");
        CITY_CODES.put("13", "河北");
        CITY_CODES.put("14", "山西");
        CITY_CODES.put("15", "内蒙古");
        CITY_CODES.put("21", "辽宁");
        CITY_CODES.put("22", "吉林");
        CITY_CODES.put("23", "黑龙江");
        CITY_CODES.put("31", "上海");
        CITY_CODES.put("32", "江苏");
        CITY_CODES.put("33", "浙江");
        CITY_CODES.put("34", "安徽");
        CITY_CODES.put("35", "福建");
        CITY_CODES.put("36", "江西");
        CITY_CODES.put("37", "山东");
        CITY_CODES.put("41", "河南");
        CITY_CODES.put("42", "湖北");
        CITY_CODES.put("43", "湖南");
        CITY_CODES.put("44", "广东");
        CITY_CODES.put("45", "广西");
        CITY_CODES.put("46", "海南");
        CITY_CODES.put("50", "重庆");
        CITY_CODES.put("51", "四川");
        CITY_CODES.put("52", "贵州");
        CITY_CODES.put("53", "云南");
        CITY_CODES.put("54", "西藏");
        CITY_CODES.put("61", "陕西");
        CITY_CODES.put("62", "甘肃");
        CITY_CODES.put("63", "青海");
        CITY_CODES.put("64", "宁夏");
        CITY_CODES.put("65", "新疆");
        CITY_CODES.put("71", "台湾");
        CITY_CODES.put("81", "香港");
        CITY_CODES.put("82", "澳门");
        CITY_CODES.put("91", "国外");

        TW_FIRST_CODE.put("A", 10);
        TW_FIRST_CODE.put("B", 11);
        TW_FIRST_CODE.put("C", 12);
        TW_FIRST_CODE.put("D", 13);
        TW_FIRST_CODE.put("E", 14);
        TW_FIRST_CODE.put("F", 15);
        TW_FIRST_CODE.put("G", 16);
        TW_FIRST_CODE.put("H", 17);
        TW_FIRST_CODE.put("J", 18);
        TW_FIRST_CODE.put("K", 19);
        TW_FIRST_CODE.put("L", 20);
        TW_FIRST_CODE.put("M", 21);
        TW_FIRST_CODE.put("N", 22);
        TW_FIRST_CODE.put("P", 23);
        TW_FIRST_CODE.put("Q", 24);
        TW_FIRST_CODE.put("R", 25);
        TW_FIRST_CODE.put("S", 26);
        TW_FIRST_CODE.put("T", 27);
        TW_FIRST_CODE.put("U", 28);
        TW_FIRST_CODE.put("V", 29);
        TW_FIRST_CODE.put("X", 30);
        TW_FIRST_CODE.put("Y", 31);
        TW_FIRST_CODE.put("W", 32);
        TW_FIRST_CODE.put("Z", 33);
        TW_FIRST_CODE.put("I", 34);
        TW_FIRST_CODE.put("O", 35);
    }



    /**
     * 根据身份编号获取生日，只支持15或18位身份证号码
     *
     * @param idCard 身份编号
     * @return 生日(yyyyMMdd) birth
     */
    public static String getBirth(String idCard) {
        final int len = idCard.length();
        if (len < CHINA_ID_MIN_LENGTH) {
            return null;
        } else if (len == CHINA_ID_MIN_LENGTH) {
            idCard = convert15To18(idCard);
        }

        return Objects.requireNonNull(idCard).substring(6, 14);
    }


    /**
     * 根据身份编号获取年龄，只支持15或18位身份证号码
     *
     * @param idCard 身份编号
     * @return 年龄 age
     */
    public static int getAge(String idCard) {
        return getAge(idCard, LocalDate.now());
    }

    /**
     * 根据身份编号获取指定日期当时的年龄年龄，只支持15或18位身份证号码
     *
     * @param idCard 身份编号
     * @param date   以此日期为界，计算年龄。
     * @return 年龄 age
     */
    public static int getAge(String idCard, LocalDate date) {
        String birth = getBirth(idCard);
        return Period.between(LocalDate.parse(birth, DatePattern.PURE_DATE_FORMAT), date).getYears();
    }

    /**
     * 根据身份编号获取生日年，只支持15或18位身份证号码
     *
     * @param idCard 身份编号
     * @return 生日(yyyy) year
     */
    public static Short getYear(String idCard) {
        final int len = idCard.length();
        if (len < CHINA_ID_MIN_LENGTH) {
            return null;
        } else if (len == CHINA_ID_MIN_LENGTH) {
            idCard = convert15To18(idCard);
        }
        return Short.valueOf(Objects.requireNonNull(idCard).substring(6, 10));
    }

    /**
     * 根据身份编号获取生日月，只支持15或18位身份证号码
     *
     * @param idCard 身份编号
     * @return 生日(MM) month
     */
    public static Short getMonth(String idCard) {
        final int len = idCard.length();
        if (len < CHINA_ID_MIN_LENGTH) {
            return null;
        } else if (len == CHINA_ID_MIN_LENGTH) {
            idCard = convert15To18(idCard);
        }
        return Short.valueOf(Objects.requireNonNull(idCard).substring(10, 12));
    }

    /**
     * 根据身份编号获取生日天，只支持15或18位身份证号码
     *
     * @param idCard 身份编号
     * @return 生日(dd) day by id card
     */
    public static Short getDayByIdCard(String idCard) {
        final int len = idCard.length();
        if (len < CHINA_ID_MIN_LENGTH) {
            return null;
        } else if (len == CHINA_ID_MIN_LENGTH) {
            idCard = convert15To18(idCard);
        }
        return Short.valueOf(Objects.requireNonNull(idCard).substring(12, 14));
    }

    /**
     * 根据身份编号获取性别，只支持15或18位身份证号码
     *
     * @param idCard 身份编号
     * @return 性别(1 : 男 ， 0 : 女) gender
     */
    public static int getGender(String idCard) {
        final int len = idCard.length();
        if (len < CHINA_ID_MIN_LENGTH) {
            throw new IllegalArgumentException("ID Card length must be 15 or 18");
        }

        if (len == CHINA_ID_MIN_LENGTH) {
            idCard = convert15To18(idCard);
        }
        char sCardChar = Objects.requireNonNull(idCard).charAt(16);
        return (sCardChar % 2 != 0) ? 1 : 0;
    }

    /**
     * 根据身份编号获取户籍省份，只支持15或18位身份证号码
     *
     * @param idCard 身份编码
     * @return 省级编码 。
     */
    public static String getProvince(String idCard) {
        int len = idCard.length();
        if (len == CHINA_ID_MIN_LENGTH || len == CHINA_ID_MAX_LENGTH) {
            String sProvinNum = idCard.substring(0, 2);
            return CITY_CODES.get(sProvinNum);
        }
        return null;
    }


}
