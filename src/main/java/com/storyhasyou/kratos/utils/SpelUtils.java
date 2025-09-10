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
        Object[] args = joinPoint.getArgs();
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        // 获取被拦截方法参数名列表(使用Spring支持类库)
        StandardReflectionParameterNameDiscoverer standardReflectionParameterNameDiscoverer = new StandardReflectionParameterNameDiscoverer();
        String[] paraNameArr = standardReflectionParameterNameDiscoverer.getParameterNames(method);
        if (paraNameArr == null || paraNameArr.length == 0) {
            return null;
        }
        // 使用SPEL进行key的解析
        ExpressionParser parser = new SpelExpressionParser();
        // SPEL上下文
        StandardEvaluationContext context = new StandardEvaluationContext();
        // 把方法参数放入SPEL上下文中
        for (int i = 0; i < paraNameArr.length; i++) {
            context.setVariable(paraNameArr[i], args[i]);
        }
        return parser.parseExpression(key).getValue(context, String.class);
    }
}
