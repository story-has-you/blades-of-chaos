package com.fxipp.kratos.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

/**
 * @author fangxi
 */
@Slf4j
public class CommonUtils {

    private CommonUtils() {
    }

    /**
     * 生成UUID
     *
     * @return uuid
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
