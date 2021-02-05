package com.storyhasyou.kratos.utils;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.MD5;
import cn.hutool.crypto.symmetric.AES;

/**
 * The type Encrypt utils.
 *
 * @author fangxi
 */
public class EncryptUtils {

    /**
     * The constant AES.
     */
    private static final AES AES = SecureUtil.aes();
    /**
     * The constant md5.
     */
    private static final MD5 MD5 = SecureUtil.md5();


    /**
     * Encrypt string.
     *
     * @param content the content
     * @return the string
     */
    public static String encrypt(String content) {
        return AES.encryptHex(content);
    }

    /**
     * Matches boolean.
     *
     * @param source          the source
     * @param encryptPassword the encrypt password
     * @return the boolean
     */
    public static boolean matches(String source, String encryptPassword) {
        return AES.decryptStr(encryptPassword).equals(source);
    }

    /**
     * Decrypt string.
     *
     * @param encryptPassword the encrypt password
     * @return the string
     */
    public static String decrypt(String encryptPassword) {
        return AES.decryptStr(encryptPassword);
    }

    /**
     * Md 5 string.
     *
     * @param content the content
     * @return the string
     */
    public static String md5(String content) {
        return MD5.digestHex(content);
    }

}
