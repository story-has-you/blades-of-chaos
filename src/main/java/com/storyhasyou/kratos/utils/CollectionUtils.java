package com.storyhasyou.kratos.utils;

import cn.hutool.core.collection.CollectionUtil;
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
public class CollectionUtils extends CollectionUtil {

    /**
     * Return {@code true} if the supplied Collection is {@code null} or empty.
     * Otherwise, return {@code false}.
     *
     * @param collection the Collection to check
     * @return whether the given Collection is empty
     */
    public static boolean isEmpty(@Nullable Collection<?> collection) {
        return (collection == null || collection.isEmpty());
    }

    /**
     * Return {@code true} if the supplied Map is {@code null} or empty.
     * Otherwise, return {@code false}.
     *
     * @param map the Map to check
     * @return whether the given Map is empty
     */
    public static boolean isEmpty(@Nullable Map<?, ?> map) {
        return (map == null || map.isEmpty());
    }

    /**
     * Convert the supplied array into a List. A primitive array gets converted
     * into a List of the appropriate wrapper type.
     * <p><b>NOTE:</b> Generally prefer the standard {@link Arrays#asList} method.
     * This {@code arrayToList} method is just meant to deal with an incoming Object
     * value that might be an {@code Object[]} or a primitive array at runtime.
     * <p>A {@code null} source value will be converted to an empty List.
     *
     * @param source the (potentially primitive) array
     * @return the converted List result
     * @see org.springframework.util.ObjectUtils#toObjectArray(Object) org.springframework.util.ObjectUtils#toObjectArray(Object)org.springframework.util .ObjectUtils#toObjectArray(Object)
     * @see Arrays#asList(Object[]) Arrays#asList(Object[])Arrays#asList(Object[])
     */
    @SuppressWarnings("rawtypes")
    public static List arrayToList(@Nullable Object source) {
        return Arrays.asList(org.springframework.util.ObjectUtils.toObjectArray(source));
    }

    /**
     * Merge the given array into the given Collection.
     *
     * @param <E>        the type parameter
     * @param array      the array to merge (may be {@code null})
     * @param collection the target Collection to merge the array into
     */
    @SuppressWarnings("unchecked")
    public static <E> void mergeArrayIntoCollection(@Nullable Object array, Collection<E> collection) {
        Object[] arr = org.springframework.util.ObjectUtils.toObjectArray(array);
        for (Object elem : arr) {
            collection.add((E) elem);
        }
    }

    /**
     * Merge the given Properties instance into the given Map,
     * copying all properties (key-value pairs) over.
     * <p>Uses {@code Properties.propertyNames()} to even catch
     * default properties linked into the original Properties instance.
     *
     * @param <K>   the type parameter
     * @param <V>   the type parameter
     * @param props the Properties instance to merge (may be {@code null})
     * @param map   the target Map to merge the properties into
     */
    @SuppressWarnings("unchecked")
    public static <K, V> void mergePropertiesIntoMap(@Nullable Properties props, Map<K, V> map) {
        if (props != null) {
            for (Enumeration<?> en = props.propertyNames(); en.hasMoreElements(); ) {
                String key = (String) en.nextElement();
                Object value = props.get(key);
                if (value == null) {
                    // Allow for defaults fallback or potentially overridden accessor...
                    value = props.getProperty(key);
                }
                map.put((K) key, (V) value);
            }
        }
    }


