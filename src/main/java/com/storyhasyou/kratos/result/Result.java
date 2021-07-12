package com.storyhasyou.kratos.result;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Maps;
import com.storyhasyou.kratos.exceptions.BusinessException;

import java.io.Serializable;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * The type Result.
 *
 * @param <T> the type parameter
 * @author fangxi 通用返回对象
 */
@SuppressWarnings("all")
public class Result<T> implements Serializable {
    @JsonIgnore
    private final Map<String, Object> responseBody = Maps.newHashMap();
    private Integer status;
    private String message;
    private T data;
    private Boolean ok;

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
     * @param ok      the ok
     */
    private Result(Integer status, String message, T data, Boolean ok) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.ok = ok;
    }

    /**
     * 成功返回结果
     *
     * @param <T> the type parameter
     * @return the result
     */
    public static <T> Result<T> ok() {
        return ok(null);
    }

    /**
     * 成功返回结果
     *
     * @param <T>  the type parameter
     * @param data 获取的数据
     * @return the result
     */
    public static <T> Result<T> ok(T data) {
        return ok(data, ResultCode.SUCCESS.getMessage());
    }

    /**
     * 成功返回结果
     *
     * @param <T>     the type parameter
     * @param data    获取的数据
     * @param message 提示信息
     * @return the result
     */
    public static <T> Result<T> ok(T data, String message) {
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
    public static <T> Result<T> error(int errorCode, String message) {
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
    public static <T> Result<T> error(ResultCode errorCode, String message) {
        return new Result<>(errorCode.getCode(), message, null, false);
    }

    /**
     * 失败返回结果
     *
     * @param <T>     the type parameter
     * @param message 提示信息
     * @return the result
     */
    public static <T> Result<T> error(String message) {
        return error(ResultCode.FAILURE, message);
    }

    /**
     * 失败返回结果
     *
     * @param <T> the type parameter
     * @return the result
     */
    public static <T> Result<T> error() {
        return error(ResultCode.FAILURE, ResultCode.FAILURE.getMessage());
    }


    /**
     * Body result.
     *
     * @return the result
     */
    public static Result<Map<String, Object>> body() {
        return Result.ok();
    }

    /**
     * Put result.
     *
     * @param name  the name
     * @param value the value
     * @return the result
     */
    public Result<Map<String, Object>> put(String name, Object value) {
        return put(true, name, value);
    }

    /**
     * Put result.
     *
     * @param condition the condition
     * @param name      the name
     * @param value     the value
     * @return the result
     */
    public Result<Map<String, Object>> put(boolean condition, String name, Object value) {
        if (condition) {
            this.responseBody.put(name, value);
        }
        return (Result<Map<String, Object>>) this;
    }

    /**
     * Build result.
     *
     * @return the result
     */
    public Result<Map<String, Object>> build() {
        return Result.ok(this.responseBody);
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
     * Sets ok.
     *
     * @param ok the ok
     */
    public void setOk(Boolean ok) {
        this.ok = ok;
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
    public <E extends BusinessException, R> R data(Function<T, R> function, Supplier<? extends E> exceptionSupplier, Supplier<R> defaultValue) {
        if (defaultValue != null) {
            return defaultValue.get();
        }
        if (isError()) {
            throw exceptionSupplier.get();
        }
        return function.apply(data);
    }

    /**
     * Service data t.
     *
     * @return the t
     */
    public T data() {
        return data;
    }

    /**
     * Service data t.
     *
     * @param <E>               the type parameter
     * @param exceptionSupplier the exception supplier
     * @return the t
     */
    public <E extends BusinessException> T data(Supplier<? extends E> exceptionSupplier) {
        return data(Function.identity(), exceptionSupplier, null);
    }

    /**
     * Service data t.
     *
     * @param <R>      the type parameter
     * @param function the function
     * @return the t
     */
    public <R> R data(Function<T, R> function, R defaultValue) {
        return data(function, () -> new BusinessException(message), () -> defaultValue);
    }

    /**
     * Service data t.
     *
     * @param <R>      the type parameter
     * @param function the function
     * @return the t
     */
    public <R> R data(Function<T, R> function, Supplier<R> defaultValue) {
        return data(function, () -> new BusinessException(message), defaultValue);
    }

    /**
     * Service data t.
     *
     * @param <R>      the type parameter
     * @param function the function
     * @return the t
     */
    public <R> R data(Function<T, R> function) {
        return data(function, () -> new BusinessException(message), null);
    }

    /**
     * Service data t.
     *
     * @param consumer the consumer
     * @return the t
     */
    public void data(Consumer<T> consumer) {
        if (isOk()) {
            consumer.accept(this.data);
        }
    }

    /**
     * Gets status.
     *
     * @return the status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * Sets status.
     *
     * @param status the status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * Gets message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets message.
     *
     * @param message the message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Sets data.
     *
     * @param data the data
     */
    public void setData(T data) {
        this.data = data;
    }

}
