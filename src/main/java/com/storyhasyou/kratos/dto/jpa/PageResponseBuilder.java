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
        PageResponse<R> response = getResponse(page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages(), page.hasNext());
        response.setRows(CollectionUtils.map(records, function));
        return response;
    }

    public static <R, T> PageResponse<R> of(Page<T> page, Class<R> clazz) {
        List<T> records = page.getContent();
        List<R> rows = BeanUtils.copyProperties(records, clazz);
        PageResponse<R> response = getResponse(page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages(), page.hasNext());
        response.setRows(rows);
        return response;
    }

    public static <T> PageResponse<T> of(Page<T> page) {
        return of(page, Function.identity());
    }


    public static <T, R> PageResponse<R> of(List<R> rows, Page<T> page) {
        PageResponse<R> response = getResponse(page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages(), page.hasNext());
        response.setRows(rows);
        return response;
    }

    public static <T, R> PageResponse<T> empty(Page<R> page) {
        return getResponse(page.getNumber(), page.getSize(), 0, 0, false);
    }

    public static <T> PageResponse<T> empty(long current, long limit) {
        return getResponse(current, limit, 0, 0, false);
    }

    public static <T> PageResponse<T> empty() {
        return getResponse(0, 0, 0, 0, false);
    }

    private static <R> PageResponse<R> getResponse(long current, long size, long total, long pages, boolean b) {
        return PageResponse.<R>builder()
                .current(current)
                .size(size)
                .records(total)
                .pages(pages)
                .hasNext(b)
                .build();
    }

}
