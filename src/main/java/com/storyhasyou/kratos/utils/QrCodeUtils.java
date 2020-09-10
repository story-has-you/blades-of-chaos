package com.storyhasyou.kratos.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Qr code utils.
 *
 * @author fangxi
 * @date 2020 /3/23
 * @since 1.0.0
 */
@Slf4j
public class QrCodeUtils {

    /**
     * Generate qr code byte [ ].
     *
     * @param content the content
     * @param width   the width
     * @param height  the height
     * @return the byte [ ]
     */
    public static byte[] generateQrCode(String content, int width, int height) {
        try {
            Map<EncodeHintType, Object> hintMap = new HashMap<>(2);
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            hintMap.put(EncodeHintType.CHARACTER_SET, StandardCharsets.UTF_8.name());
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hintMap);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "png", outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            log.error("生成二维码失败", e);
        }
        return null;
    }


    /**
     * 生成二维码并写到对应的输出流,  默认400 x 400
     *
     * @param content the content
     * @return the byte [ ]
     */
    public static byte[] generateQrCode(String content) {
        return generateQrCode(content, 400, 400);
    }


}
