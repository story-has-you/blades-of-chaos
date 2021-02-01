package com.storyhasyou.kratos.utils;

import cn.hutool.captcha.CaptchaUtil;
import com.storyhasyou.kratos.toolkit.Captcha;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The type Captcha utils.
 *
 * @author fangxi created by 2020/8/5
 */
public class CaptchaUtils extends CaptchaUtil {

    /**
     * The constant DIST.
     */
    private static final char[] DIST = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
    };


    /**
     * Instantiates a new Captcha utils.
     */
    private CaptchaUtils() {

    }

    /**
     * 生成4位验证码，默认120x35
     *
     * @return Captcha Captcha
     */
    public static Captcha generate() {
        return generate(120, 35, 4, 10);
    }


    /**
     * 生成验证码
     *
     * @param width  宽度
     * @param height 高度
     * @param length 验证码字符的长度
     * @param line   干扰线行数
     * @return the Captcha
     */
    public static Captcha generate(int width, int height, int length, int line) {
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = bufferedImage.getGraphics();
        // 背景色
        graphics.setColor(randomColor(200, 250));
        graphics.fillRect(0, 0, width, height);
        Font font = new Font("fixedsys", Font.BOLD, height - 5);
        graphics.setFont(font);
        // 生成干扰线
        ThreadLocalRandom current = ThreadLocalRandom.current();
        for (int i = 0; i < line; i++) {
            int xs = current.nextInt(width);
            int ys = current.nextInt(height);
            int xe = xs + current.nextInt(width);
            int ye = ys + current.nextInt(height);
            graphics.setColor(randomColor(1, 255));
            graphics.drawLine(xs, ys, xe, ye);
        }
        // 生成干扰点
        float yawpRate = 0.01F;
        int area = (int) (yawpRate * width * height);
        for (int i = 0; i < area; i++) {
            int x = current.nextInt(width);
            int y = current.nextInt(height);
            bufferedImage.setRGB(x, y, current.nextInt(255));
        }
        // 添加字符
        // 字符所处的y轴的坐标
        int y = height * 3 / 4;
        int codeWidth = width / (length + 1);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            // 计算当前字符绘制的X轴坐标
            int x = (i * codeWidth) + (codeWidth / 2);
            int index = current.nextInt(DIST.length);
            String code = String.valueOf(DIST[index]);
            graphics.drawString(code, x, y);
            sb.append(code);
        }
        return new Captcha(sb.toString(), bufferedImage);
    }

    /**
     * 随机生成制定长度的验证码字符
     *
     * @param length the length
     * @return string string
     */
    private static String randomCode(int length) {
        StringBuilder builder = new StringBuilder();
        ThreadLocalRandom current = ThreadLocalRandom.current();
        int len = DIST.length - 1;
        for (int i = 0; i < length; i++) {
            int index = current.nextInt(len);
            builder.append(DIST[index]);
        }
        return builder.toString();
    }

    /**
     * 随机返回颜色
     *
     * @param min the min
     * @param max the max
     * @return color color
     */
    private static Color randomColor(int min, int max) {
        min = Math.min(min, 255);
        max = Math.min(max, 255);
        ThreadLocalRandom current = ThreadLocalRandom.current();
        int r = current.nextInt(max - min);
        int g = current.nextInt(max - min);
        int b = current.nextInt(max - min);
        return new Color(r, g, b);
    }


}
