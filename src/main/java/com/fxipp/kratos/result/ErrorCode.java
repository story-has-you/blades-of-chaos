package com.fxipp.kratos.result;

/**
 * 封装API的错误码
 */
public interface ErrorCode {
    Long getCode();

    String getMessage();
}