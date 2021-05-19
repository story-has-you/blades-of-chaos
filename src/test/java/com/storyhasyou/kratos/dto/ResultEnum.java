package com.storyhasyou.kratos.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 响应枚举
 */
@Getter
@AllArgsConstructor
public enum ResultEnum {

    /**
     * Success result enum.
     */
    SUCCESS(200, "success"),
    /**
     * Error post param result enum.
     */
    ERROR_POST_PARAM(400, "请求参数异常"),
    /**
     * Path not found result enum.
     */
    PATH_NOT_FOUND(404, "页面没有找到"),
    /**
     * Error post method result enum.
     */
    ERROR_POST_METHOD(405, "请求方法异常"),
    /**
     * Failed result enum.
     */
    FAILED(500, "系统异常"),
    ;

    /**
     * The Code.
     */
    private final Integer code;

    /**
     * The Message.
     */
    private final String message;
    
    
}
