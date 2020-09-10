package com.storyhasyou.kratos.pojo;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * The type Page request.
 *
 * @author fangxi
 */
@Data
public class PageRequest implements Serializable {

    private static final int DEFAULT_CURRENT = 1;
    private static final int DEFAULT_LIMIT = 10;

    @NotNull(message = "请传入当前页数")
    @Min(value = 1L, message = "current必须大于1")
    private Integer current = DEFAULT_CURRENT;

    @NotNull(message = "请传入每页数量")
    private Integer limit = DEFAULT_LIMIT;

}
