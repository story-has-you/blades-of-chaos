package com.storyhasyou.kratos.enums;

/**
 * The interface Base enum.
 *
 * @param <T> the type parameter
 * @author 方曦 created by 2020-8-31
 */
public interface BaseEnum<T> {

    /**
     * Gets code.
     *
     * @return the code
     */
    T getCode();

    /**
     * Gets message.
     *
     * @return the message
     */
    String getMessage();

}
