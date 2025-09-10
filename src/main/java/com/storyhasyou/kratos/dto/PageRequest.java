package com.storyhasyou.kratos.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * The type Page request.
 *
 * @author fangxi
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageRequest implements Serializable {


    @NotNull(message = "请传入当前页数")
    @Min(value = 1L, message = "current必须大于1")
    private Integer current;

    @NotNull(message = "请传入每页数量")
    private Integer limit;

}
