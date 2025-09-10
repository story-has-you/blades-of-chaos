package com.storyhasyou.kratos.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.storyhasyou.kratos.base.BaseEntity;
import com.storyhasyou.kratos.exceptions.BusinessException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.junit.jupiter.api.Test;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.*;

/**
 * JacksonUtils单元测试类
 * 
 * 【强制】使用JUnit 5和AssertJ进行测试，确保测试覆盖率和健壮性
 * 【强制】测试方法命名遵循规范：should_ReturnExpected_When_GivenInput()
 * 
 * @author fangxi created by 2021/1/19
 */
public class JacksonUtilsTest {

    // ==================== 测试用例数据类 ====================

    @Data
    static class SimpleTestObject {
        private String name;
        private Integer age;
        private Boolean active;
        
        public SimpleTestObject() {}
        
        public SimpleTestObject(String name, Integer age, Boolean active) {
            this.name = name;
            this.age = age;
            this.active = active;
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    static class TestEntity extends BaseEntity {
        private String title;
        private String content;
        private Boolean deleted = false;
        
        public TestEntity() {}
        
        public TestEntity(String title, String content) {
            this.title = title;
            this.content = content;
            setCreateTime(LocalDateTime.now());
            setUpdateTime(LocalDateTime.now());
        }
    }

    @Data
    static class TimeTestObject {
        private LocalDateTime dateTime;
        private LocalDate date;
        private LocalTime time;
        
        public TimeTestObject() {}
        
        public TimeTestObject(LocalDateTime dateTime, LocalDate date, LocalTime time) {
            this.dateTime = dateTime;
            this.date = date;
            this.time = time;
        }
    }

    @Data
    static class NumberTestObject {
        private Long longValue;
        private BigInteger bigIntValue;
        private Double doubleValue;
        
        public NumberTestObject() {}
        
        public NumberTestObject(Long longValue, BigInteger bigIntValue, Double doubleValue) {
            this.longValue = longValue;
            this.bigIntValue = bigIntValue;
            this.doubleValue = doubleValue;
        }
    }

    // ==================== 基础功能测试 ====================

    @Test
    void should_ReturnObjectMapper_When_GetObjectMapper() {
        // Given & When
        ObjectMapper mapper = JacksonUtils.getObjectMapper();
        
        // Then
        assertThat(mapper).isNotNull();
        assertThat(mapper).isSameAs(JacksonUtils.getObjectMapper());
    }

    @Test
    void should_SerializeToString_When_GivenValidObject() {
        // Given
        SimpleTestObject testObject = new SimpleTestObject("张三", 25, true);
        
        // When
        String json = JacksonUtils.serialize(testObject);
        
        // Then
        assertThat(json).isNotNull();
        assertThat(json).contains("\"name\":\"张三\"");
        assertThat(json).contains("\"age\":25");
        assertThat(json).contains("\"active\":true");
    }

    @Test
    void should_ReturnSameString_When_SerializeStringInput() {
        // Given
        String input = "test string";
        
        // When
        String result = JacksonUtils.serialize(input);
        
        // Then
        assertThat(result).isEqualTo(input);
    }

    @Test
    void should_ThrowBusinessException_When_SerializeNullInput() {
        // Given & When & Then
        assertThatThrownBy(() -> JacksonUtils.serialize(null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void should_SerializeToBytes_When_GivenValidObject() {
        // Given
        SimpleTestObject testObject = new SimpleTestObject("张三", 25, true);
        
        // When
        byte[] bytes = JacksonUtils.toBytes(testObject);
        
        // Then
        assertThat(bytes).isNotNull();
        assertThat(bytes.length).isGreaterThan(0);
        
        // 验证字节数组内容
        String jsonFromBytes = new String(bytes);
        assertThat(jsonFromBytes).contains("\"name\":\"张三\"");
    }

    @Test
    void should_SerializeToBytes_When_CallingSerializeAsBytes() {
        // Given
        SimpleTestObject testObject = new SimpleTestObject("李四", 30, false);
        
        // When
        byte[] bytes = JacksonUtils.serializeAsBytes(testObject);
        
        // Then
        assertThat(bytes).isNotNull();
        assertThat(bytes.length).isGreaterThan(0);
        
        // 验证与toBytes方法结果一致
        byte[] expectedBytes = JacksonUtils.toBytes(testObject);
        assertThat(bytes).isEqualTo(expectedBytes);
    }

    // ==================== 反序列化测试 ====================

    @Test
    void should_ParseFromString_When_GivenValidJson() {
        // Given
        String json = "{\"name\":\"王五\",\"age\":35,\"active\":true}";
        
        // When
        SimpleTestObject result = JacksonUtils.parse(json, SimpleTestObject.class);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("王五");
        assertThat(result.getAge()).isEqualTo(35);
        assertThat(result.getActive()).isTrue();
    }

    @Test
    void should_ParseFromBytes_When_GivenValidJsonBytes() {
        // Given
        String json = "{\"name\":\"赵六\",\"age\":40,\"active\":false}";
        byte[] bytes = json.getBytes();
        
        // When
        SimpleTestObject result = JacksonUtils.parse(bytes, SimpleTestObject.class);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("赵六");
        assertThat(result.getAge()).isEqualTo(40);
        assertThat(result.getActive()).isFalse();
    }

    @Test
    void should_ThrowBusinessException_When_ParseInvalidJson() {
        // Given
        String invalidJson = "{invalid json}";
        
        // When & Then
        assertThatThrownBy(() -> JacksonUtils.parse(invalidJson, SimpleTestObject.class))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("json解析出错");
    }

    @Test
    void should_ThrowBusinessException_When_ParseNullInput() {
        // Given & When & Then
        assertThatThrownBy(() -> JacksonUtils.parse((String) null, SimpleTestObject.class))
            .isInstanceOf(IllegalArgumentException.class);
    }

    // ==================== 数组解析测试 ====================

    @Test
    void should_ParseArray_When_GivenValidJsonArray() {
        // Given
        String json = "[{\"name\":\"用户1\",\"age\":20},{\"name\":\"用户2\",\"age\":25}]";
        
        // When
        SimpleTestObject[] result = JacksonUtils.parseArray(json, SimpleTestObject.class);
        
        // Then
        assertThat(result).hasSize(2);
        assertThat(result[0].getName()).isEqualTo("用户1");
        assertThat(result[0].getAge()).isEqualTo(20);
        assertThat(result[1].getName()).isEqualTo("用户2");
        assertThat(result[1].getAge()).isEqualTo(25);
    }

    @Test
    void should_ParseArrayFromBytes_When_GivenValidJsonArrayBytes() {
        // Given
        String json = "[{\"name\":\"用户3\",\"age\":30},{\"name\":\"用户4\",\"age\":35}]";
        byte[] bytes = json.getBytes();
        
        // When
        SimpleTestObject[] result = JacksonUtils.parseArray(bytes, SimpleTestObject.class);
        
        // Then
        assertThat(result).hasSize(2);
        assertThat(result[0].getName()).isEqualTo("用户3");
        assertThat(result[1].getName()).isEqualTo("用户4");
    }

    @Test
    void should_ParseArrayFromInputStream_When_GivenValidStream() {
        // Given
        String json = "[{\"name\":\"用户5\",\"age\":40}]";
        InputStream inputStream = new ByteArrayInputStream(json.getBytes());
        
        // When
        SimpleTestObject[] result = JacksonUtils.parseArray(inputStream, SimpleTestObject.class);
        
        // Then
        assertThat(result).hasSize(1);
        assertThat(result[0].getName()).isEqualTo("用户5");
        assertThat(result[0].getAge()).isEqualTo(40);
    }

    // ==================== 集合解析测试 ====================

    @Test
    void should_ParseList_When_GivenValidJsonArray() {
        // Given
        String json = "[{\"name\":\"列表用户1\",\"age\":25},{\"name\":\"列表用户2\",\"age\":30}]";
        
        // When
        List<SimpleTestObject> result = JacksonUtils.parseList(json, SimpleTestObject.class);
        
        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("列表用户1");
        assertThat(result.get(1).getName()).isEqualTo("列表用户2");
    }

    @Test
    void should_ParseListFromBytes_When_GivenValidJsonArrayBytes() {
        // Given
        String json = "[{\"name\":\"字节列表用户\",\"age\":35}]";
        byte[] bytes = json.getBytes();
        
        // When
        List<SimpleTestObject> result = JacksonUtils.parseList(bytes, SimpleTestObject.class);
        
        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("字节列表用户");
    }

    @Test
    void should_ParseListFromInputStream_When_GivenValidStream() {
        // Given
        String json = "[{\"name\":\"流列表用户\",\"age\":45}]";
        InputStream inputStream = new ByteArrayInputStream(json.getBytes());
        
        // When
        List<SimpleTestObject> result = JacksonUtils.parseList(inputStream, SimpleTestObject.class);
        
        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("流列表用户");
    }

    @Test
    void should_ParseSet_When_GivenValidJsonArray() {
        // Given
        String json = "[{\"name\":\"集合用户1\",\"age\":25},{\"name\":\"集合用户2\",\"age\":30}]";
        
        // When
        Set<SimpleTestObject> result = JacksonUtils.parseSet(json, SimpleTestObject.class);
        
        // Then
        assertThat(result).hasSize(2);
        assertThat(result.stream().anyMatch(obj -> "集合用户1".equals(obj.getName()))).isTrue();
        assertThat(result.stream().anyMatch(obj -> "集合用户2".equals(obj.getName()))).isTrue();
    }

    @Test
    void should_ParseSetFromBytes_When_GivenValidJsonArrayBytes() {
        // Given
        String json = "[{\"name\":\"字节集合用户\",\"age\":35}]";
        byte[] bytes = json.getBytes();
        
        // When
        Set<SimpleTestObject> result = JacksonUtils.parseSet(bytes, SimpleTestObject.class);
        
        // Then
        assertThat(result).hasSize(1);
        assertThat(result.iterator().next().getName()).isEqualTo("字节集合用户");
    }

    @Test
    void should_ParseSetFromInputStream_When_GivenValidStream() {
        // Given
        String json = "[{\"name\":\"流集合用户\",\"age\":45}]";
        InputStream inputStream = new ByteArrayInputStream(json.getBytes());
        
        // When
        Set<SimpleTestObject> result = JacksonUtils.parseSet(inputStream, SimpleTestObject.class);
        
        // Then
        assertThat(result).hasSize(1);
        assertThat(result.iterator().next().getName()).isEqualTo("流集合用户");
    }

    // ==================== 泛型类型解析测试 ====================

    @Test
    void should_ParseWithTypeReference_When_GivenGenericType() {
        // Given
        String json = "[{\"name\":\"泛型用户1\",\"age\":25},{\"name\":\"泛型用户2\",\"age\":30}]";
        TypeReference<List<SimpleTestObject>> typeRef = new TypeReference<List<SimpleTestObject>>() {};
        
        // When
        List<SimpleTestObject> result = JacksonUtils.parse(json, typeRef);
        
        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("泛型用户1");
        assertThat(result.get(1).getName()).isEqualTo("泛型用户2");
    }

    @Test
    void should_ParseFromInputStream_When_GivenValidStreamAndClass() {
        // Given
        String json = "{\"name\":\"输入流用户\",\"age\":50,\"active\":true}";
        InputStream inputStream = new ByteArrayInputStream(json.getBytes());
        
        // When
        SimpleTestObject result = JacksonUtils.parse(inputStream, SimpleTestObject.class);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("输入流用户");
        assertThat(result.getAge()).isEqualTo(50);
        assertThat(result.getActive()).isTrue();
    }

    // ==================== Map解析测试 ====================

    @Test
    void should_ParseMap_When_GivenValidMapJson() {
        // Given
        String json = "{\"key1\":\"value1\",\"key2\":\"value2\"}";
        
        // When
        Map<String, String> result = JacksonUtils.parseMap(json, String.class, String.class);
        
        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get("key1")).isEqualTo("value1");
        assertThat(result.get("key2")).isEqualTo("value2");
    }

    @Test
    void should_ParseMapFromInputStream_When_GivenValidStream() {
        // Given
        String json = "{\"count\":123,\"total\":456}";
        InputStream inputStream = new ByteArrayInputStream(json.getBytes());
        
        // When
        Map<String, Integer> result = JacksonUtils.parseMap(inputStream, String.class, Integer.class);
        
        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get("count")).isEqualTo(123);
        assertThat(result.get("total")).isEqualTo(456);
    }

    @Test
    void should_ParseMapFromBytes_When_GivenValidMapJsonBytes() {
        // Given
        String json = "{\"price\":99.99,\"discount\":10.5}";
        byte[] bytes = json.getBytes();
        
        // When
        Map<String, Double> result = JacksonUtils.parseMap(bytes, String.class, Double.class);
        
        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get("price")).isEqualTo(99.99);
        assertThat(result.get("discount")).isEqualTo(10.5);
    }

    // ==================== 类型转换测试 ====================

    @Test
    void should_ConvertValue_When_GivenTypeReference() {
        // Given
        Map<String, Object> sourceMap = Map.of("name", "转换用户", "age", 28, "active", true);
        TypeReference<SimpleTestObject> typeRef = new TypeReference<SimpleTestObject>() {};
        
        // When
        SimpleTestObject result = JacksonUtils.convertValue(sourceMap, typeRef);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("转换用户");
        assertThat(result.getAge()).isEqualTo(28);
        assertThat(result.getActive()).isTrue();
    }

    @Test
    void should_ConvertValue_When_GivenClass() {
        // Given
        Map<String, Object> sourceMap = Map.of("name", "转换用户2", "age", 32, "active", false);
        
        // When
        SimpleTestObject result = JacksonUtils.convertValue(sourceMap, SimpleTestObject.class);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("转换用户2");
        assertThat(result.getAge()).isEqualTo(32);
        assertThat(result.getActive()).isFalse();
    }

    @Test
    void should_ConvertValue_When_GivenJavaType() {
        // Given
        Map<String, Object> sourceMap = Map.of("name", "转换用户3", "age", 35, "active", true);
        ObjectMapper mapper = JacksonUtils.getObjectMapper();
        
        // When
        SimpleTestObject result = JacksonUtils.convertValue(sourceMap, 
            mapper.getTypeFactory().constructType(SimpleTestObject.class));
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("转换用户3");
        assertThat(result.getAge()).isEqualTo(35);
    }

    // ==================== 时间序列化测试 ====================

    @Test
    void should_SerializeDateTime_When_GivenTimeObjects() {
        // Given
        LocalDateTime dateTime = LocalDateTime.of(2023, 12, 25, 15, 30, 45);
        LocalDate date = LocalDate.of(2023, 12, 25);
        LocalTime time = LocalTime.of(15, 30, 45);
        TimeTestObject timeObj = new TimeTestObject(dateTime, date, time);
        
        // When
        String json = JacksonUtils.serialize(timeObj);
        
        // Then
        assertThat(json).contains("\"dateTime\":\"2023-12-25 15:30:45\"");
        assertThat(json).contains("\"date\":\"2023-12-25\"");
        assertThat(json).contains("\"time\":\"15:30:45\"");
    }

    @Test
    void should_DeserializeDateTime_When_GivenTimeJson() {
        // Given
        String json = "{\"dateTime\":\"2023-12-25 15:30:45\",\"date\":\"2023-12-25\",\"time\":\"15:30:45\"}";
        
        // When
        TimeTestObject result = JacksonUtils.parse(json, TimeTestObject.class);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.getDateTime()).isEqualTo(LocalDateTime.of(2023, 12, 25, 15, 30, 45));
        assertThat(result.getDate()).isEqualTo(LocalDate.of(2023, 12, 25));
        assertThat(result.getTime()).isEqualTo(LocalTime.of(15, 30, 45));
    }

