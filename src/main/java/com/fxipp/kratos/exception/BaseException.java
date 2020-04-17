package com.fxipp.kratos.exception;

import org.springframework.http.HttpStatus;

/**
 * @author fangxi
 */
public class BaseException extends RuntimeException {
    private Integer status;

    public BaseException() {
    }

    public BaseException(Throwable e) {
        super(e);
    }

    public BaseException(String message) {
        super(message);
        this.status = 500;
    }

    public BaseException(String message, Integer status) {
        super(message);
        this.status = status;
    }
    public BaseException(String message, HttpStatus status) {
        super(message);
        this.status = status.value();
    }

    public Integer getStatus() {
        return status;
    }
}