    /**
     * Check whether the given Iterator contains the given element.
     *
     * @param iterator the Iterator to check
     * @param element  the element to look for
     * @return {@code true} if found, {@code false} otherwise
     */
    public static boolean contains(@Nullable Iterator<?> iterator, Object element) {
        if (iterator != null) {
            while (iterator.hasNext()) {
                Object candidate = iterator.next();
                if (org.springframework.util.ObjectUtils.nullSafeEquals(candidate, element)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Check whether the given Enumeration contains the given element.
     *
     * @param enumeration the Enumeration to check
     * @param element     the element to look for
     * @return {@code true} if found, {@code false} otherwise
     */
    public static boolean contains(@Nullable Enumeration<?> enumeration, Object element) {
        if (enumeration != null) {
            while (enumeration.hasMoreElements()) {
                Object candidate = enumeration.nextElement();
                if (org.springframework.util.ObjectUtils.nullSafeEquals(candidate, element)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Check whether the given Collection contains the given element instance.
     * <p>Enforces the given instance to be present, rather than returning
     * {@code true} for an equal element as well.
     *
     * @param collection the Collection to check
     * @param element    the element to look for
     * @return {@code true} if found, {@code false} otherwise
     */
    public static boolean containsInstance(@Nullable Collection<?> collection, Object element) {
        if (collection != null) {
            for (Object candidate : collection) {
                if (candidate == element) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Return {@code true} if any element in '{@code candidates}' is
     * contained in '{@code source}'; otherwise returns {@code false}.
     *
     * @param source     the source Collection
     * @param candidates the candidates to search for
     * @return whether any of the candidates has been found
     */
    public static boolean containsAny(Collection<?> source, Collection<?> candidates) {
        return findFirstMatch(source, candidates) != null;
    }

    /**
     * Return the first element in '{@code candidates}' that is contained in
     * '{@code source}'. If no element in '{@code candidates}' is present in
     * '{@code source}' returns {@code null}. Iteration order is
     * {@link Collection} implementation specific.
     *
     * @param <E>        the type parameter
     * @param source     the source Collection
     * @param candidates the candidates to search for
     * @return the first present object, or {@code null} if not found
     */
    @Nullable
    public static <E> E findFirstMatch(Collection<?> source, Collection<E> candidates) {
        if (isEmpty(source) || isEmpty(candidates)) {
            return null;
        }
        for (E candidate : candidates) {
            if (source.contains(candidate)) {
                return candidate;
            }
        }
        return null;
    }

    /**
     * Find a single value of the given type in the given Collection.
     *
     * @param <T>        the type parameter
     * @param collection the Collection to search
     * @param type       the type to look for
     * @return a value of the given type found if there is a clear match, or {@code null} if none or more than one such value found
     */
    @SuppressWarnings("unchecked")
    @Nullable
    public static <T> T findValueOfType(Collection<?> collection, @Nullable Class<T> type) {
        if (isEmpty(collection)) {
            return null;
        }
        T value = null;
        for (Object element : collection) {
            if (type == null || type.isInstance(element)) {
                if (value != null) {
                    // More than one value found... no clear single value.
                    return null;
                }
                value = (T) element;
            }
        }
        return value;
    }

    /**
     * Find a single value of one of the given types in the given Collection:
     * searching the Collection for a value of the first type, then
     * searching for a value of the second type, etc.
     *
     * @param collection the collection to search
     * @param types      the types to look for, in prioritized order
     * @return a value of one of the given types found if there is a clear match, or {@code null} if none or more than one such value found
     */
    @Nullable
    public static Object findValueOfType(Collection<?> collection, Class<?>[] types) {
        if (isEmpty(collection) || org.springframework.util.ObjectUtils.isEmpty(types)) {
            return null;
        }
        for (Class<?> type : types) {
            Object value = findValueOfType(collection, type);
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    /**
     * Determine whether the given Collection only contains a single unique object.
     *
     * @param collection the Collection to check
     * @return {@code true} if the collection contains a single reference or multiple references to the same instance, {@code false} otherwise
     */
    public static boolean hasUniqueObject(Collection<?> collection) {
        if (isEmpty(collection)) {
            return false;
        }
        boolean hasCandidate = false;
        Object candidate = null;
        for (Object elem : collection) {
            if (!hasCandidate) {
                hasCandidate = true;
                candidate = elem;
            } else if (candidate != elem) {
                return false;
            }
        }
        return true;
    }

    /**
     * Find the common element type of the given Collection, if any.
     *
     * @param collection the Collection to check
     * @return the common element type, or {@code null} if no clear common type has been found (or the collection was empty)
     */
    @Nullable
    public static Class<?> findCommonElementType(Collection<?> collection) {
        if (isEmpty(collection)) {
            return null;
        }
        Class<?> candidate = null;
        for (Object val : collection) {
            if (val != null) {
                if (candidate == null) {
                    candidate = val.getClass();
                } else if (candidate != val.getClass()) {
                    return null;
                }
            }
        }
        return candidate;
    }

    /**
     * Retrieve the first element of the given Set, using {@link SortedSet#first()}
     * or otherwise using the iterator.
     *
     * @param <T> the type parameter
     * @param set the Set to check (may be {@code null} or empty)
     * @return the first element, or {@code null} if none
     * @see SortedSet
     * @see LinkedHashMap#keySet() LinkedHashMap#keySet()LinkedHashMap#keySet()
     * @see java.util.LinkedHashSet
     * @since 5.2.3
     */
    @Nullable
    public static <T> T firstElement(@Nullable Set<T> set) {
        if (isEmpty(set)) {
            return null;
        }
        if (set instanceof SortedSet) {
            return ((SortedSet<T>) set).first();
        }

        Iterator<T> it = set.iterator();
        T first = null;
        if (it.hasNext()) {
            first = it.next();
        }
        return first;
    }

    /**
     * Retrieve the first element of the given List, accessing the zero index.
     *
     * @param <T>  the type parameter
     * @param list the List to check (may be {@code null} or empty)
     * @return the first element, or {@code null} if none
     * @since 5.2.3
     */
    @Nullable
    public static <T> T firstElement(@Nullable List<T> list) {
        if (isEmpty(list)) {
            return null;
        }
        return list.getFirst();
    }

    /**
     * Retrieve the last element of the given Set, using {@link SortedSet#last()}
     * or otherwise iterating over all elements (assuming a linked set).
     *
     * @param <T> the type parameter
     * @param set the Set to check (may be {@code null} or empty)
     * @return the last element, or {@code null} if none
     * @see SortedSet
     * @see LinkedHashMap#keySet() LinkedHashMap#keySet()LinkedHashMap#keySet()
     * @see java.util.LinkedHashSet
     * @since 5.0.3
     */
    @Nullable
    public static <T> T lastElement(@Nullable Set<T> set) {
        if (isEmpty(set)) {
            return null;
        }
        if (set instanceof SortedSet) {
            return ((SortedSet<T>) set).last();
        }

        // Full iteration necessary...
        Iterator<T> it = set.iterator();
        T last = null;
        while (it.hasNext()) {
            last = it.next();
        }
        return last;
    }

    /**
     * Retrieve the last element of the given List, accessing the highest index.
     *
     * @param <T>  the type parameter
     * @param list the List to check (may be {@code null} or empty)
     * @return the last element, or {@code null} if none
     * @since 5.0.3
     */
    @Nullable
    public static <T> T lastElement(@Nullable List<T> list) {
        if (isEmpty(list)) {
            return null;
        }
        return list.getLast();
    }

    /**
     * Marshal the elements from the given enumeration into an array of the given type.
     * Enumeration elements must be assignable to the type of the given array. The array
     * returned will be a different instance than the array given.
     *
     * @param <A>         the type parameter
     * @param <E>         the type parameter
     * @param enumeration the enumeration
     * @param array       the array
     * @return the a [ ]
     */
    public static <A, E extends A> A[] toArray(Enumeration<E> enumeration, A[] array) {
        ArrayList<A> elements = new ArrayList<>();
        while (enumeration.hasMoreElements()) {
            elements.add(enumeration.nextElement());
        }
        return elements.toArray(array);
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
        Assert.notNull(source, "Source must not be null");
        Assert.notNull(function, "function must not be null");
        return source.stream().map(function).toList();
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
    public static <E, T, R> List<R> merge(Collection<E> source1, Collection<T> source2,
                                          Function<E, ?> operator1, Function<T, ?> operator2,
                                          BiFunction<E, T, R> function) {

        Assert.notNull(function, "function must not be null");
        Assert.notNull(operator1, "operator1 must not be null");
        Assert.notNull(operator2, "operator2 must not be null");
        Assert.notEmpty(source1, "source1 must not be null");
        Assert.notEmpty(source2, "source2 must not be null");
        List<R> result = new ArrayList<>(Math.max(source1.size(), source2.size()));
        Map<?, E> eMap = source1.stream().collect(Collectors.toMap(operator1, Function.identity(), (e, e2) -> e2));
        source2.forEach(t -> {
            Object apply = operator2.apply(t);
            if (Objects.nonNull(apply) && eMap.containsKey(apply)) {
                E e = eMap.get(apply);
                result.add(function.apply(e, t));
            }
        });
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
    public static <T> Optional<T> find(Collection<T> source, Predicate<T> predicate) {
        if (isEmpty(source) || predicate == null) {
            return Optional.empty();
        }
        return source.stream().filter(predicate).findFirst();

    }

    /**
     * 根据条件查找list的某一个元素
     *
     * @param <T>       the type parameter
     * @param <R>       the type parameter
     * @param source    the source
     * @param predicate the predicate
     * @param function  the function
     * @return the t
     */
    public static <T, R> R find(Collection<T> source, Predicate<T> predicate, Function<T, R> function) {
        if (isEmpty(source) || predicate == null || function == null) {
            return null;
        }
        return source.stream().filter(predicate).findFirst().map(function).orElse(null);

    }

    /**
     * 映射键值（参考Python的zip()函数）<br>
     * 例如：<br>
     * keys =    [a,b,c,d]<br>
     * values = [1,2,3,4]<br>
     * 则得到的Map是 {a=1, b=2, c=3, d=4}<br>
     * 如果两个数组长度不同，则只对应最短部分
     *
     * @param <T>    the type parameter
     * @param <K>    the type parameter
     * @param keys   键列表
     * @param values 值列表
     * @return Map map
     */
    public static <T, K> Map<T, K> zip(Collection<T> keys, Collection<K> values) {
        if (isEmpty(keys) || isEmpty(values)) {
            return new HashMap<>();
        }

        final List<T> keyList = new ArrayList<>(keys);
        final List<K> valueList = new ArrayList<>(values);

        final int size = Math.min(keys.size(), values.size());
        final Map<T, K> map = new HashMap<>((int) (size / 0.75));
        for (int i = 0; i < size; i++) {
            map.put(keyList.get(i), valueList.get(i));
        }
        return map;
    }

    /**
     * 将list转成一对一的map
     *
     * @param <K>      the type parameter
     * @param <V>      the type parameter
     * @param source   the source
     * @param function the function
     * @return map
     */
    public static <K, V> Map<K, V> toMap(Collection<V> source, Function<V, K> function) {
        Assert.notEmpty(source, "source must not null");
        return source.stream().collect(Collectors.toMap(function, Function.identity()));
    }

    /**
     * 分组
     *
     * @param <K>      the type parameter
     * @param <V>      the type parameter
     * @param source   the source
     * @param function the function
     * @return map
     */
    public static <K, V> Map<K, List<V>> grouping(Collection<V> source, Function<V, K> function) {
        Assert.notEmpty(source, "source must not null");
        return source.stream().collect(Collectors.groupingBy(function));
    }


}
