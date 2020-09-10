package com.storyhasyou.kratos.valid;

import com.storyhasyou.kratos.annotation.Mobile;
import com.storyhasyou.kratos.utils.RegexUtils;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * The type Mobile constraint validator.
 *
 * @author fangxi created by 2020/7/30
 */
public class MobileConstraintValidator implements ConstraintValidator<Mobile, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (StringUtils.isBlank(value)) {
            return false;
        }
        return RegexUtils.checkMobile(value);
    }
}
