package com.storyhasyou.kratos.base;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.storyhasyou.kratos.dto.PageResponse;
import com.storyhasyou.kratos.exceptions.NotFountException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.util.Assert;

/**
 * The interface Base service.
 *
 * @param <T> the type parameter
 * @author fangxi created by 2020/6/18
 */
public interface BaseService<T extends BaseEntity> extends IService<T> {

    /**
     * 获取
     *
     * @param id {@code Long} ID
     * @return 领域模型 t
     */
    default T get(Long id) {
        return getOpt(id).orElseThrow(() -> new NotFountException(StrUtil.format("找不到id={}的对象", id)));
    }

    /**
     * 获取
     *
     * @param id {@code Long} ID
     * @return 领域模型 opt
     */
    default Optional<T> getOpt(Long id) {
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
    default PageResponse<T> page(int current, int limit, T entity) {
        QueryWrapper<T> queryWrapper = entity == null ? Wrappers.emptyWrapper() : Wrappers.query(entity);
        Page<T> page = this.page(new Page<>(current, limit), queryWrapper);
        return PageResponse.of(page);
    }

    /**
     * 分页
     *
     * @param current {@code int} 页码
     * @param limit   {@code int} 笔数
     * @return the page response
     */
    default PageResponse<T> page(int current, int limit) {
        return page(current, limit, null);
    }


    /**
     * 根据ids返回map
     *
     * @param ids the ids
     * @return map map
     */
    default Map<Long, T> mapByIds(List<Long> ids) {
        Assert.notEmpty(ids, "ids must not null or empty");
        List<T> entities = listByIds(ids);
        if (CollectionUtils.isEmpty(entities)) {
            return Collections.emptyMap();
        }
        return entities.stream().collect(Collectors.toMap(T::getId, Function.identity()));
    }
}
