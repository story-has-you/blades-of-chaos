package com.storyhasyou.kratos.valid;

import com.storyhasyou.kratos.toolkit.StringPool;
import com.storyhasyou.kratos.utils.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * 对象验证器
 * <p>
 *
 * @author fangxi
 */
public class BeanValidator {


    /**
     * 验证某个bean的参数
     *
     * @param object 被校验的参数
     * @throws ValidationException 如果参数校验不成功则抛出此异常
     */
    public static <T> void validate(T object) {
        //获得验证器
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        //执行验证
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(object);
        //如果有验证信息，则取出来包装成异常返回
        if (CollectionUtils.isNotEmpty(constraintViolations)) {
            throw new ValidationException(convertErrorMsg(constraintViolations));
        }
    }


    /**
     * 转换异常信息
     */
    private static <T> String convertErrorMsg(Set<ConstraintViolation<T>> set) {
        return set.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(StringPool.COMMA));
    }
}