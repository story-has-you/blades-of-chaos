package com.storyhasyou.kratos.result;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * @author fangxi
 * 通用返回对象
 */
public class Result<T> implements Serializable {
    private Integer status;
    private String message;
    private T data;
    private Boolean ok;

    public Result() {
    }

    public Result(Integer status, String message, T data, Boolean ok) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.ok = ok;
    }

    @JsonIgnore
    public boolean isOk() {
        return ok;
    }

    @JsonIgnore
    public boolean isError() {
        return !ok;
    }

    /**
     * 成功返回结果
     */
    public static <T> Result<T> ok() {
        return ok(null);
    }

    /**
     * 成功返回结果
     *
     * @param data 获取的数据
     */
    public static <T> Result<T> ok(T data) {
        return ok(data, ResultCode.SUCCESS.getMessage());
    }

    /**
     * 成功返回结果
     *
     * @param data    获取的数据
     * @param message 提示信息
     */
    public static <T> Result<T> ok(T data, String message) {
        return new Result<>(ResultCode.SUCCESS.getCode(), message, data,true);
    }

    /**
     * 失败返回结果
     *
     * @param errorCode 错误码
     */
    public static <T> Result<T> error(ErrorCode errorCode) {
        return error(errorCode, null);
    }

    /**
     * 失败返回结果
     *
     * @param errorCode 错误码
     * @param message   错误信息
     */
    public static <T> Result<T> error(ErrorCode errorCode, String message) {
        return new Result<>(errorCode.getCode(), message, null, false);
    }

    /**
     * 失败返回结果
     *
     * @param message 提示信息
     */
    public static <T> Result<T> error(String message) {
        return error(ResultCode.FAILED, message);
    }

    /**
     * 失败返回结果
     */
    public static <T> Result<T> error() {
        return error(ResultCode.FAILED);
    }

    /**
     * 参数验证失败返回结果
     */
    public static <T> Result<T> validateFailed() {
        return error(ResultCode.VALIDATE_FAILED);
    }


    public Boolean getOk() {
        return ok;
    }

    public void setOk(Boolean ok) {
        this.ok = ok;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}