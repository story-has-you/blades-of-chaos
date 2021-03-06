package com.storyhasyou.kratos.annotation;

import com.storyhasyou.kratos.config.BladesOfChaosConfig;
import com.storyhasyou.kratos.config.ThreadPoolConfiguration;
import com.storyhasyou.kratos.utils.SpringUtils;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author fangxi created by 2020/11/1
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({BladesOfChaosConfig.class, ThreadPoolConfiguration.class, SpringUtils.class})
public @interface EnableBladesOfChaos {
}
