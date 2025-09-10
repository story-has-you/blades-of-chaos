package com.storyhasyou.kratos.enums;

import lombok.AllArgsConstructor;

/**
 * The enum Yes no enum.
 *
 * @author fangxi
 */
@AllArgsConstructor
public enum YesNoEnum implements IntBaseEnum {

    /**
     * Yes yes no enum.
     */
    YES(1, "是"),
    /**
     * No yes no enum.
     */
    NO(0, "否"),
    ;


    private final int code;
    private final String message;

    /**
     * Gets by code.
     *
     * @param code the code
     * @return the by code
     */
    public static YesNoEnum of(int code) {
        return code == 1 ? YES : NO;
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
