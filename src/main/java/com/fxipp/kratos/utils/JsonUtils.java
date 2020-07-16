package com.fxipp.kratos.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * @author fangxi
 */
@Slf4j
public class JsonUtils {

    private static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();


    static {
        OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        OBJECT_MAPPER.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)));
        OBJECT_MAPPER.registerModule(javaTimeModule).registerModule(new ParameterNamesModule());
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static String serialize(@NonNull Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj.getClass() == String.class) {
            return (String) obj;
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("json序列化出错: {}", obj, e);
            throw new RuntimeException(e);
        }
    }

    public static <T> T parse(@NonNull String json, @NonNull Class<T> tClass) {
        try {
            return OBJECT_MAPPER.readValue(json, tClass);
        } catch (IOException e) {
            log.error("json解析出错: {}", json, e);
            throw new RuntimeException(e);
        }
    }

    public static <E> List<E> parseList(@NonNull String json, @NonNull Class<E> eClass) {
        try {
            return OBJECT_MAPPER.readValue(json, OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, eClass));
        } catch (IOException e) {
            log.error("json解析出错: {}", json, e);
            throw new RuntimeException(e);
        }
    }

    public static <K, V> Map<K, V> parseMap(@NonNull String json, @NonNull Class<K> kClass, @NonNull Class<V> vClass) {
        try {
            return OBJECT_MAPPER.readValue(json, OBJECT_MAPPER.getTypeFactory().constructMapType(Map.class, kClass, vClass));
        } catch (IOException e) {
            log.error("json解析出错: {}", json, e);
            throw new RuntimeException(e);
        }
    }

    public static <T> T nativeRead(@NonNull String json, @NonNull TypeReference<T> type) {
        try {
            return OBJECT_MAPPER.readValue(json, type);
        } catch (IOException e) {
            log.error("json解析出错: {}", json, e);
            throw new RuntimeException(e);
        }
    }

    public static <T> T parse(@NonNull InputStream in, @NonNull Class<T> tClass) {
        try {
            return OBJECT_MAPPER.readValue(in, tClass);
        } catch (IOException e) {
            log.error("json解析出错:", e);
            throw new RuntimeException(e);
        }
    }

    public static <K, V> Map<K, V> parseMap(@NonNull InputStream in, @NonNull Class<K> kClass, @NonNull Class<V> vClass) {
        try {
            return OBJECT_MAPPER.readValue(in, OBJECT_MAPPER.getTypeFactory().constructMapType(Map.class, kClass, vClass));
        } catch (IOException e) {
            log.error("json解析出错:", e);
            throw new RuntimeException(e);
        }
    }
}
