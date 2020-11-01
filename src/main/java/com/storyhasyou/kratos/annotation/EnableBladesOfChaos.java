package com.storyhasyou.kratos.annotation;

import com.storyhasyou.kratos.config.BladesOfChaosConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author fangxi created by 2020/11/1
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(BladesOfChaosConfig.class)
public @interface EnableBladesOfChaos {
}
