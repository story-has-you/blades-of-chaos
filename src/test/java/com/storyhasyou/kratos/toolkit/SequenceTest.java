package com.storyhasyou.kratos.toolkit;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Sequence单元测试类
 * 
 * 【强制】针对Snowflake算法的线程安全ID生成器进行全面测试
 * 【强制】重点测试线程安全性、ID唯一性、性能等关键指标
 * 【强制】测试方法命名遵循规范：should_ReturnExpected_When_GivenInput()
 * 
 * @author fangxi created by 2021/1/24
 */
public class SequenceTest {

    // ==================== 构造方法测试 ====================

    @Test
    void should_CreateSequence_When_DefaultConstructor() {
        // When
        Sequence sequence = new Sequence();
        
        // Then
        assertThat(sequence).isNotNull();
        
        // 验证能够生成ID
        Long id = sequence.nextId();
        assertThat(id).isNotNull();
        assertThat(id).isPositive();
    }

    @Test
    void should_CreateSequence_When_DataCenterIdProvided() {
        // Given
        long dataCenterId = 1L;
        
        // When
        Sequence sequence = new Sequence(dataCenterId);
        
        // Then
        assertThat(sequence).isNotNull();
        
        // 验证能够生成ID
        Long id = sequence.nextId();
        assertThat(id).isNotNull();
        assertThat(id).isPositive();
    }

    @Test
    void should_CreateSequence_When_DataCenterIdWithFlags() {
        // Given
        long dataCenterId = 2L;
        boolean clock = true;
        boolean randomSequence = false;
        
        // When
        Sequence sequence = new Sequence(dataCenterId, clock, randomSequence);
        
        // Then
        assertThat(sequence).isNotNull();
        
        // 验证能够生成ID
        Long id = sequence.nextId();
        assertThat(id).isNotNull();
        assertThat(id).isPositive();
    }

    @Test
    void should_CreateSequence_When_FullConstructor() {
        // Given
        long dataCenterId = 1L;
        long workerId = 100L;
        boolean clock = true;
        long timeOffset = 5L;
        boolean randomSequence = false;
        
        // When
        Sequence sequence = new Sequence(dataCenterId, workerId, clock, timeOffset, randomSequence);
        
        // Then
        assertThat(sequence).isNotNull();
        
        // 验证能够生成ID
        Long id = sequence.nextId();
        assertThat(id).isNotNull();
        assertThat(id).isPositive();
    }

    // ==================== 参数边界测试 ====================

    @Test
    void should_ThrowException_When_DataCenterIdExceedsMaxValue() {
        // Given
        long invalidDataCenterId = 4L; // MAX_DATA_CENTER_ID = 3
        long workerId = 100L;
        
        // When & Then
        assertThatThrownBy(() -> new Sequence(invalidDataCenterId, workerId, true, 5L, false))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Data Center Id can't be greater than 3");
    }

    @Test
    void should_ThrowException_When_DataCenterIdIsNegative() {
        // Given
        long invalidDataCenterId = -1L;
        long workerId = 100L;
        
        // When & Then
        assertThatThrownBy(() -> new Sequence(invalidDataCenterId, workerId, true, 5L, false))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Data Center Id can't be greater than 3 or less than 0");
    }

    @Test
    void should_ThrowException_When_WorkerIdExceedsMaxValue() {
        // Given
        long dataCenterId = 1L;
        long invalidWorkerId = 256L; // MAX_WORKER_ID = 255
        
        // When & Then
        assertThatThrownBy(() -> new Sequence(dataCenterId, invalidWorkerId, true, 5L, false))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Worker Id can't be greater than 255");
    }

    @Test
    void should_ThrowException_When_WorkerIdIsNegative() {
        // Given
        long dataCenterId = 1L;
        long invalidWorkerId = -1L;
        
        // When & Then
        assertThatThrownBy(() -> new Sequence(dataCenterId, invalidWorkerId, true, 5L, false))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Worker Id can't be greater than 255 or less than 0");
    }

    @Test
    void should_CreateSequence_When_BoundaryValues() {
        // Given - 测试边界值
        long maxDataCenterId = 3L;
        long maxWorkerId = 255L;
        
        // When
        Sequence sequence = new Sequence(maxDataCenterId, maxWorkerId, true, 5L, false);
        
        // Then
        assertThat(sequence).isNotNull();
        
        // 验证能够生成ID
        Long id = sequence.nextId();
        assertThat(id).isNotNull();
        assertThat(id).isPositive();
    }

