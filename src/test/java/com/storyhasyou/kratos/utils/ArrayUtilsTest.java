package com.storyhasyou.kratos.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

/**
 * ArrayUtils单元测试类
 * 
 * 【强制】测试数组工具类功能，特别是zip方法
 * 【强制】继承HuTool的ArrayUtil，测试扩展功能
 * 【强制】测试方法命名遵循规范：should_ReturnExpected_When_GivenInput()
 * 
 * @author 方曦 created by 2020/12/13
 */
public class ArrayUtilsTest {

    // ==================== zip方法测试 ====================

    @Test
    void should_CreateMapFromArrays_When_GivenEqualLengthArrays() {
        // Given
        String[] keys = {"a", "b", "c", "d"};
        Integer[] values = {1, 2, 3, 4};
        
        // When
        Map<String, Integer> result = ArrayUtils.zip(keys, values);
        
        // Then
        assertThat(result).hasSize(4);
        assertThat(result).containsEntry("a", 1);
        assertThat(result).containsEntry("b", 2);
        assertThat(result).containsEntry("c", 3);
        assertThat(result).containsEntry("d", 4);
    }

    @Test
    void should_CreateMapFromArrays_When_KeysAreShorter() {
        // Given
        String[] keys = {"x", "y"};
        Integer[] values = {10, 20, 30, 40};
        
        // When
        Map<String, Integer> result = ArrayUtils.zip(keys, values);
        
        // Then
        // 只对应最短部分，即keys的长度
        assertThat(result).hasSize(2);
        assertThat(result).containsEntry("x", 10);
        assertThat(result).containsEntry("y", 20);
        assertThat(result).doesNotContainKey("z"); // 不存在的键
    }

    @Test
    void should_CreateMapFromArrays_When_ValuesAreShorter() {
        // Given
        String[] keys = {"p", "q", "r", "s"};
        Integer[] values = {100, 200};
        
        // When
        Map<String, Integer> result = ArrayUtils.zip(keys, values);
        
        // Then
        // 只对应最短部分，即values的长度
        assertThat(result).hasSize(2);
        assertThat(result).containsEntry("p", 100);
        assertThat(result).containsEntry("q", 200);
        assertThat(result).doesNotContainKey("r");
        assertThat(result).doesNotContainKey("s");
    }

