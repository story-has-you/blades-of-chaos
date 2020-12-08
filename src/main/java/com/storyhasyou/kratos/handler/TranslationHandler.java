package com.storyhasyou.kratos.handler;

import com.storyhasyou.kratos.dto.TranslationResponseDTO;
import com.storyhasyou.kratos.enums.LanguageEnum;
import com.storyhasyou.kratos.utils.CollectionUtils;
import com.storyhasyou.kratos.utils.JsonUtils;
import com.storyhasyou.kratos.utils.OkHttpUtils;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 * The type Translation handler.
 *
 * @author 方曦 created by 2020/12/8
 */
public class TranslationHandler {

    /**
     * The constant YOUDAO_URL.
     */
    private static final String YOUDAO_URL = "https://openapi.youdao.com/api";

    /**
     * The constant APP_KEY.
     */
    public String appKey = "1dd58899d62d5289";
    /**
     * The constant APP_SECRET.
     */
    public String appSecret = "bj7zxfEoxbjENLscRlvf93HdbJhmBpOI";

    private TranslationHandler() {

    }

    /**
     * Instantiates a new Translation handler.
     *
     * @param appKey    the app key
     * @param appSecret the app secret
     */
    public TranslationHandler(String appKey, String appSecret) {
        if (appKey != null) {
            this.appKey = appKey;
        }
        if (appSecret != null) {
            this.appSecret = appSecret;
        }
    }

    /**
     * Builder translation handler . translation handler builder.
     *
     * @return the translation handler . translation handler builder
     */
    public static TranslationHandler.TranslationHandlerBuilder builder() {
        return new TranslationHandler.TranslationHandlerBuilder();
    }

    /**
     * Gets translation dto.
     *
     * @param text the text
     * @return the translation dto
     */
    public TranslationResponseDTO getTranslationDTO(String text) {
        return getTranslationDTO(text, LanguageEnum.AUTO, LanguageEnum.AUTO);
    }

    /**
     * Gets translation dto.
     *
     * @param text the text
     * @param from the from
     * @param to   the to
     * @return the translation dto
     */
    public TranslationResponseDTO getTranslationDTO(String text, LanguageEnum from, LanguageEnum to) {
        Map<String, Object> params = new HashMap<>(10);
        long currentTimeMillis = System.currentTimeMillis();
        String salt = String.valueOf(currentTimeMillis);
        params.put("from", from.getCode());
        params.put("to", to.getCode());
        params.put("signType", "v3");
        String current = String.valueOf(currentTimeMillis / 1000);
        params.put("curtime", current);
        String signStr = appKey + truncate(text) + salt + current + appSecret;
        String sign = getDigest(signStr);
        params.put("appKey", appKey);
        params.put("q", text);
        params.put("salt", salt);
        params.put("sign", sign);
        String response = OkHttpUtils.form(YOUDAO_URL, params);
        if (StringUtils.isNotBlank(response)) {
            return JsonUtils.parse(response, TranslationResponseDTO.class);
        }
        return null;
    }

    /**
     * Translation string.
     *
     * @param text the text
     * @param from the from
     * @param to   the to
     * @return the string
     */
    public String translation(String text, LanguageEnum from, LanguageEnum to) {
        TranslationResponseDTO translationResponseDTO = getTranslationDTO(text, from, to);
        if (translationResponseDTO == null) {
            return null;
        }
        List<String> translation = translationResponseDTO.getTranslation();
        if (CollectionUtils.isEmpty(translation)) {
            return null;
        }
        return translation.stream().findFirst().orElse(null);
    }

    /**
     * Translation string.
     *
     * @param text the text
     * @return the string
     */
    public String translation(String text) {
        return translation(text, LanguageEnum.AUTO, LanguageEnum.AUTO);
    }

    /**
     * Truncate string.
     *
     * @param q the q
     * @return the string
     */
    private String truncate(String q) {
        if (q == null) {
            return null;
        }
        int len = q.length();
        return len <= 20 ? q : (q.substring(0, 10) + len + q.substring(len - 10, len));
    }

    /**
     * 生成加密字段
     *
     * @param string the string
     * @return the digest
     */
    private String getDigest(String string) {
        if (string == null) {
            return null;
        }
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        byte[] btInput = string.getBytes(StandardCharsets.UTF_8);
        try {
            MessageDigest mdInst = MessageDigest.getInstance("SHA-256");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    /**
     * The type Translation handler builder.
     */
    public static class TranslationHandlerBuilder {
        private String appKey;
        private String appSecret;

        /**
         * Instantiates a new Translation handler builder.
         */
        TranslationHandlerBuilder() {
        }

        /**
         * App key translation handler . translation handler builder.
         *
         * @param appKey the app key
         * @return the translation handler . translation handler builder
         */
        public TranslationHandler.TranslationHandlerBuilder appKey(String appKey) {
            this.appKey = appKey;
            return this;
        }

        /**
         * App secret translation handler . translation handler builder.
         *
         * @param appSecret the app secret
         * @return the translation handler . translation handler builder
         */
        public TranslationHandler.TranslationHandlerBuilder appSecret(String appSecret) {
            this.appSecret = appSecret;
            return this;
        }

        /**
         * Build translation handler.
         *
         * @return the translation handler
         */
        public TranslationHandler build() {
            return new TranslationHandler(this.appKey, this.appSecret);
        }

    }

}
