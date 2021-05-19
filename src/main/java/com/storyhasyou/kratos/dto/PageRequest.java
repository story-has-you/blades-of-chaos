package com.storyhasyou.kratos.dto;

import java.io.Serializable;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * The type Page request.
 *
 * @author fangxi
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequest implements Serializable {


    @NotNull(message = "请传入当前页数")
    @Min(value = 1L, message = "current必须大于1")
    private Integer current;

    @NotNull(message = "请传入每页数量")
    private Integer limit;

}
