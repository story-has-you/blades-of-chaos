package com.storyhasyou.kratos.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.storyhasyou.kratos.utils.BeanUtils;
import com.storyhasyou.kratos.utils.CollectionUtils;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Collection<T> rows;
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
        PageResponse<R> response = new PageResponse<>();
        response.setCurrent(page.getCurrent());
        response.setSize(page.getSize());
        response.setRecords(page.getTotal());
        response.setPages(page.getPages());
        response.setHasNext(page.hasNext());
        response.setRows(CollectionUtils.map(records, function));
        return response;
    }

    public static <R, T> PageResponse<R> of(Class<R> clazz, Page<T> page) {
        return of(o -> BeanUtils.copyProperties(o, clazz), page);
    }

    public static <T> PageResponse<T> of(Page<T> page) {
        return of(Function.identity(), page);
    }


}

