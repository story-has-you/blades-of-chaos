package com.storyhasyou.kratos.result;

/**
 * 封装API的错误码
 */
public interface ErrorCode {
    int getCode();

    String getMessage();
}