    // ==================== HTTP消息转换器测试 ====================

    @Test
    void should_CreateMappingJackson2HttpMessageConverter_When_DefaultSettings() {
        // When
        MappingJackson2HttpMessageConverter converter = JacksonUtils.mappingJackson2HttpMessageConverter();
        
        // Then
        assertThat(converter).isNotNull();
        assertThat(converter.getObjectMapper()).isNotNull();
    }

    @Test
    void should_CreateMappingJackson2HttpMessageConverter_When_Long2StrEnabled() {
        // Given
        NumberTestObject numberObj = new NumberTestObject(123456789L, new BigInteger("987654321"), 3.14);
        
        // When
        MappingJackson2HttpMessageConverter converter = JacksonUtils.mappingJackson2HttpMessageConverter(true);
        
        // 使用自定义的converter来序列化，验证Long转String的配置
        ObjectMapper customMapper = converter.getObjectMapper();
        
        try {
            String json = customMapper.writeValueAsString(numberObj);
            
            // Then
            assertThat(converter).isNotNull();
            assertThat(converter.getObjectMapper()).isNotNull();
            
            // 验证BigInteger总是序列化为字符串
            assertThat(json).contains("\"bigIntValue\":\"987654321\"");
            // 验证Long在long2Str=true时也会序列化为字符串
            assertThat(json).contains("\"longValue\":\"123456789\"");
        } catch (Exception e) {
            fail("序列化应该成功", e);
        }
    }

