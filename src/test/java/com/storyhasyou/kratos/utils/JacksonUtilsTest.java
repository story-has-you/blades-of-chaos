package com.storyhasyou.kratos.utils;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * @author fangxi created by 2021/1/19
 */
public class JacksonUtilsTest {

    @Test
    public void test() {
        byte[] bytes = JacksonUtils.toBytes("123");
        System.out.println(Arrays.toString(bytes));
    }

}
