package com.storyhasyou.kratos.utils;

import cn.hutool.core.lang.Console;
import com.storyhasyou.kratos.result.ResultCode;
import org.junit.jupiter.api.Test;

/**
 * @author fangxi created by 2020/9/10
 */
public class EnumUtilsTest {

    @Test
    public void getMessage() throws Exception {
        String message = EnumUtils.getMessage(ResultCode.class, 200);
        Console.log(message);
    }
}