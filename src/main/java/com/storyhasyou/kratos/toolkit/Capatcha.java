package com.storyhasyou.kratos.toolkit;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.awt.image.BufferedImage;

/**
 * @author fangxi
 */
@Data
@AllArgsConstructor
public class Capatcha {

    private String code;
    private BufferedImage image;

}
