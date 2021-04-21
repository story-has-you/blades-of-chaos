package com.storyhasyou.kratos.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The type Base service.
 *
 * @param <Mapper> the type parameter
 * @param <Entity> the type parameter
 * @author fangxi
 */
public abstract class BaseServiceImpl<Mapper extends BaseMapper<Entity>, Entity extends BaseEntity> extends ServiceImpl<Mapper, Entity> implements BaseService<Entity> {

    /**
     * The Log.
     */
    protected final Logger log = LoggerFactory.getLogger(this.getClass());
    
}
