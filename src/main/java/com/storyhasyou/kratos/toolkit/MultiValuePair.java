package com.storyhasyou.kratos.toolkit;

import java.io.Serializable;
import java.util.Collection;

/**
 * The type Multi pair.
 *
 * @param <K> the type parameter
 * @param <V> the type parameter
 * @author 方曦 created by 2020/12/30
 */
public class MultiValuePair<K, V> implements Cloneable, Serializable {

    /**
     * The Key.
     */
    private K key;

    /**
     * The Value.
     */
    private Collection<V> value;

    /**
     * Instantiates a new Multi pair.
     */
    public MultiValuePair() {
    }

    /**
     * Instantiates a new Multi pair.
     *
     * @param key   the key
     * @param value the value
     */
    public MultiValuePair(K key, Collection<V> value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Gets key.
     *
     * @return the key
     */
    public K getKey() {
        return key;
    }

    /**
     * Sets key.
     *
     * @param key the key
     */
    public void setKey(K key) {
        this.key = key;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public Collection<V> getValue() {
        return value;
    }

    /**
     * Sets value.
     *
     * @param value the value
     */
    public void setValue(Collection<V> value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MultiValuePair)) {
            return false;
        }

        MultiValuePair<?, ?> multiValuePair = (MultiValuePair<?, ?>) o;

        if (!getKey().equals(multiValuePair.getKey())) {
            return false;
        }
        return getValue().equals(multiValuePair.getValue());
    }

    @Override
    public int hashCode() {
        int result = getKey().hashCode();
        result = 31 * result + getValue().hashCode();
        return result;
    }
}
