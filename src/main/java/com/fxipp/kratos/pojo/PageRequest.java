package com.fxipp.kratos.pojo;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


/**
 * @author fangxi
 */
@Data
public class PageRequest {

    @NotNull(message = "请传入分页参数")
    @Min(value = 1L, message = "page必须大于1")
    private Integer current;

    @NotNull(message = "请传入分页参数")
    private Integer limit;

}