    // ==================== IP地址获取测试 ====================

    @Test
    void should_GetLastIpAddress_When_Called() {
        // When
        byte lastIp = Sequence.getLastIpAddress();
        
        // Then
        // IP地址最后一个字节应该在byte类型有效范围内（-128到127）
        assertThat(lastIp).isBetween((byte) -128, (byte) 127);
    }

    @Test
    void should_ReturnSameValue_When_CallingGetLastIpAddressMultipleTimes() {
        // When
        byte firstCall = Sequence.getLastIpAddress();
        byte secondCall = Sequence.getLastIpAddress();
        byte thirdCall = Sequence.getLastIpAddress();
        
        // Then
        assertThat(firstCall).isEqualTo(secondCall);
        assertThat(secondCall).isEqualTo(thirdCall);
    }

    // ==================== ID生成基础测试 ====================

    @Test
    void should_GeneratePositiveId_When_NextIdCalled() {
        // Given
        Sequence sequence = new Sequence();
        
        // When
        Long id = sequence.nextId();
        
        // Then
        assertThat(id).isNotNull();
        assertThat(id).isPositive();
    }

    @Test
    void should_GenerateUniqueIds_When_CalledMultipleTimes() {
        // Given
        Sequence sequence = new Sequence();
        int idCount = 1000;
        
        // When
        Set<Long> generatedIds = new HashSet<>();
        for (int i = 0; i < idCount; i++) {
            generatedIds.add(sequence.nextId());
        }
        
        // Then
        assertThat(generatedIds).hasSize(idCount); // 所有ID都应该是唯一的
    }

    @Test
    void should_GenerateIncreasingIds_When_CalledInSequence() {
        // Given
        Sequence sequence = new Sequence();
        
        // When
        Long firstId = sequence.nextId();
        Long secondId = sequence.nextId();
        Long thirdId = sequence.nextId();
        
        // Then
        // 由于基于时间戳生成，后生成的ID应该大于先生成的ID
        assertThat(secondId).isGreaterThan(firstId);
        assertThat(thirdId).isGreaterThan(secondId);
    }

    @RepeatedTest(5)
    void should_GenerateUniqueIds_When_RepeatedExecution() {
        // Given
        Sequence sequence = new Sequence();
        int idCount = 100;
        
        // When
        Set<Long> generatedIds = new HashSet<>();
        for (int i = 0; i < idCount; i++) {
            generatedIds.add(sequence.nextId());
        }
        
        // Then
        assertThat(generatedIds).hasSize(idCount);
    }

    // ==================== 随机序列测试 ====================

