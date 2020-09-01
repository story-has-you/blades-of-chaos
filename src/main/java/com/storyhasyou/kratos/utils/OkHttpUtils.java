package com.storyhasyou.kratos.utils;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.util.Assert;

/**
 * @author fangxi
 */
@Slf4j
public class OkHttpUtils {

    private static final String HTTP_JSON = "application/json; charset=utf-8";
    private static final String HTTP_FORM = "application/x-www-form-urlencoded; charset=utf-8";

    private static final int TIMEOUT = 120;

    private static final OkHttpClient OK_HTTP_CLIENT = new OkHttpClient.Builder()
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
            .sslSocketFactory(sslSocketFactory(), x509TrustManager())
            .retryOnConnectionFailure(true)
            .build();


    public static OkHttpClient getOkHttpClient() {
        return OK_HTTP_CLIENT;
    }

    public static String get(String url, Object params) {
        Map<String, Object> paramsMap = BeanUtils.describe(params);
        if (CollectionUtils.isEmpty(paramsMap)) {
            return null;
        }
        StringBuilder sb = new StringBuilder(url).append("?");
        paramsMap.forEach((key, value) -> {
            if (key != null && value != null) {
                sb.append(key).append("=").append(value).append("&");
            }
        });
        url = sb.substring(0, sb.length() - 1);
        Request.Builder builder = new Request.Builder();
        Request request = builder.get().url(url).build();
        try {
            Response response = OK_HTTP_CLIENT.newCall(request).execute();
            if (response.code() == HttpStatus.OK.value()) {
                return Objects.requireNonNull(response.body()).string();
            }
        } catch (IOException e) {
            throw new RuntimeException("同步http GET 请求失败,url:" + url, e);
        }
        return null;
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
        return get(url, null);
    }

    public static String get(String url, Map<String, String> headers) {
        Assert.hasLength(url, "url must not be null");
        Request.Builder builder = new Request.Builder();
        if (CollectionUtils.isNotEmpty(headers)) {
            headers.forEach(builder::header);
        }
        Request request = builder.get().url(url).build();
        try {
            Response response = OK_HTTP_CLIENT.newCall(request).execute();
            if (response.code() == HttpStatus.OK.value()) {
                return Objects.requireNonNull(response.body()).string();
            } else {
                log.error("Http GET 请求失败; [errorxxCode = {} , url={}]", response.code(), url);
            }
        } catch (IOException e) {
            throw new RuntimeException("同步http GET 请求失败,url:" + url, e);
        }
        return null;
    }

    public static byte[] getByte(String url) {
        return getByte(url, null);
    }

    public static byte[] getByte(String url, Map<String, String> headers) {
        Assert.hasLength(url, "url must not be null");
        Request.Builder builder = new Request.Builder();
        if (CollectionUtils.isNotEmpty(headers)) {
            headers.forEach(builder::header);
        }
        Request request = builder.get().url(url).build();
        try {
            Response response = OK_HTTP_CLIENT.newCall(request).execute();
            if (response.code() == HttpStatus.OK.value()) {
                return Objects.requireNonNull(response.body()).bytes();
            } else {
                log.error("Http GET 请求失败; [errorxxCode = {} , url={}]", response.code(), url);
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
        return post(url, body, null);
    }

    /**
     * 同步 POST调用 有Header
     *
     * @param url
     * @param headers
     * @param content
     * @return
     */
    public static String post(String url, String content, Map<String, String> headers) {
        Assert.hasLength(url, "url must not be null");
        RequestBody requestBody = RequestBody.create(MediaType.parse(HTTP_FORM), content);
        Request.Builder requestBuilder = new Request.Builder().url(url);
        if (CollectionUtils.isNotEmpty(headers)) {
            headers.forEach(requestBuilder::addHeader);
        }
        Request request = requestBuilder.post(requestBody).build();
        try {
            Response response = OK_HTTP_CLIENT.newCall(request).execute();
            if (response.code() == HttpStatus.OK.value()) {
                log.info("http Post 请求成功; [url={}, requestContent={}]", url, content);
            } else {
                log.error("Http POST 请求失败; [ errorCode = {}, url={}, param={}]", response.code(), url, content);
            }
            return response.body().string();
        } catch (IOException e) {
            throw new RuntimeException("同步http请求失败,url:" + url, e);
        }
    }

    /**
     * 提交表单
     *
     * @param url
     * @param content
     * @param headers
     * @return
     */
    public static String form(String url, String content, Map<String, String> headers) {
        Assert.hasLength(url, "url must not be null");
        RequestBody body = RequestBody.create(MediaType.parse(HTTP_FORM), content);

        Request.Builder requestBuilder = new Request.Builder().url(url);
        if (CollectionUtils.isNotEmpty(headers)) {
            headers.forEach(requestBuilder::addHeader);
        }
        Request request = requestBuilder
                .post(body)
                .build();

        Response response;
        try {
            response = OK_HTTP_CLIENT.newCall(request).execute();
            if (response.code() == HttpStatus.OK.value()) {
                log.info("postDataByForm; [postUrl={}, requestContent={}, responseCode={}]", url, content, response.code());
            } else {
                log.error("Http Post Form请求失败,[url={}, param={}]", url, content);
            }
            return Objects.requireNonNull(response.body()).string();
        } catch (IOException e) {
            log.error("Http Post Form请求失败,[url={}, param={}]", url, content, e);
            throw new RuntimeException("Http Post Form请求失败,url:" + url);
        }
    }

    /**
     * 提交表单
     *
     * @param url
     * @param content
     * @return
     */
    public static String form(String url, String content) {
        return form(url, content, null);
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
        RequestBody body = RequestBody.create(MediaType.parse(HTTP_JSON), content);

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
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                log.error("异步http {} 请求失败,[url={}, param={}]", httpMethod.name(), url, content);
                throw new RuntimeException("异步http请求失败,url:" + url);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull final Response response) throws IOException {
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
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                failure.accept(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                responseConsumer.accept(response);
            }
        });
    }


    private static X509TrustManager x509TrustManager() {
        return new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
    }

    private static SSLSocketFactory sslSocketFactory() {
        try {
            //信任任何链接
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, null, new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
            throw new RuntimeException("SSL 初始化失败");
        }
    }

}
