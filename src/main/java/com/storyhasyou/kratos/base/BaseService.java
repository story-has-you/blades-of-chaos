package com.storyhasyou.kratos.base;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.storyhasyou.kratos.dto.PageResponse;
import com.storyhasyou.kratos.exceptions.NotFountException;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The interface Base service.
 *
 * @param <Entity> the type parameter
 * @author fangxi created by 2020/6/18
 */
@SuppressWarnings("all")
public interface BaseService<Entity extends BaseEntity> extends IService<Entity> {

    /**
     * 获取
     *
     * @param id {@code Long} ID
     * @return 领域模型 t
     */
    default Entity get(Long id) {
        return getOpt(id).orElseThrow(() -> new NotFountException(StrUtil.format("找不到id={}的对象", id)));
    }

    /**
     * 获取
     *
     * @param id {@code Long} ID
     * @return 领域模型 opt
     */
    default Optional<Entity> getOpt(Long id) {
        return Optional.ofNullable(getById(id));
    }

    /**
     * 分页
     *
     * @param current {@code int} 页码
     * @param limit   {@code int} 笔数
     * @param entity  领域模型
     * @return 管理员分页数据 page response
     */
    default PageResponse<Entity> page(int current, int limit, Entity entity) {
        Assert.isTrue(current > 0, "current must be greater than or equal to 1");
        Assert.isTrue(limit > 0, "limit must be greater than 0");
        Page<Entity> page = lambdaQuery()
                .setEntity(entity)
                .setEntityClass((Class<Entity>) entity.getClass())
                .orderByDesc(BaseEntity::getCreateTime)
                .page(new Page<>(current, limit));
        return PageResponse.of(page);
    }

    /**
     * 分页
     *
     * @param current {@code int} 页码
     * @param limit   {@code int} 笔数
     * @return the page response
     */
    default PageResponse<Entity> page(int current, int limit) {
        return page(current, limit, null);
    }


    /**
     * 根据ids分组
     *
     * @param ids the ids
     * @return map map
     */
    default Map<Long, Entity> grouping(List<Long> ids) {
        return grouping(ids, BaseEntity::getId);
    }

    /**
     * 根据字段分组
     *
     * @param <T>       the type parameter
     * @param ids       the ids
     * @param sFunction the s function
     * @return map map
     */
    default <T> Map<T, Entity> grouping(List<T> ids, SFunction<Entity, T> sFunction) {
        List<Entity> entities = lambdaQuery().in(sFunction, ids).list();
        if (CollectionUtils.isEmpty(entities)) {
            return Collections.emptyMap();
        }
        return entities.stream().collect(Collectors.toMap(sFunction, Function.identity()));
    }


    /**
     * Uniqueness boolean.
     *
     * @param column the column
     * @param value  the value
     * @return the boolean
     */
    default boolean exists(SFunction<Entity, ?> column, Object value) {
        return lambdaQuery().eq(column, value).count() > 0;
    }

    /**
     * Uniqueness boolean.
     *
     * @param params the params
     * @return the boolean
     */
    default boolean exists(Map<SFunction<Entity, ?>, Object> params) {
        return lambdaQuery().allEq(params).count() > 0;
    }
}
