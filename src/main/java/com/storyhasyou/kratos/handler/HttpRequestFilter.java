package com.storyhasyou.kratos.handler;

import com.storyhasyou.kratos.utils.IdentifierUtils;
import com.storyhasyou.kratos.utils.TraceIdUtils;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.StopWatch;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

/**
 * The type Http request filter.
 *
 * @author 方曦 created by 2021/1/6
 */
@Slf4j
public class HttpRequestFilter extends OncePerRequestFilter {

    /**
     * The Ignore content type.
     */
    private static final String IGNORE_CONTENT_TYPE = "multipart/form-data";

    /**
     * The constant URL_PREFIX.
     */
    private static final String URL_PREFIX = "/api";

    /**
     * Do filter internal.
     *
     * @param request     the request
     * @param response    the response
     * @param filterChain the filter chain
     * @throws ServletException the servlet exception
     * @throws IOException      the io exception
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        String traceId = request.getHeader(TraceIdUtils.TRACE_ID);
        if (StringUtils.isBlank(traceId)) {
            traceId = IdentifierUtils.uuid();
        }
        TraceIdUtils.putCurrentTraceId(traceId);
        StopWatch stopWatch = new StopWatch(traceId);
        try {
            stopWatch.start();
            filterChain.doFilter(requestWrapper, responseWrapper);
        } catch (Exception e) {
            log.error("HttpRequestFilter.doFilterInternal Exception >>> ", e);
        } finally {
            stopWatch.stop();
            log.debug("traceId:{},uri:{},totalTimeMillis:{}", traceId, request.getRequestURI(), stopWatch.getTotalTimeMillis());
            TraceIdUtils.removeCurrentTraceId();
            responseWrapper.copyBodyToResponse();
        }

    }

    /**
     * Gets headers.
     *
     * @param request the request
     * @return the headers
     */
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
