package com.fxipp.kratos.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fxipp.kratos.function.SFunction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.core.Converter;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.lang.invoke.SerializedLambda;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author fangxi
 */
@Slf4j
public class BeanUtils {

    /**
     * SerializedLambda 反序列化缓存
     */
    private static final Map<Class<?>, WeakReference<SerializedLambda>> FUNC_CACHE = new ConcurrentHashMap<>(1 << 8);

    /**
     * @param source 需要被拷贝的对象
     * @param target 需要返回的对象类型
     */
    public static <T> T copyProperties(Object source, Class<T> target) {
        return copyProperties(source, target, null);
    }

    /**
     * @param source 需要被拷贝的对象
     * @param target 需要返回的对象类型
     * @param converter 转换器
     */
    public static <T> T copyProperties(Object source, Class<T> target, Converter converter) {
        try {
            Assert.notNull(source, "Source must not be null");
            Assert.notNull(target, "Target must not be null");
            T targetInstance = target.newInstance();
            BeanCopier beanCopier = BeanCopier.create(source.getClass(), target, converter != null);
            beanCopier.copy(source, targetInstance, converter);
            //org.springframework.beans.BeanUtils.copyProperties(source, targetInstance);
            return targetInstance;
        } catch (Exception e) {
            log.error("属性拷贝失败", e);
            throw new RuntimeException(e);
        }
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
    public static <T, E> List<T> copyProperties(Collection<E> source, Class<T> target,Converter converter) {
        if (CollectionUtils.isEmpty(source)) {
            return Collections.emptyList();
        }
        return source.stream().map(x -> copyProperties(x, target, converter)).collect(Collectors.toList());
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
        if (CollectionUtils.isEmpty(source)) {
            return Collections.emptyList();
        }
        return source.stream().map(x -> copyProperties(x, target)).collect(Collectors.toList());
    }

    public static void copyProperties(Object source, Object target) {
        BeanCopier beanCopier = BeanCopier.create(source.getClass(), target.getClass(), false);
        beanCopier.copy(source, target, null);
    }


    /**
     * 通过Lambda的get方法引用拿到私有属性名
     * @param function lambda表达式
     * @param <T> 字段类型
     * @return 返回字段名
     */
    public static <T> String convertToFieldName(SFunction<T, ?> function) {
        Class<?> clazz = function.getClass();
        return Optional.ofNullable(FUNC_CACHE.get(clazz))
                .map(WeakReference::get)
                .map(SerializedLambda::getImplMethodName)
                .map(BeanUtils::remvoeFrefix)
                .orElseGet(() -> {
                    SerializedLambda lambda = resolve(function);
                    String methodName = lambda.getImplMethodName();
                    FUNC_CACHE.put(clazz, new WeakReference<>(lambda));
                    return remvoeFrefix(methodName);
                });

    }

    public static <T> T deepCopy(T object) {
        String serialize = JsonUtils.serialize(object);
        return JsonUtils.nativeRead(serialize, new TypeReference<T>(){});
    }

    public static Map<String, Object> describe(Object obj) {
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        Map<String, Object> result = new HashMap<>(fields.length);
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(obj);
                result.put(field.getName(), value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private static String remvoeFrefix(String methodName) {
        String prefix = null;
        if (methodName.startsWith("get")) {
            prefix = "get";
        } else if (methodName.startsWith("is")) {
            prefix = "is";
        }
        if (prefix == null) {
            throw new RuntimeException("无效的getter方法: " + methodName);
        }
        String replace = methodName.replace(prefix, "");
        if (Character.isLowerCase(replace.charAt(0))) {
            return replace;
        } else {
            return Character.toLowerCase(replace.charAt(0)) + replace.substring(1);
        }
    }

    private static <T> SerializedLambda resolve(SFunction<T, ?> lambda) {
        try {
            Class<?> clazz = lambda.getClass();
            if (!clazz.isSynthetic()) {
                throw new RuntimeException("该方法仅能传入 lambda 表达式产生的合成类");
            }
            // 提取SerializedLambda并缓存
            Method method = lambda.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(Boolean.TRUE);
            return (SerializedLambda) method.invoke(lambda);
        } catch (Exception e) {
            log.error("解析 lambda 失败", e);
            throw new RuntimeException("解析 lambda 失败");
        }
    }
}
