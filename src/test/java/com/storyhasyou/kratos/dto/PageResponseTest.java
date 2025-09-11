package com.storyhasyou.kratos.dto;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PageResponse Record 类测试
 * <p>
 * 验证 JDK 21 Record 特性的功能和向后兼容性
 * </p>
 *
 * @author fangxi
 */
class PageResponseTest {

    @Test
    void testEmptyPageResponse() {
        PageResponse<String> emptyResponse = PageResponse.empty();
        
        assertTrue(emptyResponse.isEmpty());
        assertFalse(emptyResponse.hasData());
        assertEquals(0, emptyResponse.getCurrentPageSize());
        assertNotNull(emptyResponse.rows());
        assertEquals(0, emptyResponse.rows().size());
    }

    @Test
    void testBuilder() {
        List<String> testData = Arrays.asList("test1", "test2", "test3");
        
        PageResponse<String> response = PageResponse.<String>builder()
                .rows(testData)
                .current(1L)
                .limit(10L)
                .records(3L)
                .pages(1L)
                .hasNext(false)
                .build();
        
        assertFalse(response.isEmpty());
        assertTrue(response.hasData());
        assertEquals(3, response.getCurrentPageSize());
        assertEquals(testData.size(), response.rows().size());
        assertEquals(1L, response.current());
        assertEquals(10L, response.limit());
        assertEquals(3L, response.records());
        assertEquals(1L, response.pages());
        assertFalse(response.hasNext());
    }

    @Test
    void testOfFactoryMethod() {
        List<String> testData = Arrays.asList("item1", "item2");
        PageRequest pageRequest = PageRequest.of(1, 10);
        Long totalCount = 2L;
        
        PageResponse<String> response = PageResponse.of(testData, pageRequest, totalCount);
        
        assertEquals(testData.size(), response.rows().size());
        assertEquals(1L, response.current());
        assertEquals(10L, response.limit());
        assertEquals(2L, response.records());
        assertEquals(1L, response.pages());
        assertFalse(response.hasNext());
    }

    @Test
    void testImmutability() {
        List<String> originalData = Arrays.asList("data1", "data2");
        PageResponse<String> response = PageResponse.<String>builder()
                .rows(originalData)
                .current(1L)
                .limit(10L)
                .records(2L)
                .pages(1L)
                .hasNext(false)
                .build();
        
        // 验证返回的 rows 是不可变的
        assertThrows(UnsupportedOperationException.class, () -> {
            response.rows().add("newItem");
        });
    }

    @Test
    void testValidation() {
        // 测试 null rows 验证
        assertThrows(NullPointerException.class, () -> {
            new PageResponse<String>(null, 1L, 10L, 0L, 0L, false);
        });
        
        // 测试 current 参数验证
        List<String> testData = Arrays.asList("test");
        assertThrows(IllegalArgumentException.class, () -> {
            new PageResponse<>(testData, 0L, 10L, 1L, 1L, false);
        });
        
        // 测试负数参数验证
        assertThrows(IllegalArgumentException.class, () -> {
            new PageResponse<>(testData, 1L, -1L, 1L, 1L, false);
        });
    }

    @Test
    void testRecordFeatures() {
        List<String> testData = Arrays.asList("test1", "test2");
        
        PageResponse<String> response1 = PageResponse.<String>builder()
                .rows(testData)
                .current(1L)
                .limit(10L)
                .records(2L)
                .pages(1L)
                .hasNext(false)
                .build();
        
        PageResponse<String> response2 = PageResponse.<String>builder()
                .rows(testData)
                .current(1L)
                .limit(10L)
                .records(2L)
                .pages(1L)
                .hasNext(false)
                .build();
        
        // 验证 Record 自动生成的 equals 方法
        assertEquals(response1, response2);
        
        // 验证 Record 自动生成的 hashCode 方法
        assertEquals(response1.hashCode(), response2.hashCode());
        
        // 验证 Record 自动生成的 toString 方法
        assertNotNull(response1.toString());
        assertTrue(response1.toString().contains("PageResponse"));
    }
}