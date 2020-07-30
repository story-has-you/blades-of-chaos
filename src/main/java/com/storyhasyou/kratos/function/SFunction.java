package com.storyhasyou.kratos.function;

import java.io.Serializable;
import java.util.function.Function;

/**
 * @author fangxi
 */
@FunctionalInterface
public interface SFunction<T, R> extends Function<T, R>, Serializable {

}
