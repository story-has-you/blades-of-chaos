package com.fxipp.kratos.factory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author fangxi
 */
public enum ThreadLocalFactory {
    INSTANCE;
    private static final Map<Class<?>, ThreadLocal<?>> CACHE = new ConcurrentHashMap<>(256);

    public <T> ThreadLocal<T> getInstance(Class<T> type) {
        ThreadLocal<T> threadLocal = (ThreadLocal<T>) CACHE.get(type);
        if (threadLocal == null) {
            threadLocal = new ThreadLocal<>();
            CACHE.put(type, threadLocal);
        }
        return threadLocal;
    }

}
