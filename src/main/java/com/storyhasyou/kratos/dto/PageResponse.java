package com.storyhasyou.kratos.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 分页响应数据传输对象
 * <p>
 * 使用 JDK 21 Record 特性实现的泛型分页响应类，提供不可变的分页数据封装。
 * Record 类自动提供 equals、hashCode、toString 方法，确保线程安全和数据一致性。
 * </p>
 *
 * @param <T> 分页数据的泛型类型
 * @param rows    分页数据列表，不可为null
 * @param current 当前页码，从1开始
 * @param size    每页显示数量
 * @param records 总记录数
 * @param pages   总页数
 * @param hasNext 是否有下一页
 * @author fangxi
 * @since 3.3.6
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PageResponse<T>(
        List<T> rows,
        Long current,
        Long size,
        Long records,
        Long pages,
        Boolean hasNext
) implements Serializable {

    /**
     * 紧凑构造器，用于数据验证和规范化
     * <p>
     * 遵循《阿里巴巴Java开发规范》：
     * 1. 对输入参数进行空值检查和数据校验
     * 2. 确保分页参数的合理性
     * 3. 提供不可变的防御性拷贝
     * </p>
     */
    public PageResponse {
        // 【强制】对于POJO类属性必须使用包装数据类型，进行空值校验
        Objects.requireNonNull(rows, "分页数据列表不能为null");
        
        // 【推荐】防御性编程，创建不可变副本
        rows = Collections.unmodifiableList(rows);
        
        // 【强制】数据校验，确保分页参数合理性
        if (current != null && current < 1L) {
            throw new IllegalArgumentException("当前页码必须大于等于1");
        }
        if (size != null && size < 0L) {
            throw new IllegalArgumentException("每页数量不能为负数");
        }
        if (records != null && records < 0L) {
            throw new IllegalArgumentException("总记录数不能为负数");
        }
        if (pages != null && pages < 0L) {
            throw new IllegalArgumentException("总页数不能为负数");
        }
    }

    /**
     * 创建空的分页响应
     *
     * @param <T> 泛型类型
     * @return 空的分页响应对象
     */
    public static <T> PageResponse<T> empty() {
        return new PageResponse<>(
                Collections.emptyList(),
                1L,
                0L,
                0L,
                0L,
                Boolean.FALSE
        );
    }

    /**
     * 根据分页数据和分页请求创建分页响应
     *
     * @param <T>         泛型类型
     * @param rows        分页数据列表
     * @param pageRequest 分页请求参数
     * @param totalCount  总记录数
     * @return 分页响应对象
     */
    public static <T> PageResponse<T> of(List<T> rows, PageRequest pageRequest, Long totalCount) {
        Objects.requireNonNull(rows, "分页数据列表不能为null");
        Objects.requireNonNull(pageRequest, "分页请求参数不能为null");
        Objects.requireNonNull(totalCount, "总记录数不能为null");

        long current = pageRequest.current();
        long limit = pageRequest.limit();
        long totalPages = totalCount == 0 ? 0 : (totalCount - 1) / limit + 1;
        boolean hasNext = current < totalPages;

        return new PageResponse<>(
                rows,
                current,
                limit,
                totalCount,
                totalPages,
                hasNext
        );
    }

    /**
     * 创建分页响应的建造者
     *
     * @param <T> 泛型类型
     * @return PageResponseBuilder实例
     */
    public static <T> PageResponseBuilder<T> builder() {
        return new PageResponseBuilder<>();
    }

    /**
     * 判断分页数据是否为空
     *
     * @return true表示无数据，false表示有数据
     */
    public boolean isEmpty() {
        return rows == null || rows.isEmpty();
    }

    /**
     * 判断分页数据是否不为空
     *
     * @return true表示有数据，false表示无数据
     */
    public boolean hasData() {
        return !isEmpty();
    }

    /**
     * 获取当前页数据数量
     *
     * @return 当前页的数据条数
     */
    public int getCurrentPageSize() {
        return rows == null ? 0 : rows.size();
    }

    /**
     * 分页响应建造者模式实现
     * <p>
     * 由于Record不支持Lombok的@Builder注解，提供自定义Builder实现。
     * 遵循《阿里巴巴Java开发规范》的建造者模式最佳实践。
     * </p>
     *
     * @param <T> 泛型类型
     */
    public static final class PageResponseBuilder<T> {
        private List<T> rows;
        private Long current;
        private Long size;
        private Long records;
        private Long pages;
        private Boolean hasNext;

        private PageResponseBuilder() {
        }

        /**
         * 设置分页数据列表
         *
         * @param rows 分页数据列表
         * @return Builder实例
         */
        public PageResponseBuilder<T> rows(List<T> rows) {
            this.rows = rows;
            return this;
        }

        /**
         * 设置当前页码
         *
         * @param current 当前页码
         * @return Builder实例
         */
        public PageResponseBuilder<T> current(Long current) {
            this.current = current;
            return this;
        }

        /**
         * 设置每页数量
         *
         * @param size 每页数量
         * @return Builder实例
         */
        public PageResponseBuilder<T> size(Long size) {
            this.size = size;
            return this;
        }

        /**
         * 设置总记录数
         *
         * @param records 总记录数
         * @return Builder实例
         */
        public PageResponseBuilder<T> records(Long records) {
            this.records = records;
            return this;
        }

        /**
         * 设置总页数
         *
         * @param pages 总页数
         * @return Builder实例
         */
        public PageResponseBuilder<T> pages(Long pages) {
            this.pages = pages;
            return this;
        }

        /**
         * 设置是否有下一页
         *
         * @param hasNext 是否有下一页
         * @return Builder实例
         */
        public PageResponseBuilder<T> hasNext(Boolean hasNext) {
            this.hasNext = hasNext;
            return this;
        }

        /**
         * 构建PageResponse实例
         *
         * @return PageResponse实例
         */
        public PageResponse<T> build() {
            // 【强制】确保rows不为null，提供默认值
            if (rows == null) {
                rows = Collections.emptyList();
            }
            return new PageResponse<>(rows, current, size, records, pages, hasNext);
        }
    }
}
