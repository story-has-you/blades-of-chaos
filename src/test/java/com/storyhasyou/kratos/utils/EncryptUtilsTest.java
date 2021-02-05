package com.storyhasyou.kratos.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author 方曦 created by 2021/2/5
 */
public class EncryptUtilsTest {

    @Test
    public void encrypt() {
        String encrypt = EncryptUtils.encrypt("123456");
        System.out.println(encrypt);
    }

    @Test
    public void matches() {

    }

    @Test
    public void md5() {
        String md5 = EncryptUtils.md5("123456");
        System.out.println(md5.equals(EncryptUtils.md5("123456")));
    }

}