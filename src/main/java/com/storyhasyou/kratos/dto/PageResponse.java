package com.storyhasyou.kratos.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.storyhasyou.kratos.utils.BeanUtils;
import com.storyhasyou.kratos.utils.CollectionUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * The type Page response.
 *
 * @param <T> the type parameter
 * @author fangxi
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> implements Serializable {
    /**
     * 数据
     */
    private Collection<T> rows = Lists.newArrayList();
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

    public static <R, T> PageResponse<R> of(Function<T, R> function, Page<T> page) {
        List<T> records = page.getRecords();
        PageResponse<R> response = getResponse(page.getCurrent(), page.getSize(), page.getTotal(), page.getPages(), page.hasNext());
        response.setRows(CollectionUtils.map(records, function));
        return response;
    }

    public static <R, T> PageResponse<R> of(Class<R> clazz, Page<T> page) {
        return of(o -> BeanUtils.copyProperties(o, clazz), page);
    }

    public static <T> PageResponse<T> of(Page<T> page) {
        return of(Function.identity(), page);
    }

    public static <T, R> PageResponse<R> of(Collection<R> rows, Page<T> page) {
        PageResponse<R> response = getResponse(page.getCurrent(), page.getSize(), page.getTotal(), page.getPages(), page.hasNext());
        response.setRows(rows);
        return response;
    }

    public static <T, R> PageResponse<T> empty(Page<R> page) {
        return getResponse(page.getCurrent(), page.getSize(), 0, 0, false);
    }

    public static <T> PageResponse<T> empty(long current, long limit) {
        return getResponse(current, limit, 0, 0, false);
    }

    public static <T> PageResponse<T> empty() {
        return getResponse(0, 0, 0, 0, false);
    }

    private static <R> PageResponse<R> getResponse(long current, long size, long total, long pages, boolean b) {
        PageResponse<R> response = new PageResponse<>();
        response.setCurrent(current);
        response.setSize(size);
        response.setRecords(total);
        response.setPages(pages);
        response.setHasNext(b);
        return response;
    }

}

