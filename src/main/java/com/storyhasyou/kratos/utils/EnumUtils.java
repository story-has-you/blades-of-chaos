package com.storyhasyou.kratos.utils;

import cn.hutool.core.util.EnumUtil;
import com.storyhasyou.kratos.enums.BaseEnum;

import java.lang.reflect.Field;

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
    public static String getMessage(Class<? extends BaseEnum<Integer>> enumClass, int code) {
        Field[] fields = enumClass.getFields();
        try {
            for (Field field : fields) {
                BaseEnum<?> baseEnum = (BaseEnum<?>) field.get(null);
                if (baseEnum.getCode().equals(code)) {
                    return baseEnum.getMessage();
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
