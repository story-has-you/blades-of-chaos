package com.fxipp.kratos.pojo;

import com.baomidou.mybatisplus.core.metadata.IPage;
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

}

