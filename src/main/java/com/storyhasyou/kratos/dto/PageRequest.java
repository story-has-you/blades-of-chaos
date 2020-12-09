package com.storyhasyou.kratos.dto;

import java.io.Serializable;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Data;


/**
 * The type Page request.
 *
 * @author fangxi
 */
@Data
public class PageRequest implements Serializable {

    protected static final int DEFAULT_CURRENT = 1;
    protected static final int DEFAULT_LIMIT = 10;

    @NotNull(message = "请传入当前页数")
    @Min(value = 1L, message = "current必须大于1")
    private Integer current;

    @NotNull(message = "请传入每页数量")
    private Integer limit;

}
