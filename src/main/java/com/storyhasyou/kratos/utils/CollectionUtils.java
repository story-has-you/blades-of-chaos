package com.storyhasyou.kratos.utils;

import org.springframework.lang.Nullable;

import java.util.*;

/**
 * @author fangxi
 */
public class CollectionUtils extends org.springframework.util.CollectionUtils {

    public static <E> List<E> nonNull(List<E> list) {
        return isEmpty(list) ? Collections.emptyList() : list;
    }

    public static <E> Set<E> nonNull(Set<E> set) {
        return isEmpty(set) ? Collections.emptySet() : set;
    }

    public static <K, V> Map<K,V> nonNull(Map<K, V> map) {
        return isEmpty(map) ? Collections.emptyMap() : map;
    }

    public static boolean isNotEmpty(@Nullable Collection<?> collection) {
        return !isEmpty(collection);
    }

    public static boolean isNotEmpty(@Nullable Map<?, ?> map) {
        return !isEmpty(map);
    }


}
