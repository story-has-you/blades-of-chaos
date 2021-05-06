package com.storyhasyou.kratos.annotation;

import com.storyhasyou.kratos.valid.CitizenIdConstraintValidator;
import com.storyhasyou.kratos.valid.MobileConstraintValidator;

import javax.validation.Constraint;
import java.lang.annotation.*;

/**
 * @author fangxi created by 2021/5/6
 */
@Documented
@Constraint(
        validatedBy = CitizenIdConstraintValidator.class
)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CitizenId {
}
