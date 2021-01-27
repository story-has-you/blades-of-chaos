package com.storyhasyou.kratos.base;

import com.storyhasyou.kratos.dto.PageRequest;
import com.storyhasyou.kratos.dto.PageResponse;
import com.storyhasyou.kratos.result.Result;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static com.storyhasyou.kratos.result.Result.error;
import static com.storyhasyou.kratos.result.Result.ok;

/**
 * 通用请求处理
 *
 * @param <T> the type parameter
 * @param <Service> the type parameter
 * @author fangxi
 */
public abstract class BaseController<Entity extends BaseEntity, Service extends BaseService<Entity>> {

    /**
     * The Log.
     */
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * The Base service.
     */
    @Autowired
    protected Service baseService;

    /**
     * 新增
     *
     * @param entity 领域模型
     * @return {@link Result}
     */
    @PostMapping("/create")
    public Result<Long> create(@RequestBody Entity entity) {
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
    public Result<Boolean> update(@RequestBody Entity entity) {
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
    public Result<Entity> get(@PathVariable Long id) {
        Entity entity = baseService.get(id);
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
    public Result<PageResponse<Entity>> page(@ModelAttribute @Validated PageRequest pageRequest,
                                        @ModelAttribute Entity entity) {
        PageResponse<Entity> pageResponse = baseService.page(pageRequest.getCurrent(), pageRequest.getLimit(), entity);
        return ok(pageResponse);
    }

    /**
     * All result.
     *
     * @return the result
     */
    @GetMapping("/all")
    public Result<List<Entity>> all() {
        List<Entity> list = baseService.list();
        return ok(list);
    }
}
