package com.storyhasyou.kratos.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.util.Assert;

import java.lang.invoke.SerializedLambda;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The type Bean utils.
 *
 * @author fangxi
 */
@Slf4j
public class BeanUtils extends org.springframework.beans.BeanUtils {

    /**
     * SerializedLambda 反序列化缓存
     */
    private static final Map<Class<?>, WeakReference<SerializedLambda>> FUNC_CACHE = new ConcurrentHashMap<>(1 << 8);


    /**
     * Copy properties t.
     *
     * @param <T>    the type parameter
     * @param source 需要被拷贝的对象
     * @param target 需要返回的对象类型
     * @return the t
     */
    public static <T> T copyProperties(Object source, Class<T> target) {
        try {
            Assert.notNull(source, "Source must not be null");
            Assert.notNull(target, "Target must not be null");
            final Constructor<T> constructor = target.getConstructor();
            final T targetInstance = constructor.newInstance();
            BeanCopier beanCopier = BeanCopier.create(source.getClass(), target, false);
            beanCopier.copy(source, targetInstance, null);
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
    public static <T, E> List<T> copyProperties(Collection<E> source, Class<T> target) {
        if (CollectionUtils.isEmpty(source)) {
            return Collections.emptyList();
        }
        return source.stream().map(x -> copyProperties(x, target)).collect(Collectors.toList());
    }

    /**
     * Copy properties.
     *
     * @param source 需要被拷贝的对象
     * @param target 目标对象
     */
    public static void copyProperties(Object source, Object target) {
        BeanCopier beanCopier = BeanCopier.create(source.getClass(), target.getClass(), false);
        beanCopier.copy(source, target, null);
    }


    /**
     * 通过Lambda的get方法引用拿到私有属性名
     *
     * @param <T>      字段类型
     * @param function lambda表达式
     * @return 返回字段名 string
     */
    public static <T> String convertToFieldName(Function<T, ?> function) {
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

    /**
     * 深拷贝
     *
     * @param <T>    the type parameter
     * @param object the object
     * @return t t
     */
    public static <T> T deepCopy(T object) {
        String serialize = JsonUtils.serialize(object);
        return JsonUtils.parse(serialize, new TypeReference<T>() {});
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
        return beans.stream().map(BeanUtils::describe).collect(Collectors.toList());
    }


    /**
     * 将对象转成Map
     *
     * @param obj the obj
     * @return map map
     */
    public static Map<String, Object> describe(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            Class<?> clazz = obj.getClass();
            Field[] fields = clazz.getDeclaredFields();
            Map<String, Object> result = new HashMap<>(fields.length);
            for (Field field : fields) {
                field.setAccessible(true);
                Object value = field.get(obj);
                if (value != null) {
                    result.put(field.getName(), value);
                }
            }
            return result;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
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

    private static <T> SerializedLambda resolve(Function<T, ?> lambda) {
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
