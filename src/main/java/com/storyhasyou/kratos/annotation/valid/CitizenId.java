package com.storyhasyou.kratos.annotation.valid;

import com.storyhasyou.kratos.valid.CitizenIdConstraintValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

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
    /**
     * Message string.
     *
     * @return the string
     */
    String message() default "{CitizenId.message}";

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
