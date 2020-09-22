package com.storyhasyou.kratos.utils;

import cn.hutool.core.util.EnumUtil;
import com.storyhasyou.kratos.enums.BaseEnum;
import org.apache.commons.lang3.ObjectUtils;

/**
 * The type Enum utils.
 *
 * @author fangxi created by 2020/8/31
 */
public class EnumUtils extends EnumUtil {

    private EnumUtils() {

    }

    /**
     * 根据int 获取 value
     *
     * @param enumClass the enum class
     * @param code      the code
     * @return message
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

}
