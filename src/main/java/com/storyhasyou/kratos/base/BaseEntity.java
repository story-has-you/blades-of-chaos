package com.storyhasyou.kratos.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * The type Base entity.
 *
 * @author fangxi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class BaseEntity extends Identity<Long> {

    /**
     * 创建时间 默认当前时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间 默认当前时间
     */
    private LocalDateTime updateTime;

    /**
     * 删除标识位 默认不删除
     */
    @TableLogic
    private Integer deleted;
}
