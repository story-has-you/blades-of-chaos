package com.fxipp.kratos.utils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

}
