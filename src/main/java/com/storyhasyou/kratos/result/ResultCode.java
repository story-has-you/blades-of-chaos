package com.storyhasyou.kratos.result;

import com.storyhasyou.kratos.enums.IntBaseEnum;

/**
 * 枚举了一些常用API操作码
 *
 * @author fangxi
 */
public enum ResultCode implements IntBaseEnum {
    SUCCESS(200, "操作成功"),
    FAILED(500, "操作失败"),
    NOT_FOUNT(404, "找不到数据"),
    VALIDATE_FAILED(400, "参数检验失败"),
    UNAUTHORIZED(401, "暂未登录或token已经过期"),
    FORBIDDEN(403, "没有相关权限");

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }


    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

}