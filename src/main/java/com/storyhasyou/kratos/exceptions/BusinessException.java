package com.storyhasyou.kratos.exceptions;


import com.storyhasyou.kratos.result.ErrorCode;

/**
 * @author fangxi created by 2020/6/17
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 4868842805385777275L;

    private int code;

    public BusinessException() {
    }

    public BusinessException(String message) {
        super(message);
        this.code = 500;
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(ErrorCode status) {
        super(status.getMessage());
        this.code = status.getCode();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}