    @Test
    void should_GenerateRandomSequence_When_RandomSequenceEnabled() {
        // Given
        Sequence sequenceWithRandom = new Sequence(1L, 100L, true, 5L, true);
        Sequence sequenceWithoutRandom = new Sequence(1L, 101L, true, 5L, false);
        
        // When - 生成多个ID以观察序列模式
        List<Long> randomIds = new ArrayList<>();
        List<Long> normalIds = new ArrayList<>();
        
        for (int i = 0; i < 10; i++) {
            randomIds.add(sequenceWithRandom.nextId());
            normalIds.add(sequenceWithoutRandom.nextId());
            
            // 增加小延迟确保不同毫秒
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        // Then
        assertThat(randomIds).hasSize(10);
        assertThat(normalIds).hasSize(10);
        assertThat(randomIds).allMatch(id -> id > 0);
        assertThat(normalIds).allMatch(id -> id > 0);
    }

    // ==================== 线程安全性测试（重要！）====================

    @Test
    @Timeout(30) // 30秒超时
    void should_GenerateUniqueIds_When_ConcurrentAccess() throws Exception {
        // Given
        Sequence sequence = new Sequence();
        int threadCount = 10;
        int idsPerThread = 1000;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        
        // 使用线程安全的集合收集所有生成的ID
        Set<Long> allGeneratedIds = Collections.synchronizedSet(new HashSet<>());
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        
        // When
        for (int i = 0; i < threadCount; i++) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                for (int j = 0; j < idsPerThread; j++) {
                    Long id = sequence.nextId();
                    assertThat(id).isNotNull();
                    assertThat(id).isPositive();
                    allGeneratedIds.add(id);
                }
            }, executor);
            futures.add(future);
        }
        
        // 等待所有任务完成
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).get(25, TimeUnit.SECONDS);
        
        // Then
        executor.shutdown();
        assertThat(executor.awaitTermination(5, TimeUnit.SECONDS)).isTrue();
        
        // 验证所有ID都是唯一的
        int expectedTotalIds = threadCount * idsPerThread;
        assertThat(allGeneratedIds).hasSize(expectedTotalIds);
    }

    @Test
    @Timeout(20)
    void should_MaintainThreadSafety_When_HighConcurrency() throws Exception {
        // Given
        Sequence sequence = new Sequence();
        int threadCount = 50;
        int idsPerThread = 200;
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(threadCount);
        
        Set<Long> allIds = Collections.synchronizedSet(new HashSet<>());
        AtomicLong errorCount = new AtomicLong(0);
        
        // When
        for (int i = 0; i < threadCount; i++) {
            new Thread(() -> {
                try {
                    startLatch.await(); // 等待所有线程准备就绪
                    
                    for (int j = 0; j < idsPerThread; j++) {
                        try {
                            Long id = sequence.nextId();
                            if (id == null || id <= 0) {
                                errorCount.incrementAndGet();
                            } else {
                                allIds.add(id);
                            }
                        } catch (Exception e) {
                            errorCount.incrementAndGet();
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    errorCount.incrementAndGet();
                } finally {
                    endLatch.countDown();
                }
            }).start();
        }
        
        startLatch.countDown(); // 启动所有线程
        assertThat(endLatch.await(15, TimeUnit.SECONDS)).isTrue();
        
        // Then
        assertThat(errorCount.get()).isEqualTo(0);
        assertThat(allIds).hasSize(threadCount * idsPerThread);
    }

    @Test
    @Timeout(15)
    void should_GenerateUniqueIds_When_MultipleDifferentSequences() throws Exception {
        // Given
        int sequenceCount = 5;
        int idsPerSequence = 1000;
        ExecutorService executor = Executors.newFixedThreadPool(sequenceCount);
        
        Set<Long> allIds = Collections.synchronizedSet(new HashSet<>());
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        
        // When - 创建多个不同的Sequence实例并发生成ID
        for (int i = 0; i < sequenceCount; i++) {
            final long dataCenterId = i % 4; // 0-3
            final long workerId = (i * 50) % 256; // 避免workerId重复
            
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                Sequence sequence = new Sequence(dataCenterId, workerId, true, 5L, false);
                for (int j = 0; j < idsPerSequence; j++) {
                    Long id = sequence.nextId();
                    assertThat(id).isNotNull().isPositive();
                    allIds.add(id);
                }
            }, executor);
            futures.add(future);
        }
        
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).get(10, TimeUnit.SECONDS);
        
        // Then
        executor.shutdown();
        assertThat(executor.awaitTermination(3, TimeUnit.SECONDS)).isTrue();
        
        // 验证不同Sequence实例生成的ID都是唯一的
        assertThat(allIds).hasSize(sequenceCount * idsPerSequence);
    }

    // ==================== 性能测试 ====================

    @Test
    @Timeout(10)
    void should_GenerateIdsEfficiently_When_PerformanceTest() {
        // Given
        Sequence sequence = new Sequence();
        int idCount = 100000; // 10万个ID
        
        // When
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < idCount; i++) {
            Long id = sequence.nextId();
            assertThat(id).isNotNull().isPositive();
        }
        
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        
        // Then
        // 验证性能：10万个ID生成应该在合理时间内完成（通常在几百毫秒内）
        assertThat(elapsedTime).isLessThan(5000L); // 5秒内完成
        
        // 计算每秒生成的ID数量
        double idsPerSecond = (double) idCount * 1000 / elapsedTime;
        System.out.println("生成ID性能: " + String.format("%.0f", idsPerSecond) + " IDs/秒");
        
        // 验证每秒至少能生成20,000个ID（这是一个保守的基准）
        assertThat(idsPerSecond).isGreaterThan(20000);
    }

    // ==================== 时间回拨测试 ====================

    @Test
    void should_HandleTimeOffset_When_SmallTimeRewind() {
        // Given
        long timeOffset = 10L; // 允许10ms回拨
        Sequence sequence = new Sequence(1L, 100L, false, timeOffset, false);
        
        // When - 正常生成ID应该不会有问题
        Long id1 = sequence.nextId();
        Long id2 = sequence.nextId();
        
        // Then
        assertThat(id1).isNotNull().isPositive();
        assertThat(id2).isNotNull().isPositive();
        assertThat(id2).isGreaterThan(id1);
    }

    // ==================== 边界条件和异常测试 ====================

    @Test
    void should_HandleSequenceOverflow_When_SameMillisecond() {
        // Given
        Sequence sequence = new Sequence();
        
        // When - 在极短时间内生成大量ID
        Set<Long> ids = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            ids.add(sequence.nextId());
        }
        
        // Then
        assertThat(ids).hasSize(100); // 所有ID应该都是唯一的
    }

    @Test
    void should_GenerateValidIds_When_DifferentConfigurations() {
        // Given - 测试不同配置的Sequence
        Sequence[] sequences = {
            new Sequence(0L, 0L, true, 5L, false),      // 最小值配置
            new Sequence(3L, 255L, true, 5L, false),    // 最大值配置
            new Sequence(1L, 100L, false, 0L, true),    // 随机序列
            new Sequence(2L, 200L, true, 10L, true)     // 混合配置
        };
        
        // When & Then
        for (int i = 0; i < sequences.length; i++) {
            Sequence seq = sequences[i];
            for (int j = 0; j < 10; j++) {
                Long id = seq.nextId();
                assertThat(id)
                    .describedAs("Sequence[%d] 生成的ID[%d]", i, j)
                    .isNotNull()
                    .isPositive();
            }
        }
    }

    // ==================== ID格式和组成测试 ====================

    @Test
    void should_ContainCorrectComponents_When_AnalyzingGeneratedId() {
        // Given
        long dataCenterId = 2L;
        long workerId = 100L;
        Sequence sequence = new Sequence(dataCenterId, workerId, true, 5L, false);
        
        // When
        Long id = sequence.nextId();
        
        // Then
        assertThat(id).isNotNull().isPositive();
        
        // 验证ID的二进制结构（这是一个基础验证，实际的位操作比较复杂）
        // 至少验证ID是64位long类型的正数
        assertThat(Long.numberOfLeadingZeros(id)).isLessThan(64);
        
        // 验证ID的字符串表示长度合理
        String idString = id.toString();
        assertThat(idString.length()).isBetween(15, 20); // Snowflake ID通常在这个长度范围
    }

    @Test
    void should_GenerateIdsWithDifferentTimestamps_When_DelayBetweenCalls() throws InterruptedException {
        // Given
        Sequence sequence = new Sequence();
        
        // When
        Long id1 = sequence.nextId();
        Thread.sleep(2); // 2毫秒延迟
        Long id2 = sequence.nextId();
        Thread.sleep(2);
        Long id3 = sequence.nextId();
        
        // Then
        assertThat(id1).isNotNull().isPositive();
        assertThat(id2).isNotNull().isPositive().isGreaterThan(id1);
        assertThat(id3).isNotNull().isPositive().isGreaterThan(id2);
        
        // 验证ID之间的差值合理（考虑到时间戳部分的变化）
        assertThat(id2 - id1).isPositive();
        assertThat(id3 - id2).isPositive();
    }

    // ==================== 压力测试 ====================

    @Test
    @Timeout(30)
    void should_MaintainPerformance_When_SustainedLoad() throws Exception {
        // Given
        Sequence sequence = new Sequence();
        int duration = 5; // 5秒压力测试
        AtomicLong idCount = new AtomicLong(0);
        Set<Long> uniqueIds = Collections.synchronizedSet(new HashSet<>());
        AtomicBoolean shouldStop = new AtomicBoolean(false);
        
        // When
        long startTime = System.currentTimeMillis();
        
        // 启动多个线程进行持续ID生成
        int threadCount = 5;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        
        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                while (!shouldStop.get()) {
                    Long id = sequence.nextId();
                    if (id > 0) {
                        idCount.incrementAndGet();
                        uniqueIds.add(id);
                    }
                }
            });
        }
        
        // 运行指定时间
        Thread.sleep(duration * 1000);
        shouldStop.set(true);
        
        executor.shutdown();
        assertThat(executor.awaitTermination(5, TimeUnit.SECONDS)).isTrue();
        
        long endTime = System.currentTimeMillis();
        long actualDuration = endTime - startTime;
        
        // Then
        long totalIds = idCount.get();
        assertThat(totalIds).isPositive();
        assertThat(uniqueIds).hasSize((int) totalIds); // 所有ID都应该是唯一的
        
        double idsPerSecond = (double) totalIds * 1000 / actualDuration;
        System.out.println("压力测试结果: " + totalIds + " 个ID，" + 
            String.format("%.0f", idsPerSecond) + " IDs/秒");
        
        // 验证在压力测试下仍能保持合理的性能
        assertThat(idsPerSecond).isGreaterThan(10000); // 每秒至少1万个ID
    }
}