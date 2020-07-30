package com.storyhasyou.kratos.utils;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

/**
 * @author fangxi
 * @date 2020/3/27
 * @since 1.0.0
 */
public class NumberUtilsTest {

    @Test
    public void decimal2Minute() {
        int minute = NumberUtils.decimal2Minute(BigDecimal.ONE);
        System.out.println(minute);
    }

    @Test
    public void minute2Decimal() {
        BigDecimal bigDecimal = NumberUtils.minute2Decimal(10051);
        System.out.println(bigDecimal);
    }

}
