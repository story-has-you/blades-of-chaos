package com.storyhasyou.kratos.toolkit;

import com.storyhasyou.kratos.base.Identity;
import java.io.Serializable;

/**
 * The type Pair.
 *
 * @param <K> the type parameter
 * @param <V> the type parameter
 * @author 方曦 created by 2020/12/30
 */
public class Pair<K, V> extends Identity<Integer> {

    /**
     * The Key.
     */
    private K key;

    /**
     * The Value.
     */
    private V value;

    /**
     * Instantiates a new Pair.
     */
    public Pair() {
    }

    /**
     * Instantiates a new Pair.
     *
     * @param key   the key
     * @param value the value
     */
    public Pair(K key, V value) {
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
    public V getValue() {
        return value;
    }

    /**
     * Sets value.
     *
     * @param value the value
     */
    public void setValue(V value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return key + "=" + value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pair)) {
            return false;
        }

        Pair<?, ?> pair = (Pair<?, ?>) o;

        if (!getKey().equals(pair.getKey())) {
            return false;
        }
        return getValue().equals(pair.getValue());
    }

    @Override
    public int hashCode() {
        int result = getKey().hashCode();
        result = 31 * result + getValue().hashCode();
        return result;
    }
}
