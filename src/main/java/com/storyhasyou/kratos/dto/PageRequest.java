package com.storyhasyou.kratos.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * 分页请求参数封装类
 * <p>
 * 使用 JDK 21 Record 特性实现不可变数据传输对象，提供分页查询的基础参数。
 * Record 类自动生成构造方法、getter 方法、equals、hashCode 和 toString 方法。
 * </p>
 *
 * @param current 当前页码，从1开始
 * @param limit   每页显示数量
 * @author fangxi
 * @since 3.3.6
 */
public record PageRequest(
        @NotNull(message = "请传入当前页数")
        @Min(value = 1L, message = "current必须大于1")
        Integer current,

        @NotNull(message = "请传入每页数量")
        @Min(value = 1L, message = "limit必须大于1")
        Integer limit
) implements Serializable {

    /**
     * 创建分页请求对象的静态工厂方法
     *
     * @param current 当前页码，从1开始
     * @param limit   每页显示数量
     * @return PageRequest 实例
     */
    public static PageRequest of(Integer current, Integer limit) {
        return new PageRequest(current, limit);
    }

    /**
     * 创建默认分页请求对象（第1页，每页10条）
     *
     * @return 默认的 PageRequest 实例
     */
    public static PageRequest defaultRequest() {
        return new PageRequest(1, 10);
    }

    /**
     * 计算偏移量（用于数据库查询）
     *
     * @return 查询偏移量
     */
    public int getOffset() {
        return (current - 1) * limit;
    }

    /**
     * Record 类的紧凑构造器，用于参数验证
     * <p>
     * 在创建 Record 实例时自动调用，确保数据的有效性
     * </p>
     */
    public PageRequest {
        if (current != null && current < 1) {
            throw new IllegalArgumentException("current必须大于1");
        }
        if (limit != null && limit < 1) {
            throw new IllegalArgumentException("limit必须大于1");
        }
        // 防止每页数量过大导致性能问题
        if (limit != null && limit > 1000) {
            throw new IllegalArgumentException("limit不能超过1000");
        }
    }
}
