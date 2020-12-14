package com.storyhasyou.kratos.utils;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.storyhasyou.kratos.base.BaseEntity;
import com.storyhasyou.kratos.toolkit.DatePattern;
import java.text.SimpleDateFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * The type Json utils.
 *
 * @author fangxi
 */
@Slf4j
public class JacksonUtils {

    private static ObjectMapper OBJECT_MAPPER = new ObjectMapper();


    static {
        OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        OBJECT_MAPPER.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN)));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN)));
        OBJECT_MAPPER.registerModule(javaTimeModule).registerModule(new ParameterNamesModule());
        OBJECT_MAPPER.setDateFormat(new SimpleDateFormat(DatePattern.NORM_DATETIME_PATTERN));
        // 忽略多余字段
        OBJECT_MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        //在JSON中允许未引用的字段名
        OBJECT_MAPPER.enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES);
        //识别单引号
        OBJECT_MAPPER.enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
        //允许单个数值当做数组处理
        OBJECT_MAPPER.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        //禁止重复键, 抛出异常
        OBJECT_MAPPER.enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);
    }


    /**
     * Gets object mapper.
     *
     * @return the object mapper
     */
    public static ObjectMapper getObjectMapper() {
        return JacksonUtils.OBJECT_MAPPER;
    }

    /**
     * Sets object mapper.
     *
     * @param objectMapper the object mapper
     */
    public static void setObjectMapper(ObjectMapper objectMapper) {
        JacksonUtils.OBJECT_MAPPER = objectMapper;
    }

    /**
     * To bytes byte [ ].
     *
     * @param obj the obj
     * @return the byte [ ]
     */
    public static byte[] toBytes(@NonNull Object obj) {
        try {
            return OBJECT_MAPPER.writeValueAsBytes(obj);
        } catch (JsonProcessingException e) {
            log.error("json序列化出错: {}", obj, e);
        }
        return null;
    }

    /**
     * Serialize string.
     *
     * @param obj the obj
     * @return the string
     */
    public static String serialize(@NonNull Object obj) {
        if (obj.getClass() == String.class) {
            return (String) obj;
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("json序列化出错: {}", obj, e);
        }
        return null;
    }

    /**
     * Parse t.
     *
     * @param <T>    the type parameter
     * @param bytes   the bytes
     * @param tClass the t class
     * @return the t
     */
    public static <T> T parse(@NonNull byte[] bytes, @NonNull Class<T> tClass) {
        try {
            return OBJECT_MAPPER.readValue(bytes, tClass);
        } catch (IOException e) {
            log.error("json解析出错: {}", bytes, e);
        }
        return null;
    }


    /**
     * Parse t.
     *
     * @param <T>    the type parameter
     * @param json   the json
     * @param tClass the t class
     * @return the t
     */
    public static <T> T parse(@NonNull String json, @NonNull Class<T> tClass) {
        try {
            return OBJECT_MAPPER.readValue(json, tClass);
        } catch (IOException e) {
            log.error("json解析出错: {}", json, e);
        }
        return null;
    }

    /**
     * Parse list list.
     *
     * @param <E>    the type parameter
     * @param json   the json
     * @param eClass the e class
     * @return the list
     */
    public static <E> List<E> parseList(@NonNull String json, @NonNull Class<E> eClass) {
        try {
            return OBJECT_MAPPER.readValue(json, OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, eClass));
        } catch (IOException e) {
            log.error("json解析出错: {}", json, e);
        }
        return null;
    }

    /**
     * Parse map map.
     *
     * @param <K>    the type parameter
     * @param <V>    the type parameter
     * @param json   the json
     * @param kClass the k class
     * @param vClass the v class
     * @return the map
     */
    public static <K, V> Map<K, V> parseMap(@NonNull String json, @NonNull Class<K> kClass, @NonNull Class<V> vClass) {
        try {
            return OBJECT_MAPPER.readValue(json, OBJECT_MAPPER.getTypeFactory().constructMapType(Map.class, kClass, vClass));
        } catch (IOException e) {
            log.error("json解析出错: {}", json, e);
        }
        return null;
    }

    /**
     * Parse t.
     *
     * @param <T>  the type parameter
     * @param json the json
     * @param type the type
     * @return the t
     */
    public static <T> T parse(@NonNull String json, @NonNull TypeReference<T> type) {
        try {
            return OBJECT_MAPPER.readValue(json, type);
        } catch (IOException e) {
            log.error("json解析出错: {}", json, e);
        }
        return null;
    }

    /**
     * Parse t.
     *
     * @param <T>    the type parameter
     * @param in     the in
     * @param tClass the t class
     * @return the t
     */
    public static <T> T parse(@NonNull InputStream in, @NonNull Class<T> tClass) {
        try {
            return OBJECT_MAPPER.readValue(in, tClass);
        } catch (IOException e) {
            log.error("json解析出错:", e);
        }
        return null;
    }

    /**
     * Parse map map.
     *
     * @param <K>    the type parameter
     * @param <V>    the type parameter
     * @param in     the in
     * @param kClass the k class
     * @param vClass the v class
     * @return the map
     */
    public static <K, V> Map<K, V> parseMap(@NonNull InputStream in, @NonNull Class<K> kClass, @NonNull Class<V> vClass) {
        try {
            return OBJECT_MAPPER.readValue(in, OBJECT_MAPPER.getTypeFactory().constructMapType(Map.class, kClass, vClass));
        } catch (IOException e) {
            log.error("json解析出错:", e);
        }
        return null;
    }


    /**
     * Mapping jackson 2 http message converter mapping jackson 2 http message converter.
     *
     * @return the mapping jackson 2 http message converter
     */
    public static MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        return mappingJackson2HttpMessageConverter(null);
    }

    /**
     * Mapping jackson 2 http message converter mapping jackson 2 http message converter.
     *
     * @param propertyNamingStrategy the property naming strategy
     * @return the mapping jackson 2 http message converter
     */
    public static MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(PropertyNamingStrategy propertyNamingStrategy) {
        // 设置Jackson序列化和反序列化的时候，分隔符
        if (propertyNamingStrategy != null) {
            OBJECT_MAPPER.setPropertyNamingStrategy(propertyNamingStrategy);
        }
        // 设置过滤字段
        SimpleBeanPropertyFilter fieldFilter = SimpleBeanPropertyFilter.serializeAllExcept("create_time", "update_time", "deleted");
        SimpleFilterProvider filterProvider = new SimpleFilterProvider().addFilter("defaultValue", fieldFilter);
        OBJECT_MAPPER.setFilterProvider(filterProvider).addMixIn(BaseEntity.class, PropertyFilterMixIn.class);

        SimpleModule simpleModule = new SimpleModule();
        //添加对长整型的转换关系
        ToStringSerializer stringSerializer = ToStringSerializer.instance;
        simpleModule.addSerializer(BigInteger.class, stringSerializer);
        simpleModule.addSerializer(Long.class, stringSerializer);
        simpleModule.addSerializer(Long.TYPE, stringSerializer);
        //将对象模型添加至对象映射器
        OBJECT_MAPPER.registerModule(simpleModule);
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //定义Json转换器
        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        //将对象映射器添加至Json转换器
        jackson2HttpMessageConverter.setObjectMapper(OBJECT_MAPPER);
        return jackson2HttpMessageConverter;
    }

    /**
     * The interface Property filter mix in.
     */
    @JsonFilter("defaultValue")
    interface PropertyFilterMixIn {
    }
}
