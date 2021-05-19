package com.storyhasyou.kratos.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一返回对象
 */
@Data
@NoArgsConstructor
public class ResponseResult<T> {

    private Integer code;

    private String message;

    private T data;

    public ResponseResult(ResultEnum resultEnum) {
        this.code = resultEnum.getCode();
        this.message = resultEnum.getMessage();
    }

    public ResponseResult(Integer code, String msg) {
        this.code = code;
        this.message = msg;
    }

    public ResponseResult(ResultEnum resultEnum, T data) {
        this.code = resultEnum.getCode();
        this.message = resultEnum.getMessage();
        this.data = data;
    }

    public ResponseResult(ResultEnum resultEnum, String message) {
        this.code = resultEnum.getCode();
        this.message = resultEnum.getMessage();
    }

    public ResponseResult(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    private static <T> ResponseResult<T> build(ResultEnum resultEnum) {
        return new ResponseResult<>(resultEnum);
    }

    private static <T> ResponseResult<T> build(Integer code, String msg) {
        return new ResponseResult<>(code, msg);
    }

    private static <T> ResponseResult<T> build(Integer code, String msg, T data) {
        return new ResponseResult<>(code, msg, data);
    }

    public static <T> ResponseResult<T> success() {
        return new ResponseResult<>(ResultEnum.SUCCESS);
    }

    public static <T> ResponseResult<T> success(T t) {
        return new ResponseResult<>(ResultEnum.SUCCESS, t);
    }

    public static <T> ResponseResult<T> success(T t, String msg) {
        return new ResponseResult<>(ResultEnum.SUCCESS.getCode(), msg, t);
    }

    public static <T> ResponseResult<T> error() {
        return new ResponseResult<>(ResultEnum.FAILED);
    }

    public static <T> ResponseResult<T> error(String message) {
        return new ResponseResult<>(ResultEnum.FAILED, message);
    }

    public static <T> ResponseResult<T> error(T t) {
        return new ResponseResult<>(ResultEnum.FAILED.getCode(), null, t);
    }

    public static <T> ResponseResult<T> error(Integer code, String message) {
        return new ResponseResult<>(code, message);
    }


    public static <T> ResponseResult<T> error(Integer code, String message, T t) {
        return new ResponseResult<>(code, message, t);
    }

}
