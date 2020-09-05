package com.storyhasyou.kratos.enums;

import lombok.AllArgsConstructor;

/**
 * @author fangxi
 */
@AllArgsConstructor
public enum YesNoEnum implements IntBaseEnum {

    YES(1, "是"),
    NO(0, "否"),
    ;


    private final int code;
    private final String value;

    public static YesNoEnum getByCode(int code) {
        return code == 1 ? YES : NO;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.value;
    }
}
