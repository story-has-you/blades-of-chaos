package com.storyhasyou.kratos.result;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Maps;
import com.storyhasyou.kratos.enums.IntBaseEnum;
import com.storyhasyou.kratos.exceptions.BusinessException;

import java.io.Serializable;
import java.util.Map;
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
    /**
     * The Status.
     */
    private Integer status;
    /**
     * The Message.
     */
    private String message;
    /**
     * The Data.
     */
    private T data;
    /**
     * The Ok.
     */
    private Boolean ok;

    @JsonIgnore
    private final Map<String, Object> responseBody = Maps.newHashMap();

    /**
     * Instantiates a new Result.
     */
    public Result() {
    }

    /**
     * Instantiates a new Result.
     *
     * @param status  the status
     * @param message the message
     * @param data    the data
     * @param ok      the ok
     */
    public Result(Integer status, String message, T data, Boolean ok) {
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
     * @return the result
     */
    public static <T> Result<T> error(IntBaseEnum errorCode) {
        return error(errorCode, null);
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
    public static <T> Result<T> error(IntBaseEnum errorCode, String message) {
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
        return error(ResultCode.FAILURE);
    }

    /**
     * 参数验证失败返回结果
     *
     * @param <T> the type parameter
     * @return the result
     */
    public static <T> Result<T> validateFAILURE() {
        return error(ResultCode.PARAM_VALID_ERROR);
    }

    /**
     * Unauthorized result.
     *
     * @param <T> the type parameter
     * @return the result
     */
    public static <T> Result<T> unauthorized() {
        return error(ResultCode.UN_AUTHORIZED);
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
        return !ok;
    }

    /**
     * Gets ok.
     *
     * @return the ok
     */
    @JsonIgnore
    public boolean isOk() {
        return ok;
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
    public <E extends BusinessException, R> R data(Function<T, R> function, Supplier<? extends E> exceptionSupplier) {
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
        return data(Function.identity(), exceptionSupplier);
    }


    /**
     * Service data t.
     *
     * @param <R>      the type parameter
     * @param function the function
     * @return the t
     */
    public <R> R data(Function<T, R> function) {
        return data(function, () -> new BusinessException(message));
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

    /**
     * Sets ok.
     *
     * @param ok the ok
     */
    public void setOk(Boolean ok) {
        this.ok = ok;
    }

}