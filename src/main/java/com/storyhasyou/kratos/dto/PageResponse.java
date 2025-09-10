package com.storyhasyou.kratos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * The type Page response.
 *
 * @param <T> the type parameter
 * @author fangxi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> implements Serializable {
    /**
     * 数据
     */
    private List<T> rows;
    /**
     * 当前页
     */
    private Long current;
    /**
     * 每页的数量
     */
    private Long size;
    /**
     * 总记录数
     */
    private Long records;

    /**
     * 总页数
     */
    private Long pages;

    /**
     * 是否有更多
     */
    private Boolean hasNext;

}
