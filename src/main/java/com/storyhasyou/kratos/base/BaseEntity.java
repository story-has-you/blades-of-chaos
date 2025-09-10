package com.storyhasyou.kratos.base;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * The type Base entity.
 *
 * @author fangxi
 */
@Data
public abstract class BaseEntity {

    private Long id;

    /**
     * 创建时间 默认当前时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间 默认当前时间
     */
    private LocalDateTime updateTime;

}
