package com.storyhasyou.kratos.toolkit;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.storyhasyou.kratos.annotation.Sensitive;
import com.storyhasyou.kratos.enums.SensitiveTypeEnum;
import com.storyhasyou.kratos.utils.SensitiveUtils;

import java.io.IOException;
import java.util.Objects;

/**
 * The type Sensitive serialize.
 *
 * @author fangxi
 */
public class SensitiveSerialize extends JsonSerializer<Object> implements ContextualSerializer {

    private final SensitiveTypeEnum type;

    /**
     * Instantiates a new Sensitive serialize.
     *
     * @param type the type
     */
    public SensitiveSerialize(final SensitiveTypeEnum type) {
        this.type = type;
    }

    /**
     * Instantiates a new Sensitive serialize.
     */
    public SensitiveSerialize() {
        this.type = SensitiveTypeEnum.NAME;
    }


    @Override
    public void serialize(Object value, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
        switch (this.type) {
            case ID_CARD -> jsonGenerator.writeString(SensitiveUtils.idCard(String.valueOf(value)));
            case MOBILE_PHONE -> jsonGenerator.writeString(SensitiveUtils.mobilePhone(String.valueOf(value)));
            case EMAIL -> jsonGenerator.writeString(SensitiveUtils.email(String.valueOf(value)));
            case ACCOUNT_NO -> jsonGenerator.writeString(SensitiveUtils.acctNo(String.valueOf(value)));
            case PASSWORD -> jsonGenerator.writeString(SensitiveUtils.password(String.valueOf(value)));
            case NAME -> jsonGenerator.writeString(SensitiveUtils.realName(String.valueOf(value)));
            default -> jsonGenerator.writeString(String.valueOf(value));
        }

    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {

        if (beanProperty != null) {

            // 非 String 类直接跳过
            if (Objects.equals(beanProperty.getType().getRawClass(), String.class)) {
                Sensitive sensitiveInfo = beanProperty.getAnnotation(Sensitive.class);
                if (sensitiveInfo == null) {
                    sensitiveInfo = beanProperty.getContextAnnotation(Sensitive.class);
                }
                // 如果能得到注解，就将注解的 value 传入 SensitiveInfoSerialize
                if (sensitiveInfo != null) {

                    return new SensitiveSerialize(sensitiveInfo.value());
                }
            }
            return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
        }
        return serializerProvider.findNullValueSerializer(beanProperty);

    }
}
