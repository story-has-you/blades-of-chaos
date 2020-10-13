package com.storyhasyou.kratos.base;

import com.storyhasyou.kratos.dto.PageRequest;
import com.storyhasyou.kratos.dto.PageResponse;
import com.storyhasyou.kratos.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.storyhasyou.kratos.result.Result.error;
import static com.storyhasyou.kratos.result.Result.ok;

/**
 * 通用请求处理
 *
 * @param <T> the type parameter
 * @param <S> the type parameter
 * @author fangxi
 */
public abstract class BaseController<T extends BaseEntity, S extends BaseService<T>> {

    /**
     * The Log.
     */
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * The Base service.
     */
    @Autowired
    protected S baseService;

    /**
     * 新增
     *
     * @param entity 领域模型
     * @return {@link Result}
     */
    @PostMapping("/create")
    public Result<Long> create(@RequestBody T entity) {
        // 业务逻辑
        boolean created = baseService.save(entity);
        return created ? ok(entity.getId()) : error();
    }

    /**
     * 删除
     *
     * @param id {@code Long}
     * @return {@link Result}
     */
    @DeleteMapping("/remove/{id}")
    public Result<Boolean> remove(@PathVariable Long id) {
        // 业务逻辑
        boolean deleted = baseService.removeById(id);
        return ok(deleted);
    }

    /**
     * 删除
     *
     * @param ids {@code Long}
     * @return {@link Result}
     */
    @DeleteMapping("/batch/remove")
    public Result<Boolean> removeBatch(@RequestBody List<Long> ids) {
        // 业务逻辑
        boolean deleted = baseService.removeByIds(ids);
        return ok(deleted);
    }

    /**
     * 修改
     *
     * @param entity 领域模型
     * @return {@link Result}
     */
    @PutMapping("/update")
    public Result<Boolean> update(@RequestBody T entity) {
        // 业务逻辑
        boolean updated = baseService.updateById(entity);
        return ok(updated);

    }

    /**
     * 获取
     *
     * @param id {@code Long}
     * @return {@link Result}
     */
    @GetMapping("/get/{id}")
    public Result<T> get(@PathVariable Long id) {
        T entity = baseService.get(id);
        return ok(entity);
    }

    /**
     * 分页
     *
     * @param pageRequest 分页对象
     * @param entity      the entity
     * @return {@link Result}
     */
    @GetMapping("/page")
    public Result<PageResponse<?>> page(@ModelAttribute @Validated PageRequest pageRequest,
                                        @ModelAttribute T entity) {
        PageResponse<?> pageResponse = baseService.page(pageRequest.getCurrent(), pageRequest.getLimit(), entity);
        return ok(pageResponse);
    }
}
