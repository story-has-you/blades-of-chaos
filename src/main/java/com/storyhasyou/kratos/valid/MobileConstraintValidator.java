package com.storyhasyou.kratos.valid;

import com.storyhasyou.kratos.annotation.Mobile;
import com.storyhasyou.kratos.utils.RegexUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author fangxi created by 2020/7/30
 */
public class MobileConstraintValidator implements ConstraintValidator<Mobile, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return RegexUtils.checkMobile(value);
    }
}
