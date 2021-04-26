package com.storyhasyou.kratos.utils;

import cn.hutool.extra.spring.SpringUtil;
import com.storyhasyou.kratos.base.BaseEvent;
import org.springframework.context.ApplicationEvent;

/**
 * The type Spring utils.
 *
 * @author 方曦 created by 2020/12/13
 */
public class SpringUtils extends SpringUtil {

    /**
     * Publish event.
     *
     * @param baseEvent the base event
     */
    public static void publishEvent(BaseEvent<?> baseEvent) {
        getApplicationContext().publishEvent(baseEvent);
    }

    /**
     * Publish event.
     *
     * @param event the event
     */
    public static void publishEvent(Object event) {
        getApplicationContext().publishEvent(event);
    }

    /**
     * Publish event.
     *
     * @param event the event
     */
    public static void publishEvent(ApplicationEvent event) {
        getApplicationContext().publishEvent(event);
    }
}
