package com.fxipp.kratos.utils;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author fangxi
 */
@Slf4j
public class OkHttpUtils {

    private static final String HTTP_JSON = "application/json; charset=utf-8";
    private static final String HTTP_FORM = "application/x-www-form-urlencoded; charset=utf-8";

    private static final OkHttpClient OK_HTTP_CLIENT = new OkHttpClient.Builder()
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .build();


    public static OkHttpClient getOkHttpClient() {
        return OK_HTTP_CLIENT;
    }

    /**
     * get请求
     * 对于小文档，响应体上的string()方法非常方便和高效。
     * 但是，如果响应主体很大(大于1 MB)，则应避免string()，
     * 因为它会将整个文档加载到内存中。在这种情况下，将主体处理为流。
     *
     * @param url
     * @return
     */
    public static String get(String url) {
        if (url == null || "".equals(url)) {
            log.error("url为null!");
            return "";
        }

        Request.Builder builder = new Request.Builder();
        Request request = builder.get().url(url).build();
        try {
            Response response = OK_HTTP_CLIENT.newCall(request).execute();
            if (response.code() == 200) {
                log.info("http GET 请求成功; [url={}]", url);
            } else {
                log.warn("Http GET 请求失败; [errorCode = {} , url={}]", response.code(), url);
            }
            return response.body().string();
        } catch (IOException e) {
            throw new RuntimeException("同步http GET 请求失败,url:" + url, e);
        }
    }

    public static byte[] getByte(String url) {
        if (url == null || "".equals(url)) {
            log.error("url为null!");
            return null;
        }

        Request.Builder builder = new Request.Builder();
        Request request = builder.get().url(url).build();
        try {
            Response response = OK_HTTP_CLIENT.newCall(request).execute();
            if (response.code() == 200) {
                log.info("http GET 请求成功; [url={}]", url);
            } else {
                log.warn("Http GET 请求失败; [errorCode = {} , url={}]", response.code(), url);
            }
            return response.body().bytes();
        } catch (IOException e) {
            throw new RuntimeException("同步http GET 请求失败,url:" + url, e);
        }
    }

    public static String get(String url, Map<String, String> headers) {
        if (CollectionUtils.isEmpty(headers)) {
            return get(url);
        }

        Request.Builder builder = new Request.Builder();
        headers.forEach(builder::header);
        Request request = builder.get().url(url).build();
        try {
            Response response = OK_HTTP_CLIENT.newCall(request).execute();
            if (response.code() == 200) {
                log.info("http GET 请求成功; [url={}]", url);
                return response.body().string();
            } else {
                log.warn("Http GET 请求失败; [errorxxCode = {} , url={}]", response.code(), url);
            }
        } catch (IOException e) {
            throw new RuntimeException("同步http GET 请求失败,url:" + url, e);
        }
        return null;
    }


    public static byte[] getByte(String url, Map<String, String> headers) {
        if (CollectionUtils.isEmpty(headers)) {
            return getByte(url);
        }

        Request.Builder builder = new Request.Builder();
        headers.forEach(builder::header);
        Request request = builder.get().url(url).build();
        try {
            Response response = OK_HTTP_CLIENT.newCall(request).execute();
            if (response.code() == 200) {
                log.info("http GET 请求成功; [url={}]", url);
                return response.body().bytes();
            } else {
                log.warn("Http GET 请求失败; [errorxxCode = {} , url={}]", response.code(), url);
            }
        } catch (IOException e) {
            throw new RuntimeException("同步http GET 请求失败,url:" + url, e);
        }
        return null;
    }

    /**
     * 同步 POST调用 无Header
     *
     * @param url
     * @param body
     * @return
     */
    public static String post(String url, String body) {
        if (url == null || "".equals(url)) {
            log.error("url为null!");
            return "";
        }

        RequestBody requestBody = RequestBody.create(body, MediaType.parse(HTTP_JSON));
        Request.Builder requestBuilder = new Request.Builder().url(url);
        Request request = requestBuilder.post(requestBody).build();
        try {
            Response response = OK_HTTP_CLIENT.newCall(request).execute();
            if (response.code() == 200) {
                log.info("http Post 请求成功; [url={}, requestContent={}]", url, body);
            } else {
                log.warn("Http POST 请求失败; [ errorCode = {}, url={}, param={}]", response.code(), url, body);
            }
            return response.body().string();
        } catch (IOException e) {
            throw new RuntimeException("同步http请求失败,url:" + url, e);
        }
    }

