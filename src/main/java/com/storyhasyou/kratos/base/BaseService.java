package com.storyhasyou.kratos.base;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.storyhasyou.kratos.exceptions.NotFountException;
import com.storyhasyou.kratos.pojo.PageResponse;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author fangxi created by 2020/6/18
 */
public interface BaseService<T extends BaseEntity> extends IService<T> {

    /**
     * 获取
     *
     * @param id {@code Serializable} ID
     * @return 领域模型
     */
    default T get(Serializable id) {
        return getOpt(id).orElseThrow(NotFountException::new);
    }

    /**
     * 获取
     *
     * @param id {@code Serializable} ID
     * @return 领域模型
     */
    default Optional<T> getOpt(Serializable id) {
        return Optional.ofNullable(getById(id));
    }

    /**
     * 分页
     *
     * @param current {@code int} 页码
     * @param limit   {@code int} 笔数
     * @param entity  领域模型
     * @return 管理员分页数据
     */
    default PageResponse<?> page(int current, int limit, T entity) {
        Page<?> page = page(new Page<>(current, limit), Wrappers.query(entity));
        return PageResponse.of(page.getRecords(), page.getTotal(), page.getPages(), page.getSize());
    }


    /**
     * 根据ids返回map
     *
     * @param ids
     * @return
     */
    default Map<Serializable, T> mapByIds(List<Serializable> ids) {
        Assert.notEmpty(ids, "ids must not null or empty");
        List<T> entities = listByIds(ids);
        if (CollectionUtils.isEmpty(entities)) {
            return Collections.emptyMap();
        }
        return entities.stream().collect(Collectors.toMap(T::getId, Function.identity()));
    }
}
