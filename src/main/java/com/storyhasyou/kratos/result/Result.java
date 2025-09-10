package com.storyhasyou.kratos.result;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.storyhasyou.kratos.exceptions.BusinessException;
import lombok.Data;

import java.io.Serializable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * The type Result.
 *
 * @param <T> the type parameter
 * @author fangxi 通用返回对象
 */
@Data
public class Result<T> implements Serializable {
    private Integer status;
    private String message;
    private T data;
    private Boolean success;

    /**
     * Instantiates a new Result.
     */
    private Result() {
    }

    /**
     * Instantiates a new Result.
     *
     * @param status  the status
     * @param message the message
     * @param data    the data
     * @param success      the ok
     */
    private Result(Integer status, String message, T data, boolean success) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.success = success;
    }

    /**
     * 成功返回结果
     *
     * @param <T> the type parameter
     * @return the result
     */
    public static <T> Result<T> success() {
        return success(null);
    }

    /**
     * 成功返回结果
     *
     * @param <T>  the type parameter
     * @param data 获取的数据
     * @return the result
     */
    public static <T> Result<T> success(T data) {
        return success(data, ResultCode.SUCCESS.getMessage());
    }

    /**
     * 成功返回结果
     *
     * @param <T>     the type parameter
     * @param data    获取的数据
     * @param message 提示信息
     * @return the result
     */
    public static <T> Result<T> success(T data, String message) {
        return new Result<>(ResultCode.SUCCESS.getCode(), message, data, true);
    }


    /**
     * 失败返回结果
     *
     * @param <T>       the type parameter
     * @param errorCode 错误码
     * @param message   错误信息
     * @return the result
     */
    public static <T> Result<T> fail(int errorCode, String message) {
        return new Result<>(errorCode, message, null, false);
    }

    /**
     * 失败返回结果
     *
     * @param <T>       the type parameter
     * @param errorCode 错误码
     * @param message   错误信息
     * @return the result
     */
    public static <T> Result<T> fail(ResultCode errorCode, String message) {
        return new Result<>(errorCode.getCode(), message, null, false);
    }

    /**
     * 失败返回结果
     *
     * @param <T>     the type parameter
     * @param message 提示信息
     * @return the result
     */
    public static <T> Result<T> fail(String message) {
        return fail(ResultCode.FAILURE, message);
    }

    /**
     * 失败返回结果
     *
     * @param <T> the type parameter
     * @return the result
     */
    public static <T> Result<T> fail() {
        return fail(ResultCode.FAILURE, ResultCode.FAILURE.getMessage());
    }


    /**
     * Is error boolean.
     *
     * @return the boolean
     */
    @JsonIgnore
    public boolean isError() {
        return status != 200;
    }

    /**
     * Gets ok.
     *
     * @return the ok
     */
    @JsonIgnore
    public boolean isOk() {
        return status == 200;
    }

    /**
     * Service data t.
     *
     * @return the t
     */
    public T serviceData() {
        return data;
    }

    /**
     * Service data t.
     *
     * @param <E>               the type parameter
     * @param exceptionSupplier the exception supplier
     * @return the t
     */
    public <E extends RuntimeException> T serviceData(Supplier<? extends E> exceptionSupplier) {
        return serviceData(Function.identity(), exceptionSupplier);
    }

    /**
     * Service data t.
     *
     * @param <R>      the type parameter
     * @param function the function
     * @return the t
     */
    public <R> R serviceData(Function<T, R> function) {
        return serviceData(function, () -> new BusinessException(message));
    }


    /**
     * Service data t.
     *
     * @param <E>               the type parameter
     * @param <R>               the type parameter
     * @param function          the function
     * @param exceptionSupplier the exception supplier
     * @return the t
     */
    public <E extends RuntimeException, R> R serviceData(Function<T, R> function, Supplier<? extends E> exceptionSupplier) {
        if (isError()) {
            throw exceptionSupplier.get();
        }
        return function.apply(data);
    }

    /**
     * Service data t.
     *
     * @param consumer the consumer
     * @return the t
     */
    public void serviceData(Consumer<T> consumer) {
        if (isOk()) {
            consumer.accept(this.data);
        } else {
            throw new BusinessException(this.message);
        }
    }

}
