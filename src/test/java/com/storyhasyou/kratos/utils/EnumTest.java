package com.storyhasyou.kratos.utils;

import com.storyhasyou.kratos.enums.YesNoEnum;
import org.apache.commons.lang3.EnumUtils;
import org.junit.jupiter.api.Test;

import java.util.Map;

/**
 * @author 方曦 created by 2020-8-31
 */
public class EnumTest {

    @Test
    public void test() {

        Map<String, YesNoEnum> enumMap = EnumUtils.getEnumMap(YesNoEnum.class);
    }

}
