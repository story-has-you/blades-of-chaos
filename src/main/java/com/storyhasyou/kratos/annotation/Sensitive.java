package com.storyhasyou.kratos.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.storyhasyou.kratos.enums.SensitiveTypeEnum;
import com.storyhasyou.kratos.toolkit.SensitiveSerialize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * The interface Sensitive.
 *
 * @author fangxi
 */
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize(using = SensitiveSerialize.class)
public @interface Sensitive {

    /**
     * Value sensitive type enum.
     *
     * @return the sensitive type enum
     */
    SensitiveTypeEnum value() default SensitiveTypeEnum.PASSWORD;

}
