package com.storyhasyou.kratos.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

/**
 * The type Md 5 utils.
 *
 * @author fangxi
 */
public class Md5Utils {

    /**
     * Md 5 string.
     *
     * @param str the str
     * @return the string
     */
    public static String md5(String str) {
        //确定计算方法
        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");
            //加密后的字符串
            byte[] src = md5.digest(str.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(src);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