    @Test
    void should_CreateEmptyMap_When_OneArrayIsEmpty() {
        // Given
        String[] emptyKeys = {};
        Integer[] values = {1, 2, 3};
        
        // When
        Map<String, Integer> result = ArrayUtils.zip(emptyKeys, values);
        
        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void should_CreateEmptyMap_When_BothArraysAreEmpty() {
        // Given
        String[] emptyKeys = {};
        Integer[] emptyValues = {};
        
        // When
        Map<String, Integer> result = ArrayUtils.zip(emptyKeys, emptyValues);
        
        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void should_CreateSingleEntryMap_When_BothArraysHaveOneElement() {
        // Given
        String[] singleKey = {"key1"};
        String[] singleValue = {"value1"};
        
        // When
        Map<String, String> result = ArrayUtils.zip(singleKey, singleValue);
        
        // Then
        assertThat(result).hasSize(1);
        assertThat(result).containsEntry("key1", "value1");
    }

    // ==================== 不同类型测试 ====================

    @Test
    void should_HandleDifferentTypes_When_GivenVariousDataTypes() {
        // Given
        Integer[] intKeys = {1, 2, 3};
        String[] stringValues = {"one", "two", "three"};
        
        // When
        Map<Integer, String> result = ArrayUtils.zip(intKeys, stringValues);
        
        // Then
        assertThat(result).hasSize(3);
        assertThat(result).containsEntry(1, "one");
        assertThat(result).containsEntry(2, "two");
        assertThat(result).containsEntry(3, "three");
    }

    @Test
    void should_HandleObjectArrays_When_GivenComplexObjects() {
        // Given
        Person[] personKeys = {
            new Person("Alice", 25),
            new Person("Bob", 30)
        };
        String[] jobValues = {"Engineer", "Manager"};
        
        // When
        Map<Person, String> result = ArrayUtils.zip(personKeys, jobValues);
        
        // Then
        assertThat(result).hasSize(2);
        assertThat(result).containsEntry(personKeys[0], "Engineer");
        assertThat(result).containsEntry(personKeys[1], "Manager");
    }

    @Test
    void should_HandleBooleanArrays_When_GivenBooleanTypes() {
        // Given
        String[] keys = {"isActive", "isVisible", "isEnabled"};
        Boolean[] values = {true, false, true};
        
        // When
        Map<String, Boolean> result = ArrayUtils.zip(keys, values);
        
        // Then
        assertThat(result).hasSize(3);
        assertThat(result).containsEntry("isActive", true);
        assertThat(result).containsEntry("isVisible", false);
        assertThat(result).containsEntry("isEnabled", true);
    }

    // ==================== null值处理测试 ====================

    @Test
    void should_HandleNullKeys_When_KeysArrayContainsNull() {
        // Given
        String[] keysWithNull = {"key1", null, "key3"};
        Integer[] values = {1, 2, 3};
        
        // When
        Map<String, Integer> result = ArrayUtils.zip(keysWithNull, values);
        
        // Then
        assertThat(result).hasSize(3);
        assertThat(result).containsEntry("key1", 1);
        assertThat(result).containsEntry(null, 2); // null作为key
        assertThat(result).containsEntry("key3", 3);
    }

    @Test
    void should_HandleNullValues_When_ValuesArrayContainsNull() {
        // Given
        String[] keys = {"key1", "key2", "key3"};
        String[] valuesWithNull = {"value1", null, "value3"};
        
        // When
        Map<String, String> result = ArrayUtils.zip(keys, valuesWithNull);
        
        // Then
        assertThat(result).hasSize(3);
        assertThat(result).containsEntry("key1", "value1");
        assertThat(result).containsEntry("key2", null); // null作为value
        assertThat(result).containsEntry("key3", "value3");
    }

    @Test
    void should_ThrowException_When_KeysArrayIsNull() {
        // Given
        String[] nullKeys = null;
        Integer[] values = {1, 2, 3};
        
        // When & Then
        assertThatThrownBy(() -> ArrayUtils.zip(nullKeys, values))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void should_ThrowException_When_ValuesArrayIsNull() {
        // Given
        String[] keys = {"a", "b", "c"};
        Integer[] nullValues = null;
        
        // When & Then
        assertThatThrownBy(() -> ArrayUtils.zip(keys, nullValues))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void should_ThrowException_When_BothArraysAreNull() {
        // Given
        String[] nullKeys = null;
        Integer[] nullValues = null;
        
        // When & Then
        assertThatThrownBy(() -> ArrayUtils.zip(nullKeys, nullValues))
            .isInstanceOf(NullPointerException.class);
    }

    // ==================== 重复键处理测试 ====================

    @Test
    void should_HandleDuplicateKeys_When_KeysArrayHasDuplicates() {
        // Given
        String[] duplicateKeys = {"a", "b", "a", "c"}; // "a"重复
        Integer[] values = {1, 2, 3, 4};
        
        // When
        Map<String, Integer> result = ArrayUtils.zip(duplicateKeys, values);
        
        // Then
        // 后面的值会覆盖前面的值
        assertThat(result).hasSize(3); // 只有3个不同的键
        assertThat(result).containsEntry("a", 3); // 最后一次出现的值
        assertThat(result).containsEntry("b", 2);
        assertThat(result).containsEntry("c", 4);
    }

    // ==================== 参数化测试 ====================

    @ParameterizedTest
    @MethodSource("provideArraysForZip")
    void should_CreateCorrectMap_When_GivenVariousArrayCombinations(
            String[] keys, Integer[] values, int expectedSize) {
        // When
        Map<String, Integer> result = ArrayUtils.zip(keys, values);
        
        // Then
        assertThat(result).hasSize(expectedSize);
        
        // 验证映射的正确性
        int minLength = Math.min(keys.length, values.length);
        for (int i = 0; i < minLength; i++) {
            assertThat(result).containsEntry(keys[i], values[i]);
        }
    }

    static Stream<Arguments> provideArraysForZip() {
        return Stream.of(
            Arguments.of(new String[]{"a"}, new Integer[]{1}, 1),
            Arguments.of(new String[]{"a", "b"}, new Integer[]{1, 2}, 2),
            Arguments.of(new String[]{"a", "b", "c"}, new Integer[]{1, 2}, 2),
            Arguments.of(new String[]{"a", "b"}, new Integer[]{1, 2, 3}, 2),
            Arguments.of(new String[]{}, new Integer[]{1, 2, 3}, 0),
            Arguments.of(new String[]{"a", "b"}, new Integer[]{}, 0)
        );
    }

    // ==================== 继承功能测试 ====================

    @Test
    void should_InheritHuToolMethods_When_UsingArrayUtilMethods() {
        // Given
        String[] array1 = {"a", "b", "c"};
        String[] array2 = {"d", "e", "f"};
        
        // When - 使用继承自HuTool的方法
        String[] concatenated = ArrayUtils.addAll(array1, array2);
        boolean isEmpty = ArrayUtils.isEmpty(new String[]{});
        boolean isNotEmpty = ArrayUtils.isNotEmpty(array1);
        
        // Then
        assertThat(concatenated).hasSize(6);
        assertThat(concatenated).containsExactly("a", "b", "c", "d", "e", "f");
        assertThat(isEmpty).isTrue();
        assertThat(isNotEmpty).isTrue();
    }

    @Test
    void should_UseHuToolArrayOperations_When_ManipulatingArrays() {
        // Given
        Integer[] numbers = {1, 2, 3, 4, 5};
        
        // When
        boolean contains = ArrayUtils.contains(numbers, 3);
        int index = ArrayUtils.indexOf(numbers, 4);
        Integer[] reversed = ArrayUtils.reverse(numbers.clone());
        
        // Then
        assertThat(contains).isTrue();
        assertThat(index).isEqualTo(3);
        assertThat(reversed).containsExactly(5, 4, 3, 2, 1);
    }

    // ==================== 性能测试 ====================

    @Test
    void should_PerformEfficiently_When_ProcessingLargeArrays() {
        // Given
        int size = 100000;
        String[] keys = new String[size];
        Integer[] values = new Integer[size];
        
        for (int i = 0; i < size; i++) {
            keys[i] = "key" + i;
            values[i] = i;
        }
        
        // When
        long startTime = System.currentTimeMillis();
        Map<String, Integer> result = ArrayUtils.zip(keys, values);
        long endTime = System.currentTimeMillis();
        
        // Then
        assertThat(result).hasSize(size);
        assertThat(endTime - startTime).isLessThan(1000L); // 1秒内完成
        
        // 验证几个随机项
        assertThat(result).containsEntry("key0", 0);
        assertThat(result).containsEntry("key" + (size - 1), size - 1);
        assertThat(result).containsEntry("key" + (size / 2), size / 2);
        
        System.out.println("大数组zip性能测试: " + size + " 个元素耗时 " + (endTime - startTime) + "ms");
    }

    // ==================== 实际应用场景测试 ====================

    @Test
    void should_CreateConfigurationMap_When_GivenConfigArrays() {
        // Given
        String[] configKeys = {"database.host", "database.port", "database.name"};
        String[] configValues = {"localhost", "5432", "myapp"};
        
        // When
        Map<String, String> config = ArrayUtils.zip(configKeys, configValues);
        
        // Then
        assertThat(config).containsEntry("database.host", "localhost");
        assertThat(config).containsEntry("database.port", "5432");
        assertThat(config).containsEntry("database.name", "myapp");
    }

    @Test
    void should_CreateHttpHeaderMap_When_GivenHeaderArrays() {
        // Given
        String[] headerNames = {"Content-Type", "Accept", "Authorization"};
        String[] headerValues = {"application/json", "*/*", "Bearer token123"};
        
        // When
        Map<String, String> headers = ArrayUtils.zip(headerNames, headerValues);
        
        // Then
        assertThat(headers).containsEntry("Content-Type", "application/json");
        assertThat(headers).containsEntry("Accept", "*/*");
        assertThat(headers).containsEntry("Authorization", "Bearer token123");
    }

    @Test
    void should_CreateFieldValueMap_When_ProcessingFormData() {
        // Given
        String[] fieldNames = {"username", "email", "age"};
        String[] fieldValues = {"john_doe", "john@example.com", "25"};
        
        // When
        Map<String, String> formData = ArrayUtils.zip(fieldNames, fieldValues);
        
        // Then
        assertThat(formData).containsEntry("username", "john_doe");
        assertThat(formData).containsEntry("email", "john@example.com");
        assertThat(formData).containsEntry("age", "25");
    }

    // ==================== 辅助测试类 ====================

    private static class Person {
        private final String name;
        private final int age;
        
        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Person person = (Person) obj;
            return age == person.age && name.equals(person.name);
        }
        
        @Override
        public int hashCode() {
            return name.hashCode() * 31 + age;
        }
        
        @Override
        public String toString() {
            return "Person{name='" + name + "', age=" + age + "}";
        }
    }
}