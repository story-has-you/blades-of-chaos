package com.storyhasyou.kratos.base;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import springfox.documentation.annotations.ApiIgnore;

/**
 * The type Base entity.
 *
 * @author fangxi
 */
@Data
public abstract class BaseEntity implements Serializable {

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 创建时间 默认当前时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @ApiModelProperty(hidden = true)
    private LocalDateTime createTime;

    /**
     * 更新时间 默认当前时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(hidden = true)
    private LocalDateTime updateTime;

    /**
     * 删除标识位 默认不删除
     */
    @TableField(value = "deleted", fill = FieldFill.INSERT)
    @TableLogic
    @ApiModelProperty(hidden = true)
    private Integer deleted;
}
