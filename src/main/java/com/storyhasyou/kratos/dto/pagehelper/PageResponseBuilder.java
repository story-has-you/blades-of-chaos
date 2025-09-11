package com.storyhasyou.kratos.dto.pagehelper;

import com.github.pagehelper.PageInfo;
import com.storyhasyou.kratos.dto.PageResponse;
import com.storyhasyou.kratos.utils.BeanUtils;
import com.storyhasyou.kratos.utils.CollectionUtils;

import java.util.List;
import java.util.function.Function;

/**
 * The type Page response.
 *
 * @author fangxi
 */
public final class PageResponseBuilder {

    public static <R, T> PageResponse<R> of(PageInfo<T> page, Function<T, R> function) {
        List<T> records = page.getList();
        List<R> mappedRows = CollectionUtils.map(records, function);
        return getResponse(page.getPageNum(), page.getPageSize(), page.getTotal(), page.getPages(), page.isHasNextPage(), mappedRows);
    }


    public static <R, T> PageResponse<R> of(PageInfo<T> page, Class<R> clazz) {
        List<T> records = page.getList();
        List<R> rows = BeanUtils.copyProperties(records, clazz);
        return getResponse(page.getPageNum(), page.getPageSize(), page.getTotal(), page.getPages(), page.isHasNextPage(), rows);
    }


    public static <T> PageResponse<T> of(PageInfo<T> page) {
        return of(page, Function.identity());
    }


    public static <T, R> PageResponse<R> of(PageInfo<T> page, List<R> rows) {
        return getResponse(page.getPageNum(), page.getPageSize(), page.getTotal(), page.getPages(), page.isHasNextPage(), rows);
    }

    public static <T> PageResponse<T> empty(long current, long limit) {
        return getResponse(current, limit, 0, 0, false, List.of());
    }

    public static <T> PageResponse<T> empty() {
        return getResponse(0, 0, 0, 0, false, List.of());
    }

    private static <R> PageResponse<R> getResponse(long current, long size, long total, long pages, boolean hasNext) {
        return PageResponse.<R>builder()
                .current(current)
                .size(size)
                .records(total)
                .pages(pages)
                .hasNext(hasNext)
                .build();
    }

    private static <R> PageResponse<R> getResponse(long current, long size, long total, long pages, boolean hasNext, List<R> rows) {
        return PageResponse.<R>builder()
                .rows(rows)
                .current(current)
                .size(size)
                .records(total)
                .pages(pages)
                .hasNext(hasNext)
                .build();
    }

}
