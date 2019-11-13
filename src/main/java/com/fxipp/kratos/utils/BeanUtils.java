package com.fxipp.kratos.utils;

import com.fxipp.kratos.function.SFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.lang.invoke.SerializedLambda;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author fangxi
 */
public class BeanUtils {

    private static final Logger log = LoggerFactory.getLogger(BeanUtils.class);

    /**
     * SerializedLambda 反序列化缓存
     */
    private static final Map<Class<?>, WeakReference<SerializedLambda>> FUNC_CACHE = new ConcurrentHashMap<>();

    /**
     * @param source 需要被拷贝的对象
     * @param target 需要返回的对象类型
     */
    public static <T> T copyProperties(Object source, Class<T> target) {
        try {
            Assert.notNull(source, "Source must not be null");
            Assert.notNull(target, "Target must not be null");
            T targetInstance = target.newInstance();
            BeanCopier beanCopier = BeanCopier.create(source.getClass(), target, false);
            beanCopier.copy(source, targetInstance, null);
            //org.springframework.beans.BeanUtils.copyProperties(source, targetInstance);
            return targetInstance;
        } catch (Exception e) {
            log.error("属性拷贝失败", e);
            throw new RuntimeException("属性拷贝失败");
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
    public static <T> String convertToFieldName(SFunction<T> function) {
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
        return (T) JsonUtils.parse(serialize, object.getClass());
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

    private static SerializedLambda resolve(SFunction<?> lambda) {
        try {
            Class<? extends SFunction> clazz = lambda.getClass();
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
