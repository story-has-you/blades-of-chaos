package com.fxipp.kratos.converter;

import com.fxipp.kratos.utils.DateUtils;
import org.springframework.cglib.core.Converter;

import java.time.LocalDateTime;

/**
 * @author fangxi
 * @date 2020/3/2
 */
public class Converters {

    /**
     * String转换器，如果目标是String类型，也会copy进去
     */
    public static final Converter STRING_CONVERTERS = (source, targetClass, method) -> {
        Object result = source;
        if (targetClass.equals(String.class)) {
            result = source.toString();
            if (source instanceof LocalDateTime) {
                result =  DateUtils.dateTimeToString((LocalDateTime) source);
            }
        }
        return result;
    };

}
