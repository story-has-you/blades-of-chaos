package com.storyhasyou.kratos.utils;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 方曦 created by 2020-8-31
 */
public class JudgeUtilsTest {

    @Test
    public void process() {
        List<Integer> list = Lists.newArrayList();
        JudgeUtils.process(true, () -> list.add(1));
        JudgeUtils.process(true, () -> list.add(2));
        Console.log(list);
    }

}
