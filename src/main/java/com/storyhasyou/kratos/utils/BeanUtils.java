package com.storyhasyou.kratos.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ReflectUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.storyhasyou.kratos.toolkit.StringPool;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.util.Assert;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * The type Bean utils.
 *
 * @author fangxi
 */
public class BeanUtils {

    private static final Map<String, BeanCopier> COPIER_MAP = new ConcurrentHashMap<>(128);

    /**
     * Copy properties t.
     *
     * @param <T>         the type parameter
     * @param source      需要被拷贝的对象
     * @param targetClass 需要返回的对象类型
     * @return the t
     */
    public static <T> T copyProperties(Object source, Class<T> targetClass) {
        Assert.notNull(source, "Source must not be null");
        BeanCopier beanCopier = addOrGetBeanCopier(source.getClass(), targetClass);
        T target = ReflectUtil.newInstance(targetClass);
        beanCopier.copy(source, target, null);
        return target;
    }

    /**
     * Copy list properties list.
     *
     * @param <T>    the type parameter
     * @param <E>    the type parameter
     * @param source the source
     * @param target the target
     * @return the list
     */
    public static <T, E> List<T> copyProperties(Collection<E> source, Class<T> target) {
        Assert.notNull(source, "Source must not be null");
        if (CollectionUtils.isEmpty(source)) {
            return List.of();
        }
        return source.stream().map(e -> copyProperties(e, target)).toList();
    }

    /**
     * Copy properties.
     *
     * @param source 需要被拷贝的对象
     * @param target 目标对象
     */
    public static void copyProperties(Object source, Object target) {
        addOrGetBeanCopier(source.getClass(), target.getClass()).copy(source, target, null);
    }


    /**
     * 深拷贝
     *
     * @param <T>    the type parameter
     * @param object the object
     * @return t t
     */
    public static <T> T deepCopy(Object object) {
        if (object == null) {
            return null;
        }
        String serialize = JacksonUtils.serialize(object);
        return StringUtils.isBlank(serialize) ? null : JacksonUtils.parse(serialize, new TypeReference<>() {
        });
    }


    /**
     * Describe list.
     *
     * @param <T>   the type parameter
     * @param beans the beans
     * @return the list
     */
    public static <T> List<Map<String, Object>> describe(List<T> beans) {
        if (CollectionUtils.isEmpty(beans)) {
            return Collections.emptyList();
        }
        return beans.stream().map(BeanUtils::describe).toList();
    }


    /**
     * 将对象转成Map
     *
     * @param obj the obj
     * @return map map
     */
    public static Map<String, Object> describe(Object obj) {
        return toMap(obj);
    }

    /**
     * 验证某个bean的参数
     *
     * @param <T>    the type parameter
     * @param object 被校验的参数
     */
    public static <T> void validate(T object) {
        // 获得验证器
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        // 执行验证
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(object);
        // 如果有验证信息，则取出来包装成异常返回
        if (CollectionUtils.isNotEmpty(constraintViolations)) {
            throw new ValidationException(convertErrorMsg(constraintViolations));
        }
    }

    public static Map<String, Object> toMap(Object bean) {
        return BeanUtil.beanToMap(bean, false, true);
    }

    public static <T> T mapToBean(Map<String, Object> map, Class<T> tClass) {
        return BeanUtil.mapToBean(map, tClass, false, null);
    }

    /**
     * Convert value t.
     *
     * @param <T>         the type parameter
     * @param fromValue   the from value
     * @param toValueType the to value type
     * @return the t
     */
    public static <T> T convertValue(Object fromValue, TypeReference<T> toValueType) {
        return JacksonUtils.convertValue(fromValue, toValueType);
    }

    /**
     * Convert value t.
     *
     * @param <T>         the type parameter
     * @param fromValue   the from value
     * @param toValueType the to value type
     * @return the t
     */
    public static <T> T convertValue(Object fromValue, Class<T> toValueType) {
        return JacksonUtils.convertValue(fromValue, toValueType);
    }

    /**
     * Convert value t.
     *
     * @param <T>         the type parameter
     * @param fromValue   the from value
     * @param toValueType the to value type
     * @return the t
     */
    public static <T> T convertValue(Object fromValue, JavaType toValueType) {
        return JacksonUtils.convertValue(fromValue, toValueType);
    }

    /**
     * 转换异常信息
     *
     * @param <T> the type parameter
     * @param set the set
     * @return the string
     */
    private static <T> String convertErrorMsg(Set<ConstraintViolation<T>> set) {
        return set.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(StringPool.COMMA));
    }


    private static BeanCopier addOrGetBeanCopier(Class<?> source, Class<?> target) {
        String cacheKey = buildCacheKey(source, target);
        if (COPIER_MAP.containsKey(cacheKey)) {
            return COPIER_MAP.get(cacheKey);
        }

        BeanCopier beanCopier = BeanCopier.create(source, target, false);
        COPIER_MAP.put(cacheKey, beanCopier);
        return beanCopier;
    }

    private static String buildCacheKey(Class<?> clz1, Class<?> clz2) {
        return clz1.getName() + "@" + clz2.getName();
    }


}
