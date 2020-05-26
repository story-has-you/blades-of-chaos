package com.fxipp.kratos.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author fangxi
 */
@Data
public class BaseEntity implements Serializable {

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

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
    @TableField(value = "is_deleted")
    @TableLogic
    private Integer deleted;
}
