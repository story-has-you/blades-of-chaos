package com.storyhasyou.kratos.toolkit;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.awt.image.BufferedImage;

/**
 * The type Capatcha.
 *
 * @author fangxi
 */
@Data
@AllArgsConstructor
public class Capatcha {

    private String code;
    private BufferedImage image;

}
