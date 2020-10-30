package com.storyhasyou.kratos.utils;

import cn.hutool.core.lang.Console;
import org.apache.commons.lang3.ObjectUtils;
import org.junit.jupiter.api.Test;

/**
 * @author fangxi created by 2020/7/31
 */
public class ConsoleTest {

    @Test
    public void log() throws Exception {
        Console.log();
    }

    @Test
    public void testLog() throws Exception {
        System.out.println(ObjectUtils.allNotNull(null, null));
    }


}