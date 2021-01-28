package com.storyhasyou.kratos.base;

import lombok.Data;

/**
 * @author 方曦 created by 2021/1/28
 */
@Data
public class BaseEvent<E> {

    private E data;
    private Boolean success;

    public BaseEvent(E data) {
        this(data, true);
    }

    public BaseEvent(E data, Boolean success) {
        this.data = data;
        this.success = success;
    }
}