    /**
     * 同步 POST调用 有Header
     *
     * @param url
     * @param headers
     * @param json
     * @return
     */
    public static String post(String url, Map<String, String> headers, String json) {
        if (CollectionUtils.isEmpty(headers)) {
            post(url, json);
        }

        RequestBody body = RequestBody.create(json, MediaType.parse(HTTP_JSON));
        Request.Builder requestBuilder = new Request.Builder().url(url);
        headers.forEach(requestBuilder::addHeader);
        Request request = requestBuilder.post(body).build();
        try {
            Response response = OK_HTTP_CLIENT.newCall(request).execute();
            if (response.code() == 200) {
                log.info("http Post 请求成功; [url={}, requestContent={}]", url, json);
                return response.body().string();
            } else {
                log.warn("Http POST 请求失败; [ errorCode = {}, url={}, param={}]", response.code(), url, json);
            }
        } catch (IOException e) {
            throw new RuntimeException("同步http请求失败,url:" + url, e);
        }
        return null;
    }

    /**
     * 提交表单
     * @param url
     * @param content
     * @param headers
     * @return
     */
    public static String postDataByForm(String url, String content, Map<String, String> headers) {
        RequestBody body = RequestBody.create(content, MediaType.parse(HTTP_JSON));

        Request.Builder requestBuilder = new Request.Builder().url(url);
        if (headers != null && headers.size() > 0) {
            headers.forEach(requestBuilder::addHeader);
        }
        Request request = requestBuilder
                .post(body)
                .build();

        Response response;
        try {
            response = OK_HTTP_CLIENT.newCall(request).execute();
            if (response.code() == 200) {
                log.info("postDataByForm; [postUrl={}, requestContent={}, responseCode={}]", url, content, response.code());
            } else {
                log.warn("Http Post Form请求失败,[url={}, param={}]", url, content);
            }
            return response.body().string();
        } catch (IOException e) {
            log.error("Http Post Form请求失败,[url={}, param={}]", url, content, e);
            throw new RuntimeException("Http Post Form请求失败,url:" + url);
        }
    }

    /**
     * 异步Http调用参考模板：Get、Post、Put
     * 需要异步调用的接口一般情况下你需要定制一个专门的Http方法
     *
     * @param httpMethod
     * @param url
     * @param content
     * @return
     */
    public static <T> AsyncResult<T> asyncHttpByJson(HttpMethod httpMethod,
                                              String url,
                                              Map<String, String> headers,
                                              String content,
                                              Consumer<Response> respConsumer) {
        RequestBody body = RequestBody.create(content, MediaType.parse(HTTP_JSON));

        Request.Builder requestBuilder = new Request.Builder()
                .url(url);

        if (!CollectionUtils.isEmpty(headers)) {
            headers.forEach(requestBuilder::header);
        }

        switch (httpMethod) {
            case GET:
                requestBuilder.get();
                break;
            case POST:
                requestBuilder.post(body);
                break;
            default:
        }

        Request request = requestBuilder.build();
        Call call = OK_HTTP_CLIENT.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                log.error("异步http {} 请求失败,[url={}, param={}]", httpMethod.name(), url, content);
                throw new RuntimeException("异步http请求失败,url:" + url);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull final Response response) throws IOException {
                if (response.code() == 200) {
                    respConsumer.accept(response);
                } else {
                    log.error("异步http {} 请求失败,错误码为{},请求参数为[url={}, param={}]", httpMethod.name(), response.code(), url, content);
                }
            }
        });
        return new AsyncResult<>(null);
    }

    /**
     * lambda表达式异步调用http模板,不建议使用
     *
     * @param request
     * @param failure
     * @param responseConsumer
     */
    public static void asyncCall(Request request, Consumer<Exception> failure, Consumer<Response> responseConsumer) {
        OK_HTTP_CLIENT.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                failure.accept(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                responseConsumer.accept(response);
            }
        });
    }

}
