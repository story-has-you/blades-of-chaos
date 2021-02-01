package com.storyhasyou.kratos.base;

import java.io.Serializable;
import lombok.Data;

/**
 * @author 方曦 created by 2021/1/7
 */
@Data
public abstract class Identity<T extends Serializable> implements Serializable, Cloneable {

    private T id;

}
