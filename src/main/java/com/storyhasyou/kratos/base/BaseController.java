package com.storyhasyou.kratos.base;

import com.storyhasyou.kratos.pojo.PageRequest;
import com.storyhasyou.kratos.pojo.PageResponse;
import com.storyhasyou.kratos.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

import static com.storyhasyou.kratos.result.Result.ok;

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
        return ok(created);
    }

    /**
     * 删除
     *
     * @param id {@code Long}
     * @return {@link Result}
     */
    @DeleteMapping("/remove/{id}")
    public Result<Boolean> remove(@PathVariable Serializable id) {
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
    public Result<Boolean> removeBatch(@RequestBody List<Serializable> ids) {
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
    public Result<T> get(@PathVariable Serializable id) {
        T entity = baseService.get(id);
        return ok(entity);
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
        return ok(pageResponse);
    }
}
