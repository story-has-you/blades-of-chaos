package com.storyhasyou.kratos.handler;

import com.storyhasyou.kratos.utils.IdUtils;
import com.storyhasyou.kratos.utils.JacksonUtils;
import com.storyhasyou.kratos.utils.TraceIdUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.StopWatch;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

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
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        String traceId = request.getHeader(TraceIdUtils.TRACE_ID);
        if (StringUtils.isBlank(traceId)) {
            traceId = IdUtils.uuid();
        }
        TraceIdUtils.putCurrentTraceId(traceId);
        StopWatch stopWatch = new StopWatch(traceId);
        stopWatch.start();
        try {
            filterChain.doFilter(requestWrapper, responseWrapper);
        } catch (Exception e) {
            log.error("HttpRequestFilter.doFilterInternal Exception >>> ", e);
        } finally {
            stopWatch.stop();
            if (log.isDebugEnabled()) {
                if (!StringUtils.equals(IGNORE_CONTENT_TYPE, request.getContentType())
                        && request.getRequestURI().startsWith("/api")) {
                    String requestBody = new String(requestWrapper.getContentAsByteArray());
                    String responseBody = new String(responseWrapper.getContentAsByteArray());
                    log.debug("traceId:{}, requestBody:{}, responseBody:{}, totalTimeMillis:{},headers:{}", traceId,
                            requestBody, responseBody, stopWatch.getTotalTimeMillis(), JacksonUtils.serialize(this.getHeaders(request)));
                }
            }
            TraceIdUtils.removeCurrentTraceId();
            responseWrapper.copyBodyToResponse();
        }

    }

    private Map<String, Object> getHeaders(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        Map<String, Object> headers = new HashMap<>();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, request.getHeader(headerName));
        }
        return headers;
    }
}
