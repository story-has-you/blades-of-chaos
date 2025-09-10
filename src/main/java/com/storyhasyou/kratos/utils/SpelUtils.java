package com.storyhasyou.kratos.utils;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.StandardReflectionParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;

/**
 * @author fangxi created by 2023/10/25
 */
public class SpelUtils {

    public static String parse(JoinPoint joinPoint, String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        
        try {
            Object[] args = joinPoint.getArgs();
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            
            // 优先尝试从MethodSignature获取参数名（适用于测试环境）
            String[] paraNameArr = methodSignature.getParameterNames();
            
            // 如果获取不到，则使用Spring的参数名发现器（生产环境）
            if (paraNameArr == null || paraNameArr.length == 0) {
                Method method = methodSignature.getMethod();
                StandardReflectionParameterNameDiscoverer discoverer = new StandardReflectionParameterNameDiscoverer();
                paraNameArr = discoverer.getParameterNames(method);
            }
            
            if (paraNameArr == null || paraNameArr.length == 0) {
                return null;
            }
            
            // 使用SPEL进行key的解析
            ExpressionParser parser = new SpelExpressionParser();
            // SPEL上下文
            StandardEvaluationContext context = new StandardEvaluationContext();
            // 把方法参数放入SPEL上下文中
            for (int i = 0; i < paraNameArr.length && i < args.length; i++) {
                context.setVariable(paraNameArr[i], args[i]);
            }
            
            return parser.parseExpression(key).getValue(context, String.class);
        } catch (Exception e) {
            // 日志记录异常但不抛出，避免破坏业务流程
            return null;
        }
    }
}
