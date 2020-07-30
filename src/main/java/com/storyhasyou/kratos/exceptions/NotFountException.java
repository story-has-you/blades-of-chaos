package com.storyhasyou.kratos.exceptions;

import com.storyhasyou.kratos.result.ResultCode;

/**
 * @author fangxi created by 2020/7/30
 */
public class NotFountException extends BusinessException {

    public NotFountException() {
        super(ResultCode.NOT_FOUNT);
    }

    public NotFountException(String message) {
        super(404, message);
    }

}
