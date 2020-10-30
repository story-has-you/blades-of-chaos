package com.storyhasyou.kratos.result;

import com.storyhasyou.kratos.enums.IntBaseEnum;
import org.springframework.http.HttpStatus;

/**
 * 枚举了一些常用API操作码
 *
 * @author fangxi
 */
public enum ResultCode implements IntBaseEnum {
    /**
     * Success result code.
     */
    SUCCESS(HttpStatus.OK.value(), "操作成功"),
    /**
     * Failed result code.
     */
    FAILED(HttpStatus.INTERNAL_SERVER_ERROR.value(), "操作失败"),
    /**
     * Not fount result code.
     */
    NOT_FOUNT(HttpStatus.NOT_FOUND.value(), "找不到数据"),
    /**
     * Validate failed result code.
     */
    VALIDATE_FAILED(HttpStatus.BAD_REQUEST.value(), "参数检验失败"),
    /**
     * Unauthorized result code.
     */
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED.value(), "暂未登录或token已经过期"),
    /**
     * Forbidden result code.
     */
    FORBIDDEN(HttpStatus.FORBIDDEN.value(), "没有相关权限");

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }


    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

}