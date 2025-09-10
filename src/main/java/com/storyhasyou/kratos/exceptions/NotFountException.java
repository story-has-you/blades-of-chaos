package com.storyhasyou.kratos.exceptions;

import com.storyhasyou.kratos.result.ResultCode;

/**
 * The type Not fount exception.
 *
 * @author fangxi created by 2020/7/30
 */
public class NotFountException extends BusinessException {

    /**
     * Instantiates a new Not fount exception.
     */
    public NotFountException() {
        super(ResultCode.NOT_FOUND);
    }

    /**
     * Instantiates a new Not fount exception.
     *
     * @param message the message
     */
    public NotFountException(String message) {
        super(ResultCode.NOT_FOUND.getCode(), message);
    }

}
