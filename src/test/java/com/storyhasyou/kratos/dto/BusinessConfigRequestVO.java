package com.storyhasyou.kratos.dto;

import lombok.Data;

/**
 * The type Business config request vo.
 *
 * @author fangxi created by 2021/5/12
 */
@Data
public class BusinessConfigRequestVO {

    /**
     * The Terminal id.
     */
    private Long terminalId;

    /**
     * The App version.
     */
    private String appVersion;

    /**
     * The Mpu.
     */
    private String mpu;

    /**
     * The Mcu.
     */
    private String mcu;


}
