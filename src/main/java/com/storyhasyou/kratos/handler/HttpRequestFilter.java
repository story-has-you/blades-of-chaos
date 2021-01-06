package com.storyhasyou.kratos.handler;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.storyhasyou.kratos.utils.IdUtils;
import com.storyhasyou.kratos.utils.TraceIdUtils;
import java.io.IOException;
import java.nio.charset.Charset;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletResponseWrapper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.StopWatch;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

/**
 * @author 方曦 created by 2021/1/6
 */
@Slf4j
public class HttpRequestFilter extends OncePerRequestFilter {

    /**
     * The Ignore content type.
     */
    private static final String IGNORE_CONTENT_TYPE = "multipart/form-data";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (!(request instanceof RequestBodyTwiceReadingWrapper)) {
            request = new RequestBodyTwiceReadingWrapper(request);
        }

        String traceId = request.getHeader(TraceIdUtils.TRACE_ID);
        if (StringUtils.isBlank(traceId)) {
            traceId = IdUtils.uuid();
        }
        TraceIdUtils.putCurrentTraceId(traceId);
        StopWatch stopWatch = new StopWatch(traceId);
        stopWatch.start();
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("HttpRequestFilter.doFilterInternal Exception >>> ", e);
        } finally {
            stopWatch.stop();
            if (!StringUtils.equals(IGNORE_CONTENT_TYPE, request.getContentType())) {
                log.info("traceId:{}, requestBody:{}, responseBody:{} totalTimeMillis:{}", traceId,
                        getRequestBody(request), getResponseBody(response), stopWatch.getTotalTimeMillis());
                TraceIdUtils.removeCurrentTraceId();
            }

        }

    }

    /**
     * Gets request body.
     *
     * @param request the request
     * @return the request body
     */
    private String getRequestBody(HttpServletRequest request) {
        try {
            return IOUtils.toString(request.getInputStream(), Charset.defaultCharset());
        } catch (IOException e) {
            log.error("getRequestBody Exception >>> ", e);
        }
        return StringPool.EMPTY;
    }

    /**
     * Gets response body.
     *
     * @param response the response
     * @return the response body
     */
    private String getResponseBody(HttpServletResponse response) {
        ContentCachingResponseWrapper wrapper = new ContentCachingResponseWrapper(response);
        try {
            return IOUtils.toString(wrapper.getContentAsByteArray(), wrapper.getCharacterEncoding());
        } catch (IOException e) {
            log.error("getResponseBody Exception >>> ", e);
        }
        return StringPool.EMPTY;
    }
}
