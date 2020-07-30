package com.storyhasyou.kratos.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author fangxi
 */
@AllArgsConstructor
@Getter
public enum YesNoEnum {

    YES(1, "是"),
    NO(0, "否"),
    ;


    private final int code;
    private final String value;

    public static YesNoEnum getByCode(int code) {
        return code == 1 ? YES : NO;
    }

}
