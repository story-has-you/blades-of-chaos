package com.storyhasyou.kratos.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author fangxi
 */
public class NumberUtils {

    public static boolean isInt(Double num) {
        return num.intValue() == num;
    }

    /**
     * 判断字符串是否是数值格式
     * @param str
     * @return
     */
    public static boolean isDigit(String str){
        if(str == null || "".equals(str.trim())){
            return false;
        }
        return str.matches("^\\d+$");
    }

    /**
     * 将一个小数精确到指定位数
     * @param num
     * @param scale
     * @return
     */
    public static double scale(double num, int scale) {
        BigDecimal bd = new BigDecimal(num);
        return bd.setScale(scale, RoundingMode.HALF_UP).doubleValue();
    }

    public static Double[] searchNumber(String value, String regex){
        List<Double> doubles = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);
        if(matcher.find()) {
            MatchResult result = matcher.toMatchResult();
            for (int i = 1; i <= result.groupCount(); i++) {
                doubles.add(Double.valueOf(result.group(i)));
            }
        }
        return doubles.toArray(new Double[0]);
    }

    /**
     * 生成指定位数的随机数字
     * @param len
     * @return
     */
    public static String generateCode(int len){
       return ThreadLocalRandom.current()
                .ints(0, 10)
                .limit(len)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining());
    }

    /**
     * 将金额转成分
     * @param amount
     * @return
     */
    public static int decimal2Minute(BigDecimal amount) {
        return amount.multiply(new BigDecimal(100)).setScale(0, RoundingMode.DOWN).intValue();
    }

    /**
     * 将分转成金额
     * @param amount
     * @return
     */
    public static BigDecimal minute2Decimal(int amount) {
        return BigDecimal.valueOf(amount).divide(BigDecimal.valueOf(100L), 2, RoundingMode.DOWN);
    }
}
