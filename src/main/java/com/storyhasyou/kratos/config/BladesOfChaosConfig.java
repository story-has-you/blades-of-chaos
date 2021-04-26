package com.storyhasyou.kratos.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.storyhasyou.kratos.exceptions.BusinessExceptionHandler;
import com.storyhasyou.kratos.handler.HttpRequestFilter;
import com.storyhasyou.kratos.repository.DefaultValueMetaObjectHandler;
import com.storyhasyou.kratos.utils.JacksonUtils;
import com.storyhasyou.kratos.utils.OkHttpUtils;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

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
     * Exception handler business exception handler.
     *
     * @return the business exception handler
     */
    @Bean
    @ConditionalOnMissingBean(BusinessExceptionHandler.class)
    public BusinessExceptionHandler exceptionHandler() {
        return new BusinessExceptionHandler();
    }


    /**
     * Mapping jackson 2 http message converter mapping jackson 2 http message converter.
     *
     * @return the mapping jackson 2 http message converter
     */
    @Bean
    @ConditionalOnMissingBean(MappingJackson2HttpMessageConverter.class)
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        return JacksonUtils.mappingJackson2HttpMessageConverter();
    }

    /**
     * 新的分页插件,一缓和二缓遵循mybatis的规则,需要设置
     * MybatisConfiguration#useDeprecatedExecutor = false 避免缓存出现问题(该属性会在旧插件移除后一同移除)
     *
     * @return the mybatis plus interceptor
     */
    @Bean
    @ConditionalOnMissingBean(MybatisPlusInterceptor.class)
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        paginationInnerInterceptor.setDbType(DbType.MYSQL);
        paginationInnerInterceptor.setMaxLimit(500L);
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(paginationInnerInterceptor);
        return mybatisPlusInterceptor;
    }

    /**
     * Global config global config.
     *
     * @return the global config
     */
    @Bean
    @ConditionalOnBean(GlobalConfig.class)
    public GlobalConfig globalConfig() {
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setBanner(false);
        return globalConfig;
    }

    /**
     * Trace id handler http request filter.
     *
     * @return the http request filter
     */
    @Bean
    public HttpRequestFilter traceIdHandler() {
        return new HttpRequestFilter();
    }


    /**
     * Identifier generator identifier generator.
     *
     * @return the identifier generator
     */
    @Bean
    @ConditionalOnMissingBean(IdentifierGenerator.class)
    public IdentifierGenerator identifierGenerator() {
        return new DefaultIdentifierGenerator();
    }

    /**
     * Rest template rest template.
     *
     * @return the rest template
     */
    @Bean
    @ConditionalOnMissingBean(RestTemplate.class)
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder()
                .messageConverters(mappingJackson2HttpMessageConverter())
                .requestFactory(() -> new OkHttp3ClientHttpRequestFactory(OkHttpUtils.getOkHttpClient()))
                .build();
    }

    @Bean
    @ConditionalOnProperty(prefix = "spring.redis")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setKeySerializer(stringRedisSerializer);
        Jackson2JsonRedisSerializer<?> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = JacksonUtils.getObjectMapper();
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        // value序列化方式采用jackson
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        // hash的value序列化方式采用jackson
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        return redisTemplate;
    }


}
