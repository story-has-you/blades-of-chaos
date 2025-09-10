package com.storyhasyou.kratos.valid;

import cn.hutool.core.lang.Validator;
import com.storyhasyou.kratos.annotation.valid.Mobile;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;


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
        return Validator.isMobile(value);
    }
}
