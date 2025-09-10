package com.storyhasyou.kratos.config;

import com.storyhasyou.kratos.exceptions.BusinessExceptionHandler;
import com.storyhasyou.kratos.utils.JacksonUtils;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
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
     * Exception handler business exception handler.
     *
     * @return the business exception handler
     */
    @Bean
    public BusinessExceptionHandler exceptionHandler() {
        return new BusinessExceptionHandler();
    }


    /**
     * Mapping jackson 2 http message converter mapping jackson 2 http message converter.
     *
     * @return the mapping jackson 2 http message converter
     */
    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        return JacksonUtils.mappingJackson2HttpMessageConverter(true);
    }

    /**
     * Rest template rest template.
     *
     * @return the rest template
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder()
                .requestFactory(OkHttp3ClientHttpRequestFactory.class)
                .messageConverters(JacksonUtils.mappingJackson2HttpMessageConverter(true))
                .build();
    }
}
