package com.storyhasyou.kratos.toolkit;

import java.awt.image.BufferedImage;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author fangxi
 */
@Data
@AllArgsConstructor
public class Captcha {

    private String code;
    private BufferedImage image;

}
