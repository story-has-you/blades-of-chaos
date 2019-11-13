package com.fxipp.kratos.function;

import java.io.Serializable;

/**
 * @author fangxi
 */
@FunctionalInterface
public interface SFunction<T> extends Serializable {

    Object get(T source);

}
