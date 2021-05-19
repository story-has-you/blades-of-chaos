package com.storyhasyou.kratos.utils;

import com.storyhasyou.kratos.toolkit.StringPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.time.Duration;
import java.util.Map;

/**
 * The type Rest template utils.
 *
 * @author fangxi created by 2021/5/19
 */
@Slf4j
public class RestTemplateUtils {

    private static final RestTemplate REST_TEMPLATE = new RestTemplateBuilder()
            .setConnectTimeout(Duration.ofSeconds(10L))
            .setReadTimeout(Duration.ofSeconds(10L))
            .build();


    /**
     * Get string.
     *
     * @param url the url
     * @return the string
     */
    public static String get(String url) {
        return get(url, null, null, String.class);
    }

    /**
     * Get string.
     *
     * @param url    the url
     * @param params the params
     * @return the string
     */
    public static String get(String url, Object params) {
        return get(url + describe(params), null, String.class);
    }

    /**
     * Get t.
     *
     * @param <T>        the type parameter
     * @param url        the url
     * @param params     the params
     * @param resultType the result type
     * @return the t
     */
    public static <T> T get(String url, Object params, Class<T> resultType) {
        return get(url, params, null, resultType);
    }

    /**
     * Get t.
     *
     * @param <T>        the type parameter
     * @param url        the url
     * @param params     the params
     * @param headers    the headers
     * @param resultType the result type
     * @return the t
     */
    public static <T> T get(String url, Object params, Map<String, String> headers, Class<T> resultType) {
        return exchange(url, HttpMethod.GET, params, headers, resultType);
    }

    /**
     * Post string.
     *
     * @param url    the url
     * @param params the params
     * @return the string
     */
    public static String post(String url, Object params) {
        return post(url, params, null, String.class);
    }

    /**
     * Post t.
     *
     * @param <T>        the type parameter
     * @param url        the url
     * @param params     the params
     * @param resultType the result type
     * @return the t
     */
    public static <T> T post(String url, Object params, Class<T> resultType) {
        return post(url, params, null, resultType);
    }

    /**
     * Post t.
     *
     * @param <T>        the type parameter
     * @param url        the url
     * @param params     the params
     * @param headers    the headers
     * @param resultType the result type
     * @return the t
     */
    public static <T> T post(String url, Object params, Map<String, String> headers, Class<T> resultType) {
        return exchange(url, HttpMethod.POST, params, headers, resultType);
    }


    /**
     * Exchange t.
     *
     * @param <T>        the type parameter
     * @param url        the url
     * @param method     the method
     * @param params     the params
     * @param headers    the headers
     * @param resultType the result type
     * @return the t
     */
    private static <T> T exchange(String url, HttpMethod method, Object params, Map<String, String> headers, Class<T> resultType) {
        try {

            RequestEntity<?> requestEntity;
            switch (method) {
                case GET:
                    requestEntity = RequestEntity.get(url, describe(params))
                            .accept(MediaType.APPLICATION_JSON)
                            .headers(httpHeaders -> {
                                if (CollectionUtils.isNotEmpty(headers)) {
                                    headers.forEach(httpHeaders::add);
                                }
                            })
                            .build();
                    break;
                case POST:
                case PUT:
                    requestEntity = RequestEntity.post(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .headers(httpHeaders -> {
                                if (CollectionUtils.isNotEmpty(headers)) {
                                    headers.forEach(httpHeaders::add);
                                }
                            })
                            .body(JacksonUtils.serialize(params), String.class);
                    break;
                default:
                    throw new HttpRequestMethodNotSupportedException(method.name(),
                            new String[]{HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.PUT.name()},
                            "only supports GET, POST, PUT requests");
            }
            log.info("http request start, requestEntity: {}", requestEntity);
            ResponseEntity<T> responseEntity = REST_TEMPLATE.exchange(requestEntity, resultType);
            log.info("http request end, responseEntity: {}", responseEntity);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                return responseEntity.getBody();
            }
        } catch (HttpRequestMethodNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static String describe(Object obj) {
        if (obj == null) {
            return StringPool.EMPTY;
        }
        try {
            StringBuilder append = new StringBuilder(StringPool.QUESTION_MARK);
            Class<?> clazz = obj.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Object value = field.get(obj);
                if (value != null) {
                    append.append(field.getName());
                    append.append(StringPool.EQUALS);
                    append.append(value);
                    append.append(StringPool.AMPERSAND);
                }
            }
            return append.substring(0, append.length() - 1);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return StringPool.EMPTY;
    }

}
