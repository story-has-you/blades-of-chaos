package com.storyhasyou.kratos.utils;

import java.util.function.Supplier;

/**
 * @author 方曦 created by 2020-8-31
 */
public class JudgeUtils {

    private JudgeUtils() {

    }

    public static <T> T process(boolean condition, Supplier<T> supplier) {
        if (condition) {
            return supplier.get();
        }
        return null;
    }
}
