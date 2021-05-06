package com.storyhasyou.kratos.utils;

import cn.hutool.core.util.ArrayUtil;
import com.google.common.collect.Lists;

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
     *
     * @param <T>    the type parameter
     * @param <K>    the type parameter
     * @param keys   键列表
     * @param values 值列表
     * @return Map map
     */
    public static <T, K> Map<T, K> zip(T[] keys, K[] values) {
        return CollectionUtils.zip(Lists.newArrayList(keys), Lists.newArrayList(values));
    }
}
