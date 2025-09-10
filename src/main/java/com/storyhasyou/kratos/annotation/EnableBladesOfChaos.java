package com.storyhasyou.kratos.annotation;

import com.storyhasyou.kratos.config.BladesOfChaosSelector;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author fangxi created by 2020/11/1
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(BladesOfChaosSelector.class)
public @interface EnableBladesOfChaos {

    AdviceMode mode() default AdviceMode.PROXY;

}
