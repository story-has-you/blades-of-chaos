package com.storyhasyou.kratos.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 方曦 created by 2020/12/8
 */
@Getter
@AllArgsConstructor
public enum LanguageEnum implements StringBaseEnum {


    AUTO("auto", "自动识别"),
    ZH_CHS("zh-CHS", "中文"),
    EN("en", "英语"),
    ;


    private final String code;
    private final String message;


    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