    @Test
    void should_CreateMappingJackson2HttpMessageConverter_When_CustomNamingStrategy() {
        // When
        MappingJackson2HttpMessageConverter converter = 
            JacksonUtils.mappingJackson2HttpMessageConverter(PropertyNamingStrategies.SNAKE_CASE, false);
        
        // Then
        assertThat(converter).isNotNull();
        assertThat(converter.getObjectMapper()).isNotNull();
        assertThat(converter.getObjectMapper().getPropertyNamingStrategy())
            .isEqualTo(PropertyNamingStrategies.SNAKE_CASE);
    }

    @Test
    void should_FilterDeletedField_When_SerializingBaseEntity() {
        // Given
        TestEntity entity = new TestEntity("测试标题", "测试内容");
        entity.setDeleted(true);
        
        // When
        MappingJackson2HttpMessageConverter converter = JacksonUtils.mappingJackson2HttpMessageConverter();
        String json = JacksonUtils.serialize(entity);
        
        // Then
        // deleted字段应该被过滤掉（在PropertyFilter中被忽略）
        assertThat(json).contains("\"title\":\"测试标题\"");
        assertThat(json).contains("\"content\":\"测试内容\"");
    }

    // ==================== 异常场景测试 ====================

    @Test
    void should_ThrowBusinessException_When_SerializeCircularReference() {
        // Given
        // 创建循环引用对象会导致序列化失败
        Map<String, Object> map1 = new HashMap<>();
        Map<String, Object> map2 = new HashMap<>();
        map1.put("ref", map2);
        map2.put("ref", map1); // 创建循环引用
        
        // When & Then
        // Jackson会抛出异常，工具类会包装为BusinessException
        assertThatThrownBy(() -> JacksonUtils.serialize(map1))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("json序列化出错");
    }

