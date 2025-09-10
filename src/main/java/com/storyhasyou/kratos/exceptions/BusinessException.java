package com.storyhasyou.kratos.exceptions;


import com.storyhasyou.kratos.enums.IntBaseEnum;
import com.storyhasyou.kratos.result.ResultCode;
import lombok.Getter;

/**
 * The type Business exception.
 *
 * @author fangxi created by 2020/6/17
 */
@Getter
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 4868842805385777275L;

    /**
     * -- GETTER --
     *  Gets code.
     *
     * @return the code
     */
    private int code;

    /**
     * Instantiates a new Business exception.
     */
    public BusinessException() {
    }

    /**
     * Instantiates a new Business exception.
     */
    public BusinessException(String message, Throwable throwable) {
        super(message, throwable);
    }


    /**
     * Instantiates a new Business exception.
     *
     * @param message the message
     */
    public BusinessException(String message) {
        super(message);
        this.code = ResultCode.FAILURE.getCode();
    }

    /**
     * Instantiates a new Business exception.
     *
     * @param code    the code
     * @param message the message
     */
    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * Instantiates a new Business exception.
     *
     * @param status the status
     */
    public BusinessException(IntBaseEnum status) {
        super(status.getMessage());
        this.code = status.getCode();
    }

    /**
     * Sets code.
     *
     * @param code the code
     */
    public void setCode(int code) {
        this.code = code;
    }
}
