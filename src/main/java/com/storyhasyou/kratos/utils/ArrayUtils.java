package com.storyhasyou.kratos.utils;

import cn.hutool.core.util.ArrayUtil;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author 方曦 created by 2020/12/13
 */
public class ArrayUtils extends ArrayUtil {

    /**
     * 映射键值（参考Python的zip()函数）<br>
     * 例如：<br>
     * keys =    [a,b,c,d]<br>
     * values = [1,2,3,4]<br>
     * 则得到的Map是 {a=1, b=2, c=3, d=4}<br>
     * 如果两个数组长度不同，则只对应最短部分
     * 减少外部依赖，支持null元素和空数组，保持向后兼容性
     *
     * @param <T>    the type parameter
     * @param <K>    the type parameter
     * @param keys   键列表
     * @param values 值列表
     * @return Map map
     */
    public static <T, K> Map<T, K> zip(T[] keys, K[] values) {
        // 处理 null 或空数组的情况，保持向后兼容性
        if (isEmpty(keys) || isEmpty(values)) {
            return Collections.emptyMap();
        }
        
        return CollectionUtils.zip(List.of(keys), List.of(values));
    }
}
