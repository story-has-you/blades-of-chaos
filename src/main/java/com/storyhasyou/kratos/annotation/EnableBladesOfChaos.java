package com.storyhasyou.kratos.annotation;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import com.spring4all.swagger.EnableSwagger2Doc;
import com.storyhasyou.kratos.config.BladesOfChaosConfig;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;

/**
 * @author fangxi created by 2020/11/1
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(BladesOfChaosConfig.class)
@EnableSwaggerBootstrapUI
@EnableSwagger2Doc
public @interface EnableBladesOfChaos {
}
