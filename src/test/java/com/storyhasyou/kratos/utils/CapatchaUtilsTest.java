package com.storyhasyou.kratos.utils;

import com.storyhasyou.kratos.toolkit.Capatcha;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author fangxi created by 2020/8/5
 */
public class CapatchaUtilsTest {

    @Test
    public void generateCapatcha() throws Exception {

        Capatcha capatcha = CapatchaUtils.generateCapatcha(120, 35, 4, 10);
        System.out.println(capatcha);

    }
}