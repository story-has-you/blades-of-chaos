package com.storyhasyou.kratos.base;

import com.storyhasyou.kratos.pojo.PageRequest;
import com.storyhasyou.kratos.pojo.PageResponse;
import com.storyhasyou.kratos.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 通用请求处理
 *
 * @author fangxi
 */
public abstract class BaseController<T extends BaseEntity, S extends BaseService<T>> {

    @Autowired
    protected S baseService;

    /**
     * 新增
     *
     * @param entity 领域模型
     * @return {@link Result}
     */
    @PostMapping("/create")
    public Result<Boolean> create(@RequestBody T entity) {
        // 业务逻辑
        boolean created = baseService.save(entity);
        return Result.ok(created);
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
        return Result.ok(deleted);
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
        return Result.ok(deleted);
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
        return Result.ok(updated);

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
        return Result.ok(entity);
    }

    /**
     * 分页
     *
     * @param pageRequest 分页对象
     * @return {@link Result}
     */
    @GetMapping("/page")
    public Result<PageResponse<?>> page(@ModelAttribute @Validated PageRequest pageRequest,
                                        @ModelAttribute T entity) {
        PageResponse<?> pageResponse = baseService.page(pageRequest.getCurrent(), pageRequest.getLimit(), entity);
        return Result.ok(pageResponse);
    }
}