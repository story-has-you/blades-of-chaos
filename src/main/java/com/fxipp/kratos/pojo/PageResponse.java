package com.fxipp.kratos.pojo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fxipp.kratos.utils.BeanUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author fangxi
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "of")
@Accessors(chain = true)
public class PageResponse<T> {
    /**
     * 数据
     */
    @NonNull
    private List<T> rows;
    /**
     * 总记录数
     */
    @NonNull
    private Long records;
    /**
     * 总页数
     */
    @NonNull
    private Long pages;
    /**
     * 每页的数量
     */
    @NonNull
    private Long size;


    public static PageResponse<?> of(IPage<?> iPage) {
        List<?> records = iPage.getRecords();
        long total = iPage.getTotal();
        long pages = iPage.getPages();
        long size = iPage.getSize();
        return PageResponse.of(records, total, pages, size);
    }

    public static <T> PageResponse<T> of(IPage<?> iPage, Class<T> target) {
        List<?> records = iPage.getRecords();
        long total = iPage.getTotal();
        long pages = iPage.getPages();
        long size = iPage.getSize();
        List<T> rows = BeanUtils.copyProperties(records, target);
        return PageResponse.of(rows, total, pages, size);
    }

    public static <T> PageResponse<T> of(IPage<?> iPage, List<T> rows) {
        long total = iPage.getTotal();
        long pages = iPage.getPages();
        long size = iPage.getSize();
        return PageResponse.of(rows, total, pages, size);
    }

}

