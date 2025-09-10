package com.storyhasyou.kratos.utils;

import cn.hutool.core.util.EnumUtil;
import com.storyhasyou.kratos.enums.BaseEnum;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The type Enum utils.
 *
 * @author fangxi created by 2020/8/31
 */
public class EnumUtils extends EnumUtil {

    /**
     * Instantiates a new Enum utils.
     */
    private EnumUtils() {

    }

    /**
     * 根据int 获取 value
     *
     * @param <T>       the type parameter
     * @param enumClass the enum class
     * @param code      the code
     * @return message message
     */
    public static <T> String getMessage(Class<? extends BaseEnum<T>> enumClass, T code) {
        if (ObjectUtils.isEmpty(enumClass) || ObjectUtils.isEmpty(code)) {
            return null;
        }
        if (!enumClass.isEnum()) {
            throw new IllegalArgumentException("请传入枚举实体类class");
        }

        for (BaseEnum<T> baseEnum : enumClass.getEnumConstants()) {
            if (baseEnum.getCode().equals(code)) {
                return baseEnum.getMessage();
            }
        }
        return null;
    }

    /**
     * To map map.
     *
     * @param <T>       the type parameter
     * @param enumClass the enum class
     * @return the map
     */
    public static <T> Map<T, String> toMap(Class<? extends BaseEnum<T>> enumClass) {
        BaseEnum<T>[] enumConstants = enumClass.getEnumConstants();
        return Arrays.stream(enumConstants).collect(Collectors.toMap(BaseEnum::getCode, BaseEnum::getMessage, (k1, k2) -> k1));
    }

}
