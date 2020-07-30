package com.storyhasyou.kratos.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author fangxi
 */
public abstract class BaseServiceImpl<M extends BaseMapper<T>, T extends BaseEntity> extends ServiceImpl<M, T> implements BaseService<T> {

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    protected boolean uniqueness(SFunction<T, ?> column, Object value) {
        return lambdaQuery().eq(column, value).count() > 0;
    }
}