    @Test
    void should_HandleNullValues_When_Serializing() {
        // Given
        SimpleTestObject objWithNulls = new SimpleTestObject(null, null, null);
        
        // When
        String json = JacksonUtils.serialize(objWithNulls);
        
        // Then
        // 序列化对象，null字段应该被包含（默认行为）
        assertThat(json).contains("\"name\":null");
        assertThat(json).contains("\"age\":null");
        assertThat(json).contains("\"active\":null");
    }

    @Test
    void should_HandleUnknownProperties_When_Deserializing() {
        // Given
        String jsonWithExtraField = "{\"name\":\"用户\",\"age\":25,\"unknownField\":\"unknown\"}";
        
        // When & Then
        // 由于配置了FAIL_ON_UNKNOWN_PROPERTIES为false，不应该抛出异常
        assertThatCode(() -> JacksonUtils.parse(jsonWithExtraField, SimpleTestObject.class))
            .doesNotThrowAnyException();
        
        SimpleTestObject result = JacksonUtils.parse(jsonWithExtraField, SimpleTestObject.class);
        assertThat(result.getName()).isEqualTo("用户");
        assertThat(result.getAge()).isEqualTo(25);
    }

    // ==================== 线程安全测试 ====================

    @Test
    void should_BeThreadSafe_When_ConcurrentSerialization() throws Exception {
        // Given
        int threadCount = 10;
        int operationsPerThread = 100;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        
        // When
        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                for (int j = 0; j < operationsPerThread; j++) {
                    SimpleTestObject obj = new SimpleTestObject("线程" + threadId + "_对象" + j, 
                        threadId * 100 + j, j % 2 == 0);
                    
                    // 序列化和反序列化操作
                    String json = JacksonUtils.serialize(obj);
                    SimpleTestObject parsed = JacksonUtils.parse(json, SimpleTestObject.class);
                    
                    // 验证结果正确性
                    assertThat(parsed.getName()).isEqualTo(obj.getName());
                    assertThat(parsed.getAge()).isEqualTo(obj.getAge());
                    assertThat(parsed.getActive()).isEqualTo(obj.getActive());
                }
            }, executor);
            futures.add(future);
        }
        
        // Then
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).get(30, TimeUnit.SECONDS);
        executor.shutdown();
        assertThat(executor.awaitTermination(5, TimeUnit.SECONDS)).isTrue();
    }

    // ==================== 边界条件测试 ====================

    @Test
    void should_HandleEmptyArray_When_Parsing() {
        // Given
        String emptyArrayJson = "[]";
        
        // When
        List<SimpleTestObject> result = JacksonUtils.parseList(emptyArrayJson, SimpleTestObject.class);
        
        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void should_HandleEmptyObject_When_Parsing() {
        // Given
        String emptyObjectJson = "{}";
        
        // When
        SimpleTestObject result = JacksonUtils.parse(emptyObjectJson, SimpleTestObject.class);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isNull();
        assertThat(result.getAge()).isNull();
        assertThat(result.getActive()).isNull();
    }

    @Test
    void should_HandleSingleValueAsArray_When_ConfigurationEnabled() {
        // Given
        String singleValueJson = "{\"name\":\"单个用户\",\"age\":25,\"active\":true}";
        
        // When
        List<SimpleTestObject> result = JacksonUtils.parseList("[" + singleValueJson + "]", SimpleTestObject.class);
        
        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("单个用户");
    }

    @Test
    void should_HandleSpecialCharacters_When_SerializingAndDeserializing() {
        // Given
        SimpleTestObject obj = new SimpleTestObject("特殊字符测试：\"引号\"、\\反斜杠\\、\n换行符", 100, true);
        
        // When
        String json = JacksonUtils.serialize(obj);
        SimpleTestObject result = JacksonUtils.parse(json, SimpleTestObject.class);
        
        // Then
        assertThat(result.getName()).isEqualTo(obj.getName());
        assertThat(result.getAge()).isEqualTo(obj.getAge());
        assertThat(result.getActive()).isEqualTo(obj.getActive());
    }
}