package com.fxipp.kratos.pojo;

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

}

