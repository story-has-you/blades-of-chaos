package com.storyhasyou.kratos.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.storyhasyou.kratos.repository.DefaultValueMetaObjectHandler;
import com.storyhasyou.kratos.utils.JsonUtils;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

/**
 * The type Blades of chaos config.
 *
 * @author fangxi created by 2020/11/1
 */
@SpringBootConfiguration
public class BladesOfChaosConfig {

    /**
     * Default value meta object handler default value meta object handler.
     *
     * @return the default value meta object handler
     */
    @Bean
    public DefaultValueMetaObjectHandler defaultValueMetaObjectHandler() {
        return new DefaultValueMetaObjectHandler();
    }

    /**
     * Mapping jackson 2 http message converter mapping jackson 2 http message converter.
     *
     * @return the mapping jackson 2 http message converter
     */
    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        return JsonUtils.mappingJackson2HttpMessageConverter(PropertyNamingStrategy.SNAKE_CASE);
    }

    /**
     * 新的分页插件,一缓和二缓遵循mybatis的规则,需要设置 MybatisConfiguration#useDeprecatedExecutor = false 避免缓存出现问题(该属性会在旧插件移除后一同移除)
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        paginationInnerInterceptor.setDbType(DbType.MYSQL);
        paginationInnerInterceptor.setMaxLimit(500L);
        interceptor.addInnerInterceptor(paginationInnerInterceptor);
        return interceptor;
    }

}
