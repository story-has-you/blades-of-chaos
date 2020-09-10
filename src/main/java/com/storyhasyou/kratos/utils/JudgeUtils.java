package com.storyhasyou.kratos.utils;

import java.util.function.Supplier;

/**
 * The type Judge utils.
 *
 * @author 方曦 created by 2020-8-31
 */
public class JudgeUtils {

    private JudgeUtils() {

    }

    /**
     * Process t.
     *
     * @param <T>       the type parameter
     * @param condition the condition
     * @param supplier  the supplier
     * @return the t
     */
    public static <T> T process(boolean condition, Supplier<T> supplier) {
        if (condition) {
            return supplier.get();
        }
        return null;
    }

    /**
     * Process t.
     *
     * @param <T>       the type parameter
     * @param condition the condition
     * @param result    the result
     * @return the t
     */
    public static <T> T process(boolean condition, T result) {
        return process(condition, () -> result);
    }
}
