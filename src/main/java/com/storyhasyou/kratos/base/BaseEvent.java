package com.storyhasyou.kratos.base;

import lombok.Data;

import java.io.Serializable;
import java.time.Clock;

/**
 * @author 方曦 created by 2021/1/28
 */
@Data
public class BaseEvent<E> implements Serializable {

    private final Long timestamp = Clock.systemDefaultZone().millis();
    private E data;
    private Boolean success;

    public BaseEvent() {
        this(null, true);
    }

    public BaseEvent(boolean success) {
        this(null, success);
    }

    public BaseEvent(E data) {
        this(data, true);
    }

    public BaseEvent(E data, boolean success) {
        this.data = data;
        this.success = success;
    }
}
