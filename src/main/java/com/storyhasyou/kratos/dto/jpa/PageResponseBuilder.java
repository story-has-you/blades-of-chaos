package com.storyhasyou.kratos.dto.jpa;

import com.storyhasyou.kratos.dto.PageResponse;
import com.storyhasyou.kratos.utils.BeanUtils;
import com.storyhasyou.kratos.utils.CollectionUtils;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

/**
 * The type Page response.
 *
 * @author fangxi
 */
public final class PageResponseBuilder {


    public static <R, T> PageResponse<R> of(Page<T> page, Function<T, R> function) {
        List<T> records = page.getContent();
        List<R> mappedRows = CollectionUtils.map(records, function);
        return getResponse(page.getNumber() + 1, page.getSize(), page.getTotalElements(), page.getTotalPages(), page.hasNext(), mappedRows);
    }

    public static <R, T> PageResponse<R> of(Page<T> page, Class<R> clazz) {
        List<T> records = page.getContent();
        List<R> rows = BeanUtils.copyProperties(records, clazz);
        return getResponse(page.getNumber() + 1, page.getSize(), page.getTotalElements(), page.getTotalPages(), page.hasNext(), rows);
    }

    public static <T> PageResponse<T> of(Page<T> page) {
        return of(page, Function.identity());
    }

    public static <T, R> PageResponse<R> of(List<R> rows, Page<T> page) {
        return getResponse(page.getNumber() + 1, page.getSize(), page.getTotalElements(), page.getTotalPages(), page.hasNext(), rows);
    }

    public static <T, R> PageResponse<T> empty(Page<R> page) {
        return getResponse(page.getNumber() + 1, page.getSize(), 0, 0, false, List.of());
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
                .limit(size)
                .records(total)
                .pages(pages)
                .hasNext(hasNext)
                .build();
    }

    private static <R> PageResponse<R> getResponse(long current, long size, long total, long pages, boolean hasNext, List<R> rows) {
        return PageResponse.<R>builder()
                .rows(rows)
                .current(current)
                .limit(size)
                .records(total)
                .pages(pages)
                .hasNext(hasNext)
                .build();
    }

}
