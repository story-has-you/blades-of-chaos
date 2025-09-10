package com.storyhasyou.kratos.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * @author fangxi
 */
public class ChineseUtils {

    private static final Pattern PATTERN = Pattern.compile("[一-龥]");

    /**
     * 判断字符串中是否包含中文
     * @param str
     * 待校验字符串
     * @return 是否为中文
     * @warn 不能校验是否为中文标点符号
     */
    public static boolean isContainChinese(String str) {
        return PATTERN.matcher(str).find();
    }

    /**
     * 过滤掉中文
     * @param str 待过滤中文的字符串
     * @return 过滤掉中文后字符串
     */
    public static String filterChinese(String str) {
        if(StringUtils.isBlank(str)){
            return StringUtils.EMPTY;
        }
        // 用于返回结果
        String result = str;
        boolean flag = isContainChinese(str);
        // 包含中文
        if (!flag) {
            return result;
        }
        // 用于拼接过滤中文后的字符
        StringBuilder sb = new StringBuilder();
        // 用于校验是否为中文
        boolean flag2;
        // 用于临时存储单字符
        char chinese;
        // 5.去除掉文件名中的中文
        // 将字符串转换成char[]
        char[] charArray = str.toCharArray();
        // 过滤到中文及中文字符
        for (char c : charArray) {
            chinese = c;
            flag2 = isChinese(chinese);
            // 不是中日韩文字及标点符号
            if (!flag2) {
                sb.append(chinese);
            }
        }
        result = sb.toString();
        return result;
    }

    /**
     * 判定输入的是否是汉字
     *
     * @param c
     *  被校验的字符
     * @return true代表是汉字
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;
    }

}
