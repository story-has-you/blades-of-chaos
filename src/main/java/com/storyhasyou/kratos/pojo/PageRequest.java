package com.storyhasyou.kratos.pojo;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


/**
 * @author fangxi
 */
@Data
public class PageRequest {

    @NotNull(message = "请传入当前页数")
    @Min(value = 1L, message = "current必须大于1")
    private Integer current;

    @NotNull(message = "请传入每页数量")
    private Integer limit;

}
