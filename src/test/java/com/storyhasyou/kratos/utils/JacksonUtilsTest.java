package com.storyhasyou.kratos.utils;

import org.junit.jupiter.api.Test;

/**
 * @author fangxi created by 2021/1/19
 */
public class JacksonUtilsTest {

    @Test
    public void test() {
        byte[] bytes = JacksonUtils.toBytes("111");
        System.out.println(bytes);
    }

}