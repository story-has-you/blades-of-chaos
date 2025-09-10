package com.storyhasyou.kratos.annotation.valid;

import com.storyhasyou.kratos.valid.MobileConstraintValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * The interface Mobile.
 *
 * @author fangxi created by 2020/7/30
 */
@Documented
@Constraint(
        validatedBy = MobileConstraintValidator.class
)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Mobile {

    /**
     * Message string.
     *
     * @return the string
     */
    String message() default "{Mobile.message}";

    /**
     * Groups class [ ].
     *
     * @return the class [ ]
     */
    Class<?>[] groups() default {};

    /**
     * Payload class [ ].
     *
     * @return the class [ ]
     */
    Class<? extends Payload>[] payload() default {};
}
