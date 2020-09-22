package com.storyhasyou.kratos.utils;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * The type Collection utils.
 *
 * @author fangxi
 */
public class CollectionUtils extends org.springframework.util.CollectionUtils {

    /**
     * Non null list.
     *
     * @param <E>  the type parameter
     * @param list the list
     * @return the list
     */
    public static <E> List<E> nonNull(List<E> list) {
        return isEmpty(list) ? Collections.emptyList() : list;
    }

    /**
     * Non null set.
     *
     * @param <E> the type parameter
     * @param set the set
     * @return the set
     */
    public static <E> Set<E> nonNull(Set<E> set) {
        return isEmpty(set) ? Collections.emptySet() : set;
    }

    /**
     * Non null map.
     *
     * @param <K> the type parameter
     * @param <V> the type parameter
     * @param map the map
     * @return the map
     */
    public static <K, V> Map<K, V> nonNull(Map<K, V> map) {
        return isEmpty(map) ? Collections.emptyMap() : map;
    }

    /**
     * Is not empty boolean.
     *
     * @param collection the collection
     * @return the boolean
     */
    public static boolean isNotEmpty(@Nullable Collection<?> collection) {
        return !isEmpty(collection);
    }

    /**
     * Is not empty boolean.
     *
     * @param map the map
     * @return the boolean
     */
    public static boolean isNotEmpty(@Nullable Map<?, ?> map) {
        return !isEmpty(map);
    }


    /**
     * 返回list制定的类型
     *
     * @param <E>      the type parameter
     * @param <R>      the type parameter
     * @param source   the source
     * @param function the function
     * @return list list
     */
    public static <E, R> List<R> map(Collection<E> source, Function<E, R> function) {
        Assert.notNull(function, "function must not be null");
        return source.stream().map(function).distinct().collect(Collectors.toList());
    }


    /**
     * Marge list.
     * 提供两个list，根据制定的两个字段比较，如果相同，组成一个新的list返回
     *
     * @param <E>       the type parameter
     * @param <T>       the type parameter
     * @param <R>       the type parameter
     * @param source1   the source 1
     * @param source2   the source 2
     * @param operator1 the operator 1
     * @param operator2 the operator 2
     * @param function  the function
     * @return the list
     */
    public static <E, T, R> List<R> marge(Collection<E> source1, Collection<T> source2,
                                          Function<E, ?> operator1, Function<T, ?> operator2,
                                          BiFunction<E, T, R> function) {

        if (isEmpty(source1) || isEmpty(source2) || ObjectUtils.anyNotNull(operator1, operator2) || function == null) {
            return Collections.emptyList();
        }

        List<R> result = new ArrayList<>(Math.max(source1.size(), source2.size()));
        for (E e : source1) {
            for (T t : source2) {
                if (Objects.equals(operator1.apply(e), operator2.apply(t))) {
                    result.add(function.apply(e, t));
                }
            }
        }
        return result;
    }


    /**
     * 根据条件查找list的某一个元素
     *
     * @param <T>       the type parameter
     * @param source    the source
     * @param predicate the predicate
     * @return the t
     */
    public static <T> T find(Collection<T> source, Predicate<T> predicate) {
        if (isEmpty(source)) {
            return null;
        }
        return source.stream().filter(predicate).findFirst().orElse(null);

    }

}
