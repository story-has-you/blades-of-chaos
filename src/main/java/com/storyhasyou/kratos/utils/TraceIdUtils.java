package com.storyhasyou.kratos.utils;

import java.util.Optional;
import org.slf4j.MDC;

/**
 * The type Trace id utils.
 *
 * @author 方曦 created by 2021/1/6
 */
public class TraceIdUtils {

    /**
     * The constant TRACE_ID.
     */
    public static final String TRACE_ID = "traceId";

    /**
     * Gets current trace id.
     *
     * @return the current trace id
     */
    public static String getCurrentTraceId() {
        String traceId = MDC.get(TRACE_ID);
        return Optional.ofNullable(traceId).orElse(IdentifierUtils.uuid());
    }

    /**
     * Put current trace id.
     *
     * @param traceId the trace id
     */
    public static void putCurrentTraceId(String traceId) {
        MDC.put(TRACE_ID, traceId);
    }

    /**
     * Remove current trace id.
     */
    public static void removeCurrentTraceId() {
        MDC.clear();
    }

}
