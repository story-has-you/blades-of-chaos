package com.storyhasyou.kratos.base;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

/**
 * @author fangxi created by 2020/6/17
 */
public class DefaultValueMetaObjectHandler implements MetaObjectHandler {

    /**
     * 通用字段：创建时间
     */
    private static final String CREATE_TIME = "createTime";

    /**
     * 通用字段：更新时间
     */
    private static final String UPDATE_TIME = "updateTime";

    /**
     * 通用字段：已删除
     */
    private static final String DELETED = "deleted";

    @Override
    public void insertFill(MetaObject metaObject) {
        // 判断是否有相关字段
        boolean hasCreateTime = metaObject.hasSetter(CREATE_TIME);
        boolean hasUpdateTime = metaObject.hasSetter(UPDATE_TIME);
        boolean hasDeleted = metaObject.hasSetter(DELETED);
        LocalDateTime now = LocalDateTime.now();
        // 有字段则自动填充
        if (hasCreateTime) {
            strictInsertFill(metaObject, CREATE_TIME, LocalDateTime.class, now);
        }
        if (hasUpdateTime) {
            strictInsertFill(metaObject, UPDATE_TIME, LocalDateTime.class, now);
        }
        if (hasDeleted) {
            strictInsertFill(metaObject, DELETED, Integer.class, 0);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Object val = getFieldValByName(UPDATE_TIME, metaObject);
        // 没有自定义值时才更新字段
        if (val == null) {
            strictUpdateFill(metaObject, UPDATE_TIME, LocalDateTime.class, LocalDateTime.now());
        }
    }
}
