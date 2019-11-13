package com.fxipp.kratos.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fxipp.kratos.config.SensitiveSerialize;
import com.fxipp.kratos.enums.SensitiveTypeEnum;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author fangxi
 */
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize(using = SensitiveSerialize.class)
public @interface Sensitive {

    SensitiveTypeEnum value() default SensitiveTypeEnum.NAME;

}
