package com.storyhasyou.kratos.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The enum Language enum.
 *
 * @author 方曦 created by 2020/12/8
 */
@Getter
@AllArgsConstructor
public enum LanguageEnum implements StringBaseEnum {


    /**
     * Auto language enum.
     */
    AUTO("auto", "自动识别"),
    /**
     * Zh chs language enum.
     */
    ZH_CHS("zh-CHS", "中文"),
    /**
     * En language enum.
     */
    EN("en", "英语"),
    ;


    /**
     * The Code.
     */
    private final String code;
    /**
     * The Message.
     */
    private final String message;


    /**
     * Gets code.
     *
     * @return the code
     */
    @Override
    public String getCode() {
        return this.code;
    }

    /**
     * Gets message.
     *
     * @return the message
     */
    @Override
    public String getMessage() {
        return this.message;
    }
}
