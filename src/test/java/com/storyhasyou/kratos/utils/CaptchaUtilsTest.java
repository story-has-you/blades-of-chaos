package com.storyhasyou.kratos.utils;

import com.storyhasyou.kratos.toolkit.Captcha;
import org.junit.jupiter.api.Test;

/**
 * @author fangxi created by 2020/8/5
 */
public class CaptchaUtilsTest {

    @Test
    public void generateCapatcha() throws Exception {

        Captcha capatcha = CaptchaUtils.generateCapatcha(120, 35, 4, 10);
        System.out.println(capatcha);
    }